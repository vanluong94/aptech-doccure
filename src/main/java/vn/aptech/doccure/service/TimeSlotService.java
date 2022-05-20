package vn.aptech.doccure.service;

import vn.aptech.doccure.entities.TimeSlot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TimeSlotService {

    Optional<TimeSlot> findById(Long id);

    TimeSlot saveAndFlush(TimeSlot timeSlot);

    TimeSlot save(TimeSlot timeSlot);

    List<TimeSlot> findAllByDate(LocalDateTime date);

    List<TimeSlot> findAllByDoctorOnDate(Long id, LocalDateTime date);

}
