package vn.aptech.doccure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import vn.aptech.doccure.entities.TimeSlot;
import vn.aptech.doccure.entities.User;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    @Query(
            value = "SELECT * FROM time_slots WHERE doctor_id = :doctorId AND CAST(time_start AS DATE) = CAST(:theDate AS DATE)",
            nativeQuery = true
    )
    List<TimeSlot> findAvailableTimeSlotByDoctorOnDate(@Param("doctorId") Long doctorId, @Param("theDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime date);

    TimeSlot findFirstByDoctorAndTimeStartAfterAndAppointmentIsNullOrderByTimeStartAsc(User doctor, LocalDateTime datetime);
}