package vn.aptech.doccure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    SpecialityService specialityService;

    @Autowired
    ServiceService serviceService;

    @Autowired
    private StorageService storageService;

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
