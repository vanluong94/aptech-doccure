package vn.aptech.doccure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;
import vn.aptech.doccure.entities.Appointment;
import vn.aptech.doccure.entities.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query(
            value = "SELECT * FROM appointments WHERE doctor_id = :doctorId AND CAST(time_start AS DATE) = CAST(:theDate AS DATE)",
            nativeQuery = true
    )
    List<Appointment> findAllByDoctorOnDate(@Param("doctorId") Long doctorId, @Param("theDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime date);

    List<Appointment> findAllByPatient(User patient);

    Page<Appointment> findAllByPatient(User patient, Pageable pageable);
}