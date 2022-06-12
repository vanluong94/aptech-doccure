package vn.aptech.doccure.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    private static Logger logger = LoggerFactory.getLogger(GeneralControllerAdvice.class);
    @Autowired
    private UserService userService;

    @ModelAttribute
    public void userModelAttribute(Authentication auth, Model model) {
        if (auth != null && auth.isAuthenticated()) {
            User user = (User) auth.getPrincipal();
            Optional<User> doctor = userService.findByUsername(user.getUsername());
            if (doctor.isPresent()) {
                model.addAttribute("loggedUser", doctor.get());

                if (user.isDoctor()) {
                    model.addAttribute("doctorDto", DoctorDTO.from(doctor.get()));
                } else if (user.isPatient()) {
                    model.addAttribute("patientDto", PatientDTO.from(doctor.get()));
                }
            }
        }
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(final Throwable throwable, final Model model) {
        logger.error("Exception during execution of SpringSecurity application", throwable);
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
        model.addAttribute(Constants.MESSSAGE.ERROR, errorMessage);
        return "pages/error";
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFoundException(final Throwable throwable, final Model model) {
        logger.error("Exception during execution of SpringSecurity application", throwable);
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
        model.addAttribute(Constants.MESSSAGE.ERROR, errorMessage);
        return "error/404";
    }
}
