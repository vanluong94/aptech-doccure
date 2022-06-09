package vn.aptech.doccure.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import vn.aptech.doccure.common.Constants;
import vn.aptech.doccure.entities.Role;
import vn.aptech.doccure.service.RoleService;
import vn.aptech.doccure.service.ServiceService;
import vn.aptech.doccure.service.SpecialityService;
import vn.aptech.doccure.service.UserService;

import javax.annotation.security.RolesAllowed;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin/doctors")
@RolesAllowed("ROLE_ADMIN")
public class AdminDoctorController {
    @Autowired
    private UserService userService;

    @Autowired
    private SpecialityService specialityService;

    @Autowired
    private ServiceService serviceService;
    @Autowired
    private RoleService roleService;

    @GetMapping
    public ModelAndView getDoctorList() {
        ModelAndView modelAndView = new ModelAndView("/admin/pages/doctors/doctor-list");
        Set<Role> doctorRoles = new HashSet<>();
        doctorRoles.add(roleService.findByName(Constants.Roles.ROLE_DOCTOR));
        modelAndView.addObject("doctors", userService.findAllByRolesInOrderByIdAsc(doctorRoles));
        return modelAndView;
    }

}