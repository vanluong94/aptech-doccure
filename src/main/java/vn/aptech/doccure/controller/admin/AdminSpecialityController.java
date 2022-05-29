package vn.aptech.doccure.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.doccure.common.Constants;
import vn.aptech.doccure.entities.Speciality;
import vn.aptech.doccure.service.SpecialityService;
import vn.aptech.doccure.storage.StorageException;
import vn.aptech.doccure.storage.StorageService;
import vn.aptech.doccure.utils.StringUtils;

import javax.annotation.security.RolesAllowed;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/admin/specialities")
@RolesAllowed("ROLE_ADMIN")
public class AdminSpecialityController {

    @Autowired
    private SpecialityService specialityService;

    @Autowired
    private StorageService storageService;

    @GetMapping
    public ModelAndView showAll() {
        Iterable<Speciality> specialities = specialityService.findAll();
        ModelAndView modelAndView = new ModelAndView("admin/pages/specialities/specialities-list");
        modelAndView.addObject("specialities", specialities);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        Speciality speciality = new Speciality();
        ModelAndView modelAndView = new ModelAndView("admin/pages/specialities/specialities-new");
        modelAndView.addObject("speciality", speciality);
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable("id") Long id, RedirectAttributes redirect) {
        Optional<Speciality> speciality = specialityService.findById(id);
        ModelAndView modelAndView = new ModelAndView("admin/pages/specialities/specialities-edit");
        if (speciality.isPresent()) {
            modelAndView.addObject("speciality", speciality.get());
        } else {
            modelAndView.addObject("speciality", new Speciality());
            modelAndView.addObject("errorMessage", "Speciality not found.");
        }
        return modelAndView;
    }

    @PostMapping("/edit")
    public String update(@Validated @ModelAttribute("speciality") Speciality speciality, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "admin/pages/specialities/specialities-edit";
        }
        MultipartFile file = speciality.getImageData();
        String fileName = file.getOriginalFilename();
        speciality.setSlug(StringUtils.toNoSign(speciality.getName()).toLowerCase());
        try {
            if (file.getSize() > 0) {
                if (file.getSize() > Constants.MAX_FILE_SIZE) {
                    redirect.addFlashAttribute("errorMessage", "Max size of 2MB");
                    return "redirect:/admin/specialities/edit/" + speciality.getId();
                }
                storageService.store(file);
                speciality.setImage(fileName);
            }
        } catch (StorageException e) {
            speciality.setImage("150.png");
        }
        Speciality specialitySave = specialityService.save(speciality);
        if (specialitySave != null) {
            redirect.addFlashAttribute("successMessage", "Update successfully.");
        } else {
            redirect.addFlashAttribute("errorMessage", "Error.");
        }
        return "redirect:/admin/specialities/edit/" + speciality.getId();
    }

    @PostMapping("/create")
    public String save(@Validated @ModelAttribute("speciality") Speciality speciality, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "admin/pages/specialities/specialities-new";
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
            redirect.addFlashAttribute("globalMessage", "Create successfully.");
        } else {
            redirect.addFlashAttribute("globalMessage", "Error.");
        }
        return "redirect:/admin/specialities";
    }

    @PostMapping("/delete")
    public String update(@RequestParam("id") Long id, RedirectAttributes redirect) {
        specialityService.deleteById(id);
        redirect.addFlashAttribute("globalMessage", "Successfully deleted a speciality");
        return "redirect:/admin/specialities";
    }
}
