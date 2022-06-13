package vn.aptech.doccure.controller.ajax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("ajax/timeSlots")
@Secured("ROLE_DOCTOR")
public class TimeSlotController {

    private Logger logger = LoggerFactory.getLogger(TimeSlotController.class);

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

        List<List<TimeSlotDefault>> slotsGroupedByWeekday = Arrays.asList(
                new ArrayList<>(), //mon
                new ArrayList<>(), //tue
                new ArrayList<>(), //wed
                new ArrayList<>(), //thur
                new ArrayList<>(), //fri
                new ArrayList<>(), //sat
                new ArrayList<>() //sun
        );

        for (TimeSlotDefault apmtDefault : postedTimeSlots) {
            if (apmtDefault.isTimeRangeValid()) {
                apmtDefault.setDoctor(doctor);
                apmtDefault.setDoctorId(doctor.getId());
                slotsGroupedByWeekday.get(apmtDefault.getWeekday()).add(apmtDefault);
            }
        }

        // check for overlap time range
        for (List<TimeSlotDefault> slotsWeekday : slotsGroupedByWeekday) {
            for (TimeSlotDefault aSlot : slotsWeekday) {
                boolean isOverlapped = false;

                comparingloop:
                for (TimeSlotDefault bSlot : slotsWeekday) {

                    if (aSlot.equals(bSlot)) {
                        continue;
                    }

                    LocalTime s = aSlot.getTimeStart();
                    LocalTime e = aSlot.getTimeEnd();
                    LocalTime a = bSlot.getTimeStart();
                    LocalTime b = bSlot.getTimeEnd();

                    if (
                            (s.compareTo(a) <= 0 && e.compareTo(a) >= 0)
                            || (s.compareTo(a) >= 0 && e.compareTo(b) <= 0)
                    ) {
                        isOverlapped = true;
                        break comparingloop;
                    }
                }

                if (!isOverlapped) {
                    updateOnes.add(aSlot);
                }
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
