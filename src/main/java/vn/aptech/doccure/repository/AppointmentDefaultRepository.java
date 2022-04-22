package vn.aptech.doccure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.aptech.doccure.entities.AppointmentDefault;
import vn.aptech.doccure.entities.AppointmentDefaultId;

@Repository
@Transactional(readOnly = true)
public interface AppointmentDefaultRepository extends JpaRepository<AppointmentDefault, AppointmentDefaultId> {
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM appointments_default WHERE doctor_id = ?1 AND weekday = ?2", nativeQuery = true)
    int deleteDoctorWeekdayTimeSlots(Long doctorId, Integer weekday);
}