package vn.aptech.doccure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import vn.aptech.doccure.common.Constants;
import vn.aptech.doccure.entities.Role;
import vn.aptech.doccure.entities.Speciality;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.service.RoleService;
import vn.aptech.doccure.service.SpecialityService;
import vn.aptech.doccure.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private SpecialityService specialityService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("pages/home");
        /*
         * Chỉ lấy ra 10 bản ghi
         * Sắp xếp theo lượt đánh giá (Chưa làm, đang sắp xếp theo id giảm dần)
         */
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.findByName(Constants.Roles.ROLE_DOCTOR));
        Iterable<User> doctors = userService.findTop10ByRolesInOrderByIdDesc(roles);
        Iterable<Speciality> specialities = specialityService.findAllByOrderByIdDesc();
        modelAndView.addObject("doctors", doctors);
        modelAndView.addObject("specialities", specialities);
        return modelAndView;
    }

}
