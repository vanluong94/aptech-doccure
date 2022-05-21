package vn.aptech.doccure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import vn.aptech.doccure.common.Constants;
import vn.aptech.doccure.entities.Role;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.service.RoleService;
import vn.aptech.doccure.service.UserService;

import javax.annotation.security.RolesAllowed;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
@RolesAllowed("ROLE_ADMIN")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ModelAndView admin() {
        ModelAndView modelAndView = new ModelAndView("admin/pages/dashboard");
        Set<Role> doctorRoles = new HashSet<>();
        doctorRoles.add(roleService.findByName(Constants.Roles.ROLE_DOCTOR));
        Set<Role> patientRoles = new HashSet<>();
        patientRoles.add(roleService.findByName(Constants.Roles.ROLE_PATIENT));
        modelAndView.addObject("countDoctors", userService.countByRolesIn(doctorRoles));
        modelAndView.addObject("countPatients", userService.countByRolesIn(patientRoles));

        modelAndView.addObject("listDoctors", userService.findTop10ByRolesInOrderByIdDesc(doctorRoles));
        modelAndView.addObject("listPatients", userService.findTop10ByRolesInOrderByIdDesc(patientRoles));
        return modelAndView;
    }
}
