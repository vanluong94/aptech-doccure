package vn.aptech.doccure.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.aptech.doccure.entities.Appointment;
import vn.aptech.doccure.entities.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByDoctorAndStatusInAndTimeSlotTimeStartBetween(User doctor, Collection<Short> status, LocalDateTime startDate, LocalDateTime endDate);

    Page<Appointment> findAllByPatientOrderByCreatedDateDesc(User patient, Pageable pageable);

    Page<Appointment> findAllByDoctorOrderByCreatedDateDesc(User doctor, Pageable pageable);

    Page<Appointment> findAllByDoctorAndTimeSlotTimeStartAfterOrderByTimeSlotTimeStartAsc(User doctor, LocalDateTime afterTimestamp, Pageable pageable);

    Page<Appointment> findAllByPatientAndTimeSlotTimeStartAfterOrderByTimeSlotTimeStartAsc(User patient, LocalDateTime afterTimestamp, Pageable pageable);

    Page<Appointment> findAllByDoctorAndTimeSlotTimeStartBetweenOrderByTimeSlotTimeStartAsc(User doctor, LocalDateTime startTimestamp, LocalDateTime endTimestamp, Pageable pageable);

    Page<Appointment> findAllByPatientAndTimeSlotTimeStartBetweenOrderByTimeSlotTimeStartAsc(User patient, LocalDateTime startTimestamp, LocalDateTime endTimestamp, Pageable pageable);

    @Query(
            value = "SELECT * FROM appointments JOIN time_slots ON time_slots.id = appointments.time_slot_id WHERE time_slots.time_end < current_timestamp AND appointments.status NOT IN (2,3)",
            nativeQuery = true
    )
    List<Appointment> findAllPastIncomplete();

    Long countByDoctor(User doctor);

    Long countByDoctorAndTimeSlotTimeStartBetween(User doctor, LocalDateTime startTimestamp, LocalDateTime endTimestamp);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.doctor = :doctor")
    Long countPatientByDoctor(@Param("doctor") User doctor);

    @Query("SELECT u FROM Appointment a JOIN a.patient u WHERE a.doctor = :doctor GROUP BY a.patient")
    Page<User> findPatientByDoctor(@Param("doctor") User doctor, Pageable pageable);
}