package vn.aptech.doccure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.doccure.common.Constants;
import vn.aptech.doccure.entities.Role;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.service.AppointmentService;
import vn.aptech.doccure.service.RoleService;
import vn.aptech.doccure.service.UserService;
import vn.aptech.doccure.utils.StringUtils;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/admin")
@RolesAllowed("ROLE_ADMIN")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ModelAndView admin() {
        ModelAndView modelAndView = new ModelAndView("admin/pages/dashboard");
        Set<Role> doctorRoles = new HashSet<>();
        doctorRoles.add(roleService.findByName(Constants.Roles.ROLE_DOCTOR));
        Set<Role> patientRoles = new HashSet<>();
        patientRoles.add(roleService.findByName(Constants.Roles.ROLE_PATIENT));
        modelAndView.addObject("countDoctors", userService.countByRolesIn(doctorRoles));
        modelAndView.addObject("countPatients", userService.countByRolesIn(patientRoles));
        modelAndView.addObject("countAppointments", appointmentService.count());

        modelAndView.addObject("listDoctors", userService.findTop10ByRolesInOrderByIdDesc(doctorRoles));
        modelAndView.addObject("listPatients", userService.findTop10ByRolesInOrderByIdDesc(patientRoles));

        modelAndView.addObject("listAppointments", appointmentService.findTop10Latest());

        return modelAndView;
    }

    @GetMapping("/appointments")
    public String appointments() {
        return "admin/pages/appointments";
    }

    @GetMapping("/profile")
    public ModelAndView profile(Authentication authentication) {
        ModelAndView modelAndView;
        if (authentication != null) {
            User currentUser = (User) authentication.getPrincipal();
            Optional<User> user = userService.findById(currentUser.getId());
            if (user.isPresent()) {
                modelAndView = new ModelAndView("admin/pages/profile");
                modelAndView.addObject("user", user.get());
            } else {
                modelAndView = new ModelAndView("pages/404");
            }
        } else {
            modelAndView = new ModelAndView("pages/404");
        }
        return modelAndView;
    }

    @PostMapping("/profile")
    public String changeProfile(@ModelAttribute("user") User user, RedirectAttributes redirect) {
        try {
            user.setModifiedDate(LocalDateTime.now());
            if (userService.save(user) != null) {
                redirect.addFlashAttribute("successMessage", "Updated profile successfully.");
            } else {
                redirect.addFlashAttribute("errorMessage", "Updated profile fail.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirect.addFlashAttribute("errorMessage", "Updated profile fail. Detail: " + e.getMessage());
        }
        return "redirect:/admin/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(Authentication authentication, @RequestParam("password") String password, @RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword, RedirectAttributes redirect) {
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
                    return "redirect:/admin/profile";
                } else {
                    redirect.addFlashAttribute("errorMessage", "A system error has occurred. Please try again later...");
                }
            } else {
                redirect.addFlashAttribute("errorMessage", "Old Password does not match.");
            }
        }
        return "redirect:/admin/profile";
    }
}
