package vn.aptech.doccure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import vn.aptech.doccure.common.Constants;
import vn.aptech.doccure.entities.Role;
import vn.aptech.doccure.entities.Speciality;
import vn.aptech.doccure.request.SearchDoctorRequest;
import vn.aptech.doccure.service.RoleService;
import vn.aptech.doccure.service.SpecialityService;
import vn.aptech.doccure.service.UserService;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping(value = "/search")
public class SearchController {

    @Autowired
    private SpecialityService specialityService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ModelAndView search(@RequestParam(value = "location", required = false) String location, @RequestParam(value = "query", required = false) String query, @RequestParam(value = "gender", required = false) Collection<Short> gender, @RequestParam(value = "specialities", required = false) Collection<Long> specialities) {
        ModelAndView modelAndView = new ModelAndView("search");
        modelAndView.addObject("specialities", specialityService.findAll());
        List<String> roles = new ArrayList<>();
        roles.add(Constants.Roles.ROLE_DOCTOR);
        modelAndView.addObject("doctors", userService.findAllWithAdvanceSearch(location, query, gender, specialities, roles));
        return modelAndView;
    }

}
