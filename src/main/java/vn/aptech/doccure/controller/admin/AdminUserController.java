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

    @GetMapping("/{id}")
    public ModelAndView editUser(@PathVariable("id") Long id) {
        User user = userService.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        ModelAndView modelAndView = new ModelAndView("admin/pages/users/user-edit");
        modelAndView.addObject("editUser", user);
        modelAndView.addObject("userRoles", roleService.findAll());
        return modelAndView;
    }

    @PutMapping("/{id}")
    public String update(@PathVariable("id") Long id, @Validated @ModelAttribute("editUser") User user, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "admin/pages/users/user-edit";
        }
        MultipartFile file = user.getAvatarMultipartFile();
        String fileName = file.getOriginalFilename();
        try {
            if (file.getSize() > 0) {
                if (file.getSize() > Constants.MAX_FILE_SIZE) {
                    redirect.addFlashAttribute(Constants.MESSAGE.ERROR, "Max size of 2MB");
                    return "redirect:/admin/users/" + user.getId();
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
            if (userService.save(user) != null) {
                redirect.addFlashAttribute(Constants.MESSAGE.SUCCESS, "Successfully updated user profile.");
            } else {
                redirect.addFlashAttribute(Constants.MESSAGE.ERROR, "Something wrong happened, please try again later.");
            }
        } catch (Exception e) {
            logger.error("Exception when /admin/users/" + id, e);
            redirect.addFlashAttribute(Constants.MESSAGE.ERROR, "Something wrong happened, please try again later. Details: " + e.getMessage());
        }
        return "redirect:/admin/users/" + user.getId();
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam("id") Long id, RedirectAttributes redirect) {
        try {
            userService.deleteById(id);
            redirect.addFlashAttribute(Constants.MESSAGE.SUCCESS, "Successfully deleted a user");
        } catch (Exception e) {
            logger.error("Exception when /admin/users/delete", e);
            redirect.addFlashAttribute(Constants.MESSAGE.ERROR, "Something wrong happened, please try again later. Details: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
