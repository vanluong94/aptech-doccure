package vn.aptech.doccure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.service.UserService;

import java.util.Optional;

@Controller
@RequestMapping("doctor")
public class DoctorController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile/{id}")
    public ModelAndView profile(@PathVariable("id") Long id) {
        ModelAndView modelAndView;
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            modelAndView = new ModelAndView("/pages/doctor/doctor-profile");
            user.get().getClinic().parseImages();
            modelAndView.addObject("doctor", user.get());
        } else {
            modelAndView = new ModelAndView("/pages/404");
        }
        return modelAndView;
    }
}
