package vn.aptech.doccure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.doccure.common.Constants;
import vn.aptech.doccure.entities.Role;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.service.RoleService;
import vn.aptech.doccure.service.UserService;
import vn.aptech.doccure.validator.RegisterValidator;

import java.util.HashSet;
import java.util.Set;

@Controller
class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RegisterValidator registerValidator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("login")
    String login() {
        return "auth/login";
    }

    @GetMapping("register")
    public ModelAndView register(@RequestParam(value = "doctor", required = false) String doctor) {
        ModelAndView modelAndView = new ModelAndView("auth/register");
        User user;
        Set<Role> roles = new HashSet<>();
        if (doctor != null && doctor.equals("doctor")) {
            roles.add(roleService.findByName(Constants.Roles.ROLE_DOCTOR));
        } else {
            roles.add(roleService.findByName(Constants.Roles.ROLE_PATIENT));
        }
        user = new User(1, roles);
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @PostMapping("register")
    public String register(@Validated @ModelAttribute("user") User user, BindingResult result, RedirectAttributes redirect) {
        registerValidator.validate(user, result);
        if (result.hasErrors()) {
            return "auth/register";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        redirect.addFlashAttribute("globalMessage", "Register successfully.");
        return "redirect:/login";
    }
}
