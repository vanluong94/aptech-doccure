package vn.aptech.doccure.service;

import vn.aptech.doccure.entities.TimeSlot;
import vn.aptech.doccure.entities.TimeSlotDefault;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.model.ClinicOpeningTimes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TimeSlotService {

    Optional<TimeSlot> findById(Long id);

    TimeSlot saveAndFlush(TimeSlot timeSlot);

    TimeSlot save(TimeSlot timeSlot);

    List<TimeSlot> saveAll(Iterable<TimeSlot> timeSlots);

    void delete(TimeSlot timeSlot);

    void deleteAll(Iterable<TimeSlot> timeSlots);

    List<TimeSlot> findAllByDate(LocalDateTime date);

    List<TimeSlot> findAllByDoctorOnDate(User doctor, LocalDateTime date);

    List<TimeSlot> findAvailableTimeSlotByDoctorOnDate(Long id, LocalDateTime date);

    List<TimeSlot> findExistedTimeSlotByTimeSlotDefault(TimeSlotDefault timeSlotDefault);

    TimeSlot findUpcomingAvailable(User doctor);

    ClinicOpeningTimes getOpeningTimesOnDate(User doctor, LocalDateTime date);

    List<ClinicOpeningTimes> getAllOpeningTimes(User doctor);
}
