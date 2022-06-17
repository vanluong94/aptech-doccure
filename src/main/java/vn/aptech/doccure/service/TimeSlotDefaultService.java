package vn.aptech.doccure.service;

import vn.aptech.doccure.entities.TimeSlotDefault;
import vn.aptech.doccure.entities.User;

public interface TimeSlotDefaultService {

    Iterable<TimeSlotDefault> saveAll(Iterable<TimeSlotDefault> timeSlotDefaults);

    Iterable<TimeSlotDefault> findAllByDoctor(User doctor);

    void deleteAll(Iterable<TimeSlotDefault> timeSlotDefaults);

}
