package vn.aptech.doccure.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.doccure.common.Constants;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.service.RoleService;
import vn.aptech.doccure.service.UserService;
import vn.aptech.doccure.storage.StorageException;
import vn.aptech.doccure.storage.StorageService;

import javax.annotation.security.RolesAllowed;

@Controller
@RequestMapping("/admin/users")
@RolesAllowed("ROLE_ADMIN")
public class AdminUserController {
    private final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private StorageService storageService;

    @GetMapping
    public ModelAndView getUserList() {
        ModelAndView modelAndView = new ModelAndView("/admin/pages/users/user-list");
        modelAndView.addObject("users", userService.findAll());
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editUser(@PathVariable("id") Long id) {
        User user = userService.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        ModelAndView modelAndView = new ModelAndView("admin/pages/users/user-edit");
        modelAndView.addObject("editUser", user);
        modelAndView.addObject("UserRoles", roleService.findAll());
        return modelAndView;
    }

    @PostMapping("/edit")
    public String update(@Validated @ModelAttribute("editUser") User user, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "admin/pages/users/user-edit";
        }
        MultipartFile file = user.getAvatarMultipartFile();
        String fileName = file.getOriginalFilename();
        try {
            if (file.getSize() > 0) {
                if (file.getSize() > Constants.MAX_FILE_SIZE) {
                    redirect.addFlashAttribute("errorMessage", "Max size of 2MB");
                    return "redirect:/admin/users/edit/" + user.getId();
                }
                storageService.store(file);
                user.setAvatar(fileName);
            }
        } catch (StorageException e) {
            user.setAvatar("150.png");
        }
        user.getPatientAddress().setUser(user);
        user.getPatientAddress().setUserId(user.getId());
        try {
            User userSave = userService.save(user);
            if (userSave != null) {
                redirect.addFlashAttribute("successMessage", "Update successfully.");
            } else {
                redirect.addFlashAttribute("errorMessage", "Error.");
            }
        } catch (Exception e) {
            logger.error("Exception when /admin/users/edit", e);
            redirect.addFlashAttribute("errorMessage", "Error. Details: " + e.getMessage());
        }
        return "redirect:/admin/users/edit/" + user.getId();
    }

    @PostMapping("/delete")
    public String update(@RequestParam("id") Long id, RedirectAttributes redirect) {
        userService.deleteById(id);
        redirect.addFlashAttribute("successMessage", "Successfully deleted a user");
        return "redirect:/admin/users";
    }
}
