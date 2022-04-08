package vn.aptech.doccure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.service.UserService;
import vn.aptech.doccure.validator.RegisterValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RegisterValidator registerValidator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("login")
    String login() {
        return "auth/login";
    }

    @GetMapping("logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }

    @GetMapping("register")
    public ModelAndView register() {
        ModelAndView modelAndView = new ModelAndView("auth/register");
        modelAndView.addObject("user", new User(1));
        return modelAndView;
    }

    @PostMapping("register")
    public String register(@Validated @ModelAttribute("user") User user,
                           BindingResult result, RedirectAttributes redirect) {
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
