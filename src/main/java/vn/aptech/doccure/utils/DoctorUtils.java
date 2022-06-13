package vn.aptech.doccure.utils;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vn.aptech.doccure.entities.User;

public class DoctorUtils {
    public static String getDoctorProfileUrl(User doctor) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/doctor/profile/{id}")
                .buildAndExpand(doctor.getId())
                .toUriString();
    }

    public static String getDoctorBookingUrl(User doctor) {
        return getDoctorProfileUrl(doctor).concat("/booking");
    }
}
