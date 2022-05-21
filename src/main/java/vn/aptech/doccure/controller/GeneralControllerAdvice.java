package vn.aptech.doccure.controller;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.model.DoctorDTO;
import vn.aptech.doccure.model.PatientDTO;

@ControllerAdvice
public class GeneralControllerAdvice {

    @ModelAttribute
    public void userModelAttribute(Authentication auth, Model model) {
        if (auth != null && auth.isAuthenticated()) {
            User user = (User) auth.getPrincipal();
            model.addAttribute("user", user);

            if (user.isDoctor()) {
                model.addAttribute("doctorDto", DoctorDTO.from(user));
            } else if (user.isPatient()) {
                model.addAttribute("patientDto", PatientDTO.from(user));
            }
        }
    }
}
