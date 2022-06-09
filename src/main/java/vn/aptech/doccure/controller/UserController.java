package vn.aptech.doccure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.doccure.common.Constants;
import vn.aptech.doccure.entities.Service;
import vn.aptech.doccure.entities.Speciality;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.service.ServiceService;
import vn.aptech.doccure.service.SpecialityService;
import vn.aptech.doccure.service.UserService;
import vn.aptech.doccure.storage.StorageException;
import vn.aptech.doccure.storage.StorageService;
import vn.aptech.doccure.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    SpecialityService specialityService;
    @Autowired
    ServiceService serviceService;

    @Autowired
    StorageService storageService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @ModelAttribute("specialities")
    public Iterable<Speciality> specialities() {
        return specialityService.findAll();
    }

    @ModelAttribute("services")
    public Iterable<Service> services() {
        return serviceService.findAll();
    }


    @GetMapping("dashboard/profile-settings")
    @Secured({Constants.Roles.ROLE_DOCTOR, Constants.Roles.ROLE_PATIENT})
    public ModelAndView profile(HttpServletRequest request) {
        User userLogin = (User) request.getUserPrincipal();
        User newUser = userService.findById(userLogin.getId()).orElseThrow(() -> new UsernameNotFoundException(userLogin.getUsername() + " not found"));
        ModelAndView modelAndView;
        if (request.isUserInRole(Constants.Roles.ROLE_DOCTOR)) {
            modelAndView = new ModelAndView("pages/doctor/doctor-profile-settings");
            modelAndView.addObject("doctor", newUser);
        } else {
            modelAndView = new ModelAndView("pages/patient/patient-profile-settings");
            modelAndView.addObject("patient", newUser);
        }
        return modelAndView;
    }

    @PostMapping("dashboard/profile-settings")
    @Secured({Constants.Roles.ROLE_DOCTOR, Constants.Roles.ROLE_PATIENT})
    public String saveProfileSettings(@Validated User user, BindingResult result, RedirectAttributes redirect, Authentication authentication) {
        try {
            User userLogin = (User) authentication.getPrincipal();
            if (Objects.equals(user.getId(), userLogin.getId())) {
                if (result.hasErrors()) {
                    return "pages/doctor/doctor-profile-settings";
                }
                try {
                    MultipartFile file = user.getAvatarMultipartFile();
                    String fileName = file.getOriginalFilename();
                    if (file.getSize() > 0) {
                        if (file.getSize() > Constants.MAX_FILE_SIZE) {
                            redirect.addFlashAttribute("errorMessage", "Max size of 2MB");
                            return "redirect:/dashboard/profile-settings";
                        }
                        storageService.store(file);
                        user.setAvatar(fileName);
                    }
                } catch (StorageException e) {
                    user.setAvatar("avatar-admin.png");
                }
                if (user.hasRole(Constants.Roles.ROLE_DOCTOR)) {
                    user.getBio().setDoctor(user);
                    user.getBio().setDoctorId(user.getId());
                    user.setModifiedDate(LocalDateTime.now());
                } else {
                    user.getPatientBio().setPatient(user);
                    user.getPatientBio().setPatientId(user.getId());
                    user.setModifiedDate(LocalDateTime.now());
                }
                User saveUser = userService.save(user);
                if (saveUser != null) {
                    redirect.addFlashAttribute("successMessage", "Profile updated successfully");
                } else {
                    redirect.addFlashAttribute("errorMessage", "Profile update failure");
                }
            } else {
                redirect.addFlashAttribute("errorMessage", "Not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirect.addFlashAttribute("errorMessage", "Error. Detail: " + e.getMessage());
        }
        return "redirect:/dashboard/profile-settings";
    }

    @GetMapping("/dashboard/change-password")
    @Secured({Constants.Roles.ROLE_DOCTOR, Constants.Roles.ROLE_PATIENT})
    public ModelAndView userChangePassword(Authentication auth) {
        ModelAndView modelAndView;
        if (auth.isAuthenticated()) {
            User currentUser = (User) auth.getPrincipal();
            User user = userService.findById(currentUser.getId()).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
            modelAndView = new ModelAndView("/pages/change-password");
            modelAndView.addObject("user", user);
        } else {
            modelAndView = new ModelAndView("pages/404");
        }
        return modelAndView;
    }

    @PostMapping("dashboard/change-password")
    @Secured({Constants.Roles.ROLE_DOCTOR, Constants.Roles.ROLE_PATIENT, Constants.Roles.ROLE_ADMIN})
    public String changePassword(@RequestParam("password") String password, @RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword, RedirectAttributes redirect, Authentication auth) {
        if (StringUtils.isNullOrBlank(password)) {
            redirect.addFlashAttribute("errorMessage", "Old Password must not be null or empty!");
        } else if (StringUtils.isNullOrBlank(newPassword)) {
            redirect.addFlashAttribute("errorMessage", "New password must not be null or empty!");
        } else if (StringUtils.isNullOrBlank(confirmPassword)) {
            redirect.addFlashAttribute("errorMessage", "Confirm password must not be null or empty!");
        } else if (!newPassword.equals(confirmPassword)) {
            redirect.addFlashAttribute("errorMessage", "The Confirm Password confirmation does not match.");
        } else {
            User currentUser = (User) auth.getPrincipal();
            if (passwordEncoder.matches(password, currentUser.getPassword())) {
                currentUser.setPassword(passwordEncoder.encode(newPassword));
                currentUser.setModifiedDate(LocalDateTime.now());
                if (userService.save(currentUser) != null) {
                    redirect.addFlashAttribute("successMessage", "Awesome, you've successfully updated your password.");
                    return "redirect:/dashboard/change-password";
                } else {
                    redirect.addFlashAttribute("errorMessage", "A system error has occurred. Please try again later...");
                }
            } else {
                redirect.addFlashAttribute("errorMessage", "Old Password does not match.");
            }
        }
        return "redirect:/dashboard/change-password";
    }

    @GetMapping("dashboard")
    @Secured({Constants.Roles.ROLE_DOCTOR, Constants.Roles.ROLE_PATIENT})
    public String dashboard(HttpServletRequest request) {
        if (request.isUserInRole(Constants.Roles.ROLE_DOCTOR)) {
            return "pages/dashboard/doctorDashboard";
        } else if (request.isUserInRole(Constants.Roles.ROLE_PATIENT)) {
            return "pages/dashboard/patientDashboard";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/dashboard/appointments")
    @Secured({Constants.Roles.ROLE_DOCTOR, Constants.Roles.ROLE_PATIENT})
    public String dashboardAppointments(HttpServletRequest request) {
        if (request.isUserInRole(Constants.Roles.ROLE_DOCTOR)) {
            return "pages/dashboard/doctorAppointments";
        } else if (request.isUserInRole(Constants.Roles.ROLE_PATIENT)) {
            return "pages/dashboard/patientAppointments";
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("dashboard/profile")
    @Secured({Constants.Roles.ROLE_DOCTOR, Constants.Roles.ROLE_PATIENT})
    public String dashboardProfile(HttpServletRequest request) {
        if (request.isUserInRole(Constants.Roles.ROLE_DOCTOR)) {
            return "pages/doctorDashboard";
        } else if (request.isUserInRole(Constants.Roles.ROLE_PATIENT)) {
            return "pages/patientDashboard";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    private String getPrincipal() {
        String username = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }
}
