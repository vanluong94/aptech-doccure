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
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.entities.UserAddress;
import vn.aptech.doccure.service.RoleService;
import vn.aptech.doccure.service.UserService;
import vn.aptech.doccure.storage.StorageException;
import vn.aptech.doccure.storage.StorageService;

import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private StorageService storageService;

    @GetMapping("/edit/{id}")
    public ModelAndView editUser(@PathVariable("id") Long id, RedirectAttributes redirect){
        Optional<User> user = userService.findById(id);
        ModelAndView modelAndView = new ModelAndView("/admin/pages/users/user-edit");
        if(user.isPresent()){
            modelAndView.addObject("user", user.get());
            modelAndView.addObject("UserRoles", roleService.findAll());
        }else{
            modelAndView.addObject("user", new User());
            modelAndView.addObject("errorMessage", "User not found");
        }
        return modelAndView;
    }
    @PostMapping("/edit")
    public String update(@Validated @ModelAttribute("user") User user, BindingResult result, RedirectAttributes redirect){
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
        User userSave = userService.save(user);
        if (userSave != null) {
            redirect.addFlashAttribute("successMessage", "Update successfully.");
        } else {
            redirect.addFlashAttribute("errorMessage", "Error.");
        }
        return "redirect:/admin/users/edit/" + user.getId();
    }
    @PostMapping("/delete")
    public String update(@RequestParam("id") Long id, RedirectAttributes redirect) {
        userService.deleteById(id);
        redirect.addFlashAttribute("globalMessage", "Successfully deleted a user");
        return "redirect:/admin/doctors";
    }
}
