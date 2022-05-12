package vn.aptech.doccure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import vn.aptech.doccure.request.SearchDoctorRequest;
import vn.aptech.doccure.service.SpecialityService;
import vn.aptech.doccure.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/search")
public class SearchController {

    @Autowired
    private SpecialityService specialityService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ModelAndView search(@Valid @ModelAttribute("searchDoctorRequest") SearchDoctorRequest request) {
        ModelAndView modelAndView = new ModelAndView("search");
        modelAndView.addObject("searchDoctorRequest", request);
        modelAndView.addObject("specialities", specialityService.findAll());
        if (request.getGender() != null && request.getSpecialist().size() > 0) {
            modelAndView.addObject("doctors", userService.findAllByGenderInAndSpecialitiesIn(request.getGender(), request.getSpecialist()));
        } else {
            modelAndView.addObject("doctors", userService.findTop10ByOrderByIdDesc());
        }
        return modelAndView;
    }

}
