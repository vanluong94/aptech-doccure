package vn.aptech.doccure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import vn.aptech.doccure.common.Constants;
import vn.aptech.doccure.entities.Service;
import vn.aptech.doccure.entities.Speciality;
import vn.aptech.doccure.service.ServiceService;
import vn.aptech.doccure.service.SpecialityService;
import vn.aptech.doccure.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping
public class SearchController {

    @Autowired
    private SpecialityService specialityService;
    @Autowired
    private ServiceService serviceService;

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public ModelAndView search(@RequestParam(value = "location", required = false) String location,
                               @RequestParam(value = "query", required = false) String query,
                               @RequestParam(value = "gender", required = false) Collection<Short> gender,
                               @RequestParam(value = "specialities", required = false) Collection<Long> specialities,
                               @RequestParam(value = "services", required = false) Collection<Long> services) {
        ModelAndView modelAndView = new ModelAndView("search");
        modelAndView.addObject("specialities", specialityService.findAll());
        modelAndView.addObject("services", serviceService.findAll());
        List<String> roles = new ArrayList<>();
        roles.add(Constants.Roles.ROLE_DOCTOR);
        modelAndView.addObject("doctors", userService.findAllWithAdvanceSearch(location, query, gender, specialities, services, roles));
        return modelAndView;
    }

    @GetMapping("/search-nearby")
    public String searchNearby() {
        return "/pages/nearby";
    }

    @GetMapping("/speciality/{slug}")
    public ModelAndView searchBySpeciality(@PathVariable("slug") String slug) {
        Optional<Speciality> speciality = specialityService.findBySlug(slug);
        ModelAndView modelAndView;
        if (speciality.isPresent()) {
            modelAndView = new ModelAndView("search");
            modelAndView.addObject("specialities", specialityService.findAll());
            modelAndView.addObject("services", serviceService.findAll());
            modelAndView.addObject("specialityId", speciality.get().getId());
            List<String> roles = new ArrayList<>();
            roles.add(Constants.Roles.ROLE_DOCTOR);
            modelAndView.addObject("doctors", userService.findAllBySpecialitySlug(slug, roles));
        } else {
            modelAndView = new ModelAndView("pages/404");
        }
        return modelAndView;
    }

    @GetMapping("/service/{slug}")
    public ModelAndView searchByService(@PathVariable("slug") String slug) {
        Optional<Service> service = serviceService.findBySlug(slug);
        ModelAndView modelAndView;
        if (service.isPresent()) {
            modelAndView = new ModelAndView("search");
            modelAndView.addObject("specialities", specialityService.findAll());
            modelAndView.addObject("services", serviceService.findAll());
            modelAndView.addObject("serviceId", service.get().getId());
            List<String> roles = new ArrayList<>();
            roles.add(Constants.Roles.ROLE_DOCTOR);
            modelAndView.addObject("doctors", userService.findAllByServiceSlug(slug, roles));
        } else {
            modelAndView = new ModelAndView("pages/404");
        }
        return modelAndView;
    }

}
