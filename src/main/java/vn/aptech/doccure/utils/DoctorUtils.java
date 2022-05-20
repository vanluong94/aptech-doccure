package vn.aptech.doccure.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vn.aptech.doccure.entities.PatientFavorite;
import vn.aptech.doccure.entities.PatientFavoriteId;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.service.PatientFavoriteService;

import java.util.Optional;

public class DoctorUtils {
    public static String getDoctorProfileUrl(User doctor) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/doctor/profile/{id}")
                .buildAndExpand(doctor.getId())
                .toUriString();
    }
}
