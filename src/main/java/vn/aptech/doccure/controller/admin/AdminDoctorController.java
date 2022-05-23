package vn.aptech.doccure.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import vn.aptech.doccure.service.ServiceService;
import vn.aptech.doccure.service.SpecialityService;
import vn.aptech.doccure.service.UserService;

@Controller
@RequestMapping("/admin/doctors")
public class AdminDoctorController {
    @Autowired
    private UserService userService;
    @Autowired
    private SpecialityService specialityService;
    @Autowired
    private ServiceService serviceService;
    @GetMapping
    public ModelAndView getList(){
        ModelAndView modelAndView = new ModelAndView("/admin/pages/doctors/doctor-list");
        modelAndView.addObject("doctors", userService.findAll());
        return modelAndView;
    }
    @GetMapping("{id}")
    public ModelAndView editDoctor(@PathVariable("id") Long id){
        ModelAndView modelAndView = new ModelAndView("/admin/pages/user-edit");
        return modelAndView;
    }
}