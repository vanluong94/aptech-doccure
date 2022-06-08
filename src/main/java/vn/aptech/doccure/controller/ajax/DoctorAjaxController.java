package vn.aptech.doccure.controller.ajax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.aptech.doccure.common.AjaxResponse;
import vn.aptech.doccure.entities.DoctorClinic;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.model.DoctorDTO;
import vn.aptech.doccure.service.ClinicService;
import vn.aptech.doccure.service.PatientFavoriteService;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("ajax/doctors")
public class DoctorAjaxController {

    @Autowired
    ClinicService clinicService;

    @Autowired
    PatientFavoriteService favoriteService;

    @GetMapping("searchByMap")
    public ResponseEntity<Object> searchByMap(
            @RequestParam BigDecimal fromLat,
            @RequestParam BigDecimal toLat,
            @RequestParam BigDecimal fromLng,
            @RequestParam BigDecimal toLng,
            Authentication authentication
    ) {
        Map<String, Object> response = new LinkedHashMap<>();
        List<Object> doctorResults = new LinkedList<>();

        User currentUser = (User) authentication.getPrincipal();

        for (DoctorClinic clinic : clinicService.findAllByMapBound(fromLat, toLat, fromLng, toLng)) {
            Map<String, Object> item = new LinkedHashMap<>();

            item.put("doctor", DoctorDTO.from(clinic.getDoctor()));
            item.put("lat", clinic.getLat());
            item.put("lng", clinic.getLng());
            item.put("isFavorite", authentication.isAuthenticated() ? favoriteService.isDoctorFavorited(clinic.getDoctor(), currentUser) : 0);

            doctorResults.add(item);
        }

        response.put("results", doctorResults);

        return AjaxResponse.responseSuccess(response, "success");
    }
}
