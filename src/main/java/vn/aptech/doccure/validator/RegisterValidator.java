package vn.aptech.doccure.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.service.UserService;

@Component
public class RegisterValidator implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "", "The Confirm Password confirmation does not match");
        }

        if (userService.existByEmail(user.getEmail())) {
            errors.rejectValue("email", "", "Email is Already Registered");
        }
    }
}