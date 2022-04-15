package vn.aptech.doccure.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;
import vn.aptech.doccure.common.Constants;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "pages/home";
    }

    @GetMapping("dashboard")
    @Secured({"ROLE_ADMIN", "ROLE_DOCTOR", "ROLE_PATIENT"})
    public String dashboard(HttpServletRequest request) {
        if (request.isUserInRole(Constants.Roles.ROLE_DOCTOR)) {
            return "pages/doctorDashboard";
        } else if (request.isUserInRole(Constants.Roles.ROLE_PATIENT)) {
            return "pages/patientDashboard";
        } else if (request.isUserInRole(Constants.Roles.ROLE_ADMIN)) {
            return "admin/pages/dashboard";
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

}
