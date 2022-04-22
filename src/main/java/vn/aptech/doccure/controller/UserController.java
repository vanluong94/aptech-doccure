package vn.aptech.doccure.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import vn.aptech.doccure.common.Constants;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    @GetMapping("dashboard")
    @Secured({"ROLE_ADMIN", "ROLE_DOCTOR", "ROLE_PATIENT"})
    public String dashboard(HttpServletRequest request) {
        if (request.isUserInRole(Constants.Roles.ROLE_DOCTOR)) {
            return "pages/dashboard/doctorDashboard";
        } else if (request.isUserInRole(Constants.Roles.ROLE_PATIENT)) {
            return "pages/dashboard/patientDashboard";
        } else if (request.isUserInRole(Constants.Roles.ROLE_ADMIN)) {
            return "admin/pages/dashboard";
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
