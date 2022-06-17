package vn.aptech.doccure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.doccure.entities.TimeSlot;
import vn.aptech.doccure.entities.TimeSlotDefault;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.repositories.TimeSlotDefaultRepository;
import vn.aptech.doccure.service.TimeSlotDefaultService;
import vn.aptech.doccure.service.TimeSlotService;

import java.util.List;

@Service
public class TimeSlotDefaultServiceImpl implements TimeSlotDefaultService {

    @Autowired
    TimeSlotDefaultRepository repository;

    @Autowired
    TimeSlotService timeSlotService;

    @Override
    public Iterable<TimeSlotDefault> saveAll(Iterable<TimeSlotDefault> timeSlotDefaults) {
        return repository.saveAll(timeSlotDefaults);
    }

    @Override
    public Iterable<TimeSlotDefault> findAllByDoctor(User doctor) {
        return repository.findAllByDoctor(doctor);
    }

    @Override
    public void deleteAll(Iterable<TimeSlotDefault> timeSlotDefaults) {
        for (TimeSlotDefault timeSlotDefault : timeSlotDefaults) {
            List<TimeSlot> timeSlots = timeSlotService.findExistedTimeSlotByTimeSlotDefault(timeSlotDefault);
            timeSlotService.deleteAll(timeSlots);
        }
        repository.deleteAll(timeSlotDefaults);
    }
}
