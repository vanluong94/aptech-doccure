package vn.aptech.doccure.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.doccure.DatabaseLoader;
import vn.aptech.doccure.common.Constants;
import vn.aptech.doccure.entities.Speciality;
import vn.aptech.doccure.service.SpecialityService;
import vn.aptech.doccure.storage.StorageException;
import vn.aptech.doccure.storage.StorageService;
import vn.aptech.doccure.utils.StringUtils;

import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/admin/specialities")
public class AdminSpecialityController {
    private final Logger logger = LoggerFactory.getLogger(AdminSpecialityController.class);

    @Autowired
    private SpecialityService specialityService;

    @Autowired
    private StorageService storageService;

    @GetMapping
    public ModelAndView showAll() {
        Iterable<Speciality> specialities = specialityService.findAll();
        ModelAndView modelAndView = new ModelAndView(Constants.PAGE_VIEW.ADMIN.SPECIALITIES.LIST_PAGE);
        modelAndView.addObject(Constants.OBJECT.SPECIALITIES, specialities);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        Speciality speciality = new Speciality();
        ModelAndView modelAndView = new ModelAndView(Constants.PAGE_VIEW.ADMIN.SPECIALITIES.NEW_PAGE);
        modelAndView.addObject(Constants.OBJECT.SPECIALITY, speciality);
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable("id") Long id) {
        Optional<Speciality> speciality = specialityService.findById(id);
        ModelAndView modelAndView = new ModelAndView(Constants.PAGE_VIEW.ADMIN.SPECIALITIES.EDIT_PAGE);
        if (speciality.isPresent()) {
            modelAndView.addObject(Constants.OBJECT.SPECIALITY, speciality.get());
        } else {
            modelAndView.addObject(Constants.OBJECT.SPECIALITY, new Speciality());
            modelAndView.addObject(Constants.MESSSAGE.ERROR, "Could not find this item.");
        }
        return modelAndView;
    }

    @PostMapping("/edit")
    public String update(@Validated @ModelAttribute("speciality") Speciality speciality, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return Constants.PAGE_VIEW.ADMIN.SPECIALITIES.EDIT_PAGE;
        }
        MultipartFile file = speciality.getImageData();
        String fileName = file.getOriginalFilename();
        speciality.setSlug(StringUtils.toNoSign(speciality.getName()).toLowerCase());
        try {
            if (file.getSize() > 0) {
                if (file.getSize() > Constants.MAX_FILE_SIZE) {
                    redirect.addFlashAttribute(Constants.MESSSAGE.ERROR, "Max size of 2MB");
                    return "redirect:/admin/specialities/edit/" + speciality.getId();
                }
                storageService.store(file);
                speciality.setImage(fileName);
            }
        } catch (StorageException e) {
            speciality.setImage("150.png");
        }
        try {
            Speciality specialitySave = specialityService.save(speciality);
            if (specialitySave != null) {
                redirect.addFlashAttribute(Constants.MESSSAGE.SUCCESS, "The item has been updated successfully.");
            } else {
                redirect.addFlashAttribute(Constants.MESSSAGE.ERROR, "Cannot update item " + speciality.getName() + ".");
            }
        } catch (Exception e) {
            logger.error("Exception when /admin/specialities/edit", e);
            redirect.addFlashAttribute(Constants.MESSSAGE.ERROR, "Cannot update item " + speciality.getName() + ". Details: " + e.getMessage());
        }
        return "redirect:/admin/specialities/edit/" + speciality.getId();
    }

    @PostMapping("/create")
    public String save(@Validated @ModelAttribute("speciality") Speciality speciality, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return Constants.PAGE_VIEW.ADMIN.SPECIALITIES.NEW_PAGE;
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
        try {
            Speciality specialitySave = specialityService.save(speciality);
            if (specialitySave != null) {
                redirect.addFlashAttribute(Constants.MESSSAGE.SUCCESS, "The item has been created successfully.");
            } else {
                redirect.addFlashAttribute(Constants.MESSSAGE.ERROR, "Cannot create item " + speciality.getName() + ".");
            }
        } catch (Exception e) {
            logger.error("Exception when /admin/specialities/create", e);
            redirect.addFlashAttribute(Constants.MESSSAGE.ERROR, "Cannot create item " + speciality.getName() + ". Details: " + e.getMessage());
        }
        return "redirect:/admin/specialities";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id, RedirectAttributes redirect) {
        try {
            specialityService.deleteById(id);
            redirect.addFlashAttribute(Constants.MESSSAGE.SUCCESS, "The item has been successfully deleted.");
        } catch (Exception e) {
            logger.error("Exception when /admin/specialities/delete", e);
            redirect.addFlashAttribute(Constants.MESSSAGE.ERROR, "The selected item cannot be deleted. Details: " + e.getMessage());
        }
        return "redirect:/admin/specialities";
    }
}
