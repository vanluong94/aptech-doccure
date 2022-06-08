package vn.aptech.doccure.service;

import vn.aptech.doccure.entities.DoctorClinic;

import java.math.BigDecimal;
import java.util.List;

public interface ClinicService {
    List<DoctorClinic> findAllByMapBound(BigDecimal fromLat, BigDecimal toLat, BigDecimal fromLng, BigDecimal toLng);
}
