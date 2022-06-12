package vn.aptech.doccure.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import vn.aptech.doccure.common.Constants;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.model.DoctorDTO;
import vn.aptech.doccure.model.PatientDTO;
import vn.aptech.doccure.service.UserService;

import java.util.Optional;

@ControllerAdvice
public class GeneralControllerAdvice {

    Logger logger = LoggerFactory.getLogger(GeneralControllerAdvice.class);

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void userModelAttribute(Authentication auth, Model model) {
       this.addUserModelAttribute(model, auth);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(final Throwable throwable, final Model model, Authentication authentication) {
        logger.error("Exception during execution of SpringSecurity application", throwable);
        this.addUserModelAttribute(model, authentication);
        this.addErrorMessage(model, (throwable != null ? throwable.getMessage() : "Unknown error"));
        return "error";
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFoundException(final Throwable throwable, final Model model, Authentication authentication) {
        logger.error("Exception during execution of SpringSecurity application", throwable);
        this.addUserModelAttribute(model, authentication);
        this.addErrorMessage(model, (throwable != null ? throwable.getMessage() : "Unknown error"));
        return "error/404";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String accessDeniedException(final Throwable throwable, final Model model, Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        this.addUserModelAttribute(model, authentication);
        this.addErrorMessage(model, "You are unable to access this page");
        return "error";
    }

    public void addUserModelAttribute(Model model, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            User user = (User) auth.getPrincipal();
            Optional<User> doctor = userService.findByUsername(user.getUsername());
            if (doctor.isPresent()) {
                model.addAttribute("user", doctor.get());

                if (user.isDoctor()) {
                    model.addAttribute("doctorDto", DoctorDTO.from(doctor.get()));
                } else if (user.isPatient()) {
                    model.addAttribute("patientDto", PatientDTO.from(doctor.get()));
                }
            }
        }
    }

    public void addErrorMessage(Model model, String errorMessage) {
        model.addAttribute(Constants.MESSAGE.ERROR, errorMessage);
    }
}
