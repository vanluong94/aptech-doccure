package vn.aptech.doccure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.doccure.entities.DoctorClinic;

import java.math.BigDecimal;
import java.util.List;

public interface DoctorClinicRepository extends JpaRepository<DoctorClinic, Long> {
    List<DoctorClinic> findAllByLatBetweenAndLngBetween(BigDecimal fromLat, BigDecimal toLat, BigDecimal fromLng, BigDecimal toLng);
}