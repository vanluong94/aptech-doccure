package vn.aptech.doccure.controller;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import vn.aptech.doccure.entities.User;

@ControllerAdvice
public class GeneralControllerAdvice {

    @ModelAttribute
    public void userModelAttribute(Authentication auth, Model model) {
        if (auth.isAuthenticated()) {
            model.addAttribute("user", (User) auth.getPrincipal());
        }
    }
}
