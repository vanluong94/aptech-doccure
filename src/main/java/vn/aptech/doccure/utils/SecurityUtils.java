package vn.aptech.doccure.utils;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.aptech.doccure.SpringContext;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.service.UserService;

import java.util.Optional;

public class SecurityUtils {

    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }

    public static User getAuthenticatedUser() {
        if (isAuthenticated()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            Optional<User> userOptional = SpringContext.getBean(UserService.class).findById(user.getId());

           return userOptional.orElse(user);
        }
        return null;
    }

}
