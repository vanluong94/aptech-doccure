package vn.aptech.doccure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import vn.aptech.doccure.common.Constants;
import vn.aptech.doccure.entities.Speciality;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.service.SpecialityService;
import vn.aptech.doccure.service.UserService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private SpecialityService specialityService;

    @GetMapping
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("pages/home");
        /*
         * Chỉ lấy ra 10 bản ghi
         * Sắp xếp theo lượt đánh giá
         */
        Iterable<User> doctors = userService.findTop10ByOrderByIdDesc();
        /*
         * Chỉ lấy ra 10 bản ghi
         * Sắp xếp theo id giảm dần
         */
        Iterable<Speciality> specialities = specialityService.findAllByOrderByIdDesc();
        modelAndView.addObject("doctors", doctors);
        modelAndView.addObject("specialities", specialities);
        return modelAndView;
    }

}
