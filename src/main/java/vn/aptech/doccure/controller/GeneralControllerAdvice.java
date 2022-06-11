package vn.aptech.doccure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.model.DoctorDTO;
import vn.aptech.doccure.model.PatientDTO;
import vn.aptech.doccure.service.UserService;

import java.util.Optional;

@ControllerAdvice
public class GeneralControllerAdvice {

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void userModelAttribute(Authentication auth, Model model) {
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
}
