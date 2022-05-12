package vn.aptech.doccure.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import vn.aptech.doccure.request.SearchDoctorRequest;

@Controller
@RequestMapping(value = "/search")
public class SearchController {

    @GetMapping
    public ModelAndView search(@ModelAttribute("searchDoctorRequest") SearchDoctorRequest request) {
        ModelAndView modelAndView = new ModelAndView("search");
        modelAndView.addObject("searchDoctorRequest", request);
        return modelAndView;
    }

}
