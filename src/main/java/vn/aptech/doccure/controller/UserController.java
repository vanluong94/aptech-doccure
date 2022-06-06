package vn.aptech.doccure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Controller
public class UserController {
    private static final long MAX_FILE_SIZE = 1048576; // 2097152 = 2mb
    @Autowired
    private UserService userService;
    @Autowired
    SpecialityService specialityService;
    @Autowired
    ServiceService serviceService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @ModelAttribute("specialities")
    public Iterable<Speciality> specialities() {
        Iterable<Speciality> specialities = specialityService.findAll();
        specialities.forEach(speciality -> System.out.println(speciality.getName()));
        return specialities;
    }
    @ModelAttribute("services")
    public Iterable<Service> services() {
        Iterable<Service> services = serviceService.findAll();
        services.forEach(service -> System.out.println(service.getName()));
        return services;
    }
    @GetMapping("dashboard/profile-settings")
    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    public ModelAndView profile(HttpServletRequest request, @ModelAttribute("user") User user, Principal principal) {
        if (request.isUserInRole(Constants.Roles.ROLE_DOCTOR)) {
            Authentication authentication = (Authentication) principal;
            User doctor = (User) authentication.getPrincipal();
            ModelAndView modelAndView;
            Optional<User> newUser = userService.findByUsername(doctor.getUsername());
            if (newUser.isPresent()) {
                modelAndView = new ModelAndView("pages/doctor/doctor-profile-settings");
                modelAndView.addObject("doctor", newUser.get());
            } else {
                modelAndView = new ModelAndView("pages/404");
            }
            return modelAndView;
        } else if (request.isUserInRole(Constants.Roles.ROLE_PATIENT)) {
            Authentication authentication = (Authentication) principal;
            User patient = (User) authentication.getPrincipal();
            ModelAndView modelAndView;
            Optional<User> newUser = userService.findByUsername(patient.getUsername());
            if(newUser.isPresent()){
                modelAndView = new ModelAndView("pages/patient/patient-profile-settings");
                modelAndView.addObject("patient", newUser.get());
            } else{
                modelAndView = new ModelAndView("pages/404");
            }
            return modelAndView;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    @PostMapping("dashboard/profile-settings")
    public String saveProfileSettings(@Validated @ModelAttribute("user") User user, BindingResult result, RedirectAttributes redirect) {
        if(user.hasRole(Constants.Roles.ROLE_DOCTOR)){
            User doctor = user;
            try {
                if (result.hasErrors()) {
                    return "pages/doctor/doctor-profile-settings";
                }
                MultipartFile file = doctor.getAvatarMultipartFile();
                String fileName = file.getOriginalFilename();
                try {
                    if (file.getSize() > 0) {
                        if (file.getSize() > MAX_FILE_SIZE) {
                            redirect.addFlashAttribute("errorMessage", "Max size of 2MB");
                            return "redirect:/dashboard/profile-settings";
                        }
                        storageService.store(file);
                        doctor.setAvatar(fileName);
                    }
                } catch (StorageException e) {
                    doctor.setAvatar("avatar-admin.png");
                }

                Set<Service> services = doctor.getServices();
                System.out.println("service size: ------------------ " + services.size());
                services.forEach(service -> {
                    System.out.println("service: --------------- " + service.getId() + "/" + service.getName());
                });

                Set<Speciality> specialities = doctor.getSpecialities();
                System.out.println("specialities size: ------------------ " + services.size());
                specialities.forEach(speciality -> {
                    System.out.println("speciality: --------------- " + speciality.getId() + "/" + speciality.getName());
                });
                doctor.getBio().setDoctor(doctor);
                doctor.getBio().setDoctorId(doctor.getId());
                doctor.setModifiedDate(LocalDateTime.now());
                User saveUser = userService.save(doctor);
                if (saveUser != null) {
                    // Luu thanh cong
                    redirect.addFlashAttribute("successMessage", "Successfully updated profile.");
                } else {
                    // Da xay ra loi
                    redirect.addFlashAttribute("errorMessage", "Error.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                redirect.addFlashAttribute("errorMessage", "Error. Detail: " + e.getMessage());
            }
            return "redirect:/dashboard/profile-settings";
        }else if(user.hasRole(Constants.Roles.ROLE_PATIENT)){
            User patient = user;
            try {
                if (result.hasErrors()) {
                    return "pages/patient/patient-profile-settings";
                }
                MultipartFile file = patient.getAvatarMultipartFile();
                String fileName = file.getOriginalFilename();
                try {
                    if (file.getSize() > 0) {
                        if (file.getSize() > MAX_FILE_SIZE) {
                            redirect.addFlashAttribute("errorMessage", "Max size of 2MB");
                            return "redirect:/dashboard/profile-settings";
                        }
                        storageService.store(file);
                        patient.setAvatar(fileName);
                    }
                } catch (StorageException e) {
                    patient.setAvatar("avatar-admin.png");
                }
                patient.getPatientBio().setPatient(patient);
                patient.getPatientBio().setPatientId(patient.getId());
                patient.setModifiedDate(LocalDateTime.now());
                User saveUser = userService.save(patient);
                if (saveUser != null) {
                    // Luu thanh cong
                    redirect.addFlashAttribute("successMessage", "Successfully updated profile.");
                } else {
                    // Da xay ra loi
                    redirect.addFlashAttribute("errorMessage", "Error.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                redirect.addFlashAttribute("errorMessage", "Error. Detail: " + e.getMessage());
            }
            return "redirect:/dashboard/profile-settings";
        }
        else{
            return "redirect:/dashboard/profile-settings";
        }
    }
    @GetMapping("/dashboard/change-password")
    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    public ModelAndView userChangePassword(Authentication authentication, Principal principal) {
        ModelAndView modelAndView = new ModelAndView("pages/404");;
        if(authentication != null){
            User currentUser = (User) authentication.getPrincipal();
            if(!currentUser.isAdmin() && (currentUser.isDoctor() || currentUser.isPatient())){
                Optional<User> user = userService.findById(currentUser.getId());
                if (user.isPresent()) {
                    modelAndView = new ModelAndView("/pages/change-password");
                    modelAndView.addObject("user", user.get());
                }
            }
        }
        return modelAndView;
    }
    @PostMapping("dashboard/change-password")
    public String changePassword(Authentication authentication,
                                 @RequestParam("password") String password,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 RedirectAttributes redirect) {
        if (StringUtils.isNullOrBlank(password)) {
            redirect.addFlashAttribute("errorMessage", "Old Password must not be null or empty!");
        } else if (StringUtils.isNullOrBlank(newPassword)) {
            redirect.addFlashAttribute("errorMessage", "New password must not be null or empty!");
        } else if (StringUtils.isNullOrBlank(confirmPassword)) {
            redirect.addFlashAttribute("errorMessage", "Confirm password must not be null or empty!");
        } else if (!newPassword.equals(confirmPassword)) {
            redirect.addFlashAttribute("errorMessage", "The Confirm Password confirmation does not match.");
        } else {
            User currentUser = (User) authentication.getPrincipal();
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
    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    public String dashboard(HttpServletRequest request) {
        if (request.isUserInRole(Constants.Roles.ROLE_DOCTOR)) {
            return "pages/dashboard/doctorDashboard";
        } else if (request.isUserInRole(Constants.Roles.ROLE_PATIENT)) {
            return "pages/dashboard/patientDashboard";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/dashboard/appointments")
    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    public String dashboardAppointments(HttpServletRequest request) {
        if (request.isUserInRole(Constants.Roles.ROLE_DOCTOR)) {
            return "pages/dashboard/doctorAppointments";
        } else if (request.isUserInRole(Constants.Roles.ROLE_PATIENT)) {
            return "pages/dashboard/patientAppointments";
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("dashboard/profile")
    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    public String profile(HttpServletRequest request) {
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
