package vn.aptech.doccure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.doccure.entities.DoctorClinic;
import vn.aptech.doccure.repositories.DoctorClinicRepository;
import vn.aptech.doccure.service.ClinicService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ClinicServiceImpl implements ClinicService {

    @Autowired
    DoctorClinicRepository clinicRepository;

    @Override
    public List<DoctorClinic> findAllByMapBound(BigDecimal fromLat, BigDecimal toLat, BigDecimal fromLng, BigDecimal toLng) {
        return clinicRepository.findAllByLatBetweenAndLngBetween(fromLat, toLat, fromLng, toLng);
    }
}
