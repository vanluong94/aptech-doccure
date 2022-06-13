package vn.aptech.doccure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.aptech.doccure.entities.PatientFavorite;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.model.DoctorDTO;
import vn.aptech.doccure.service.PatientFavoriteService;

import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
@Secured("ROLE_PATIENT")
public class PatientDashboardController {

    @Autowired
    private PatientFavoriteService favoriteService;

    @GetMapping("/favorites")
    public String favoritePage(Authentication authentication, Model model, @RequestParam(defaultValue = "1") int page) {

        User patient = (User) authentication.getPrincipal();
        Pageable pageable = PageRequest.of(--page, 8);

        List<DoctorDTO> favoriteDoctors = new LinkedList<>();

        Page<PatientFavorite> favoriteResult = favoriteService.findByPatient(patient, pageable);
        for (PatientFavorite favorite : favoriteResult.getContent()) {
            favoriteDoctors.add(new DoctorDTO(favorite.getId().getDoctor()));
        }

        model.addAttribute("favoriteDoctors", favoriteDoctors);
        model.addAttribute("favoriteResult", favoriteResult);

        return "pages/dashboard/patientFavorites";

    }
}
