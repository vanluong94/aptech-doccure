package vn.aptech.doccure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.doccure.entities.Speciality;
import vn.aptech.doccure.service.SpecialityService;
import vn.aptech.doccure.storage.StorageException;
import vn.aptech.doccure.storage.StorageService;
import vn.aptech.doccure.utils.StringUtils;

import java.util.Locale;

@Controller
@RequestMapping("/admin/specialities")
public class AdminSpecialityController {

    @Autowired
    private SpecialityService specialityService;

    @Autowired
    private StorageService storageService;

    @GetMapping
    public ModelAndView showAll() {
        Iterable<Speciality> specialities = specialityService.findAll();
        ModelAndView modelAndView = new ModelAndView("admin/specialities/specialities-list");
        modelAndView.addObject("specialities", specialities);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        Speciality speciality = new Speciality();
        ModelAndView modelAndView = new ModelAndView("admin/specialities/specialities-new");
        modelAndView.addObject("speciality", speciality);
        return modelAndView;
    }

    @PostMapping("/create")
    public String save(@Validated @ModelAttribute("speciality") Speciality speciality, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "admin/specialities/specialities-new";
        }
        MultipartFile multipartFile = speciality.getImageData();
        String fileName = multipartFile.getOriginalFilename();
        speciality.setSlug(StringUtils.toNoSign(speciality.getName()).toLowerCase());
        try {
            storageService.store(multipartFile);
            speciality.setImage(fileName);
        } catch (StorageException e) {
            speciality.setImage("150.png");
        }
        Speciality specialitySave = specialityService.save(speciality);
        if (specialitySave != null) {
            redirect.addFlashAttribute("globalMessage", "Register successfully.");
        } else {
            redirect.addFlashAttribute("globalMessage", "Error.");
        }
        return "redirect:/admin/specialities";
    }
}
