package vn.aptech.doccure.controller.ajax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.aptech.doccure.common.AjaxResponse;
import vn.aptech.doccure.entities.TimeSlotDefault;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.repositories.TimeSlotDefaultRepository;
import vn.aptech.doccure.repositories.UserRepository;

import java.util.*;

@RestController
@RequestMapping("ajax/timeSlots")
@Secured("ROLE_DOCTOR")
public class TimeSlotController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TimeSlotDefaultRepository timeSlotDefaultRepository;

    @GetMapping("getAll")
    @ResponseBody
    public ResponseEntity<Object> getAll(Authentication auth) {
        User doctor = (User) auth.getPrincipal();
        Map<String, Object> respData = new HashMap<>();
        respData.put("timeSlots", timeSlotDefaultRepository.findAllByDoctor(doctor));
        return AjaxResponse.responseSuccess(respData, "success");
    }

    @PostMapping("saveAll")
    @ResponseBody
    public ResponseEntity<Object> updateAll(Authentication auth, @RequestBody(required = false) List<TimeSlotDefault> postedTimeSlots) {

        Map<String, Object> results = new LinkedHashMap<>();

        User doctor = (User) auth.getPrincipal();

        Set<TimeSlotDefault> updateOnes = new TreeSet<>(new Comparator<TimeSlotDefault>() {
            @Override
            public int compare(TimeSlotDefault o1, TimeSlotDefault o2) {
                if (o1.getWeekday() < o2.getWeekday()) {
                    return -1;
                } else if (o1.getWeekday() > o2.getWeekday()) {
                    return 1;
                } else if (o1.getTimeStart().isBefore(o2.getTimeStart())) {
                    return -1;
                } else if (o1.getTimeStart().equals(o2.getTimeEnd())) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        for (TimeSlotDefault apmtDefault : postedTimeSlots) {
            if (apmtDefault.isTimeRangeValid()) {
                apmtDefault.setDoctor(doctor);
                apmtDefault.setDoctorId(doctor.getId());
                updateOnes.add(apmtDefault);
            }
        }
        
        List<TimeSlotDefault> deleteOnes = new ArrayList<>();
        for (TimeSlotDefault apmtDefault : timeSlotDefaultRepository.findAllByDoctor(doctor)) {
            if (updateOnes.stream().noneMatch(ad -> ad.equals(apmtDefault))) {
                deleteOnes.add(apmtDefault);
            }
        }

        doctor.setTimeSlotsDefault(updateOnes);
        timeSlotDefaultRepository.deleteAll(deleteOnes);
        timeSlotDefaultRepository.saveAll(updateOnes);

        results.put("timeSlots", doctor.getTimeSlotsDefault());

        return AjaxResponse.responseSuccess(results,"success");
    }

}
