package vn.aptech.doccure.service;

import vn.aptech.doccure.entities.TimeSlot;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeSlotService {

    List<TimeSlot> findAllByDate(LocalDateTime date);

}
