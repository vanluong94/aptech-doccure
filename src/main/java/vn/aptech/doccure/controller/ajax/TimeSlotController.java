package vn.aptech.doccure.controller.ajax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.aptech.doccure.common.AjaxResponse;
import vn.aptech.doccure.entities.TimeSlot;
import vn.aptech.doccure.entities.TimeSlotDefault;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.repositories.UserRepository;
import vn.aptech.doccure.service.TimeSlotDefaultService;
import vn.aptech.doccure.service.TimeSlotService;

import java.time.LocalDateTime;
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
    TimeSlotDefaultService timeSlotDefaultService;

    @Autowired
    TimeSlotService timeSlotService;

    @GetMapping("getAll")
    @ResponseBody
    public ResponseEntity<Object> getAll(Authentication auth) {
        User doctor = (User) auth.getPrincipal();
        Map<String, Object> respData = new HashMap<>();
        respData.put("timeSlots", timeSlotDefaultService.findAllByDoctor(doctor));
        return AjaxResponse.responseSuccess(respData, "success");
    }

    @PostMapping("saveAll")
    @ResponseBody
    public ResponseEntity<Object> updateAll(Authentication auth, @RequestBody(required = false) Map<String, List<TimeSlotDefault>> postedData) {

        Map<String, Object> results = new LinkedHashMap<>();

        List<TimeSlotDefault> postedOnes = postedData.get("update");
        List<TimeSlotDefault> deleteOnes = postedData.get("delete");

        User doctor = (User) auth.getPrincipal();

        Set<TimeSlotDefault> updateOnes = new TreeSet<>(new Comparator<TimeSlotDefault>() {
            @Override
            public int compare(TimeSlotDefault o1, TimeSlotDefault o2) {
                if (o1.getWeekday().equals(o2.getWeekday())) {

                    if (o1.getTimeStart().isBefore(o2.getTimeStart())) {
                        return -1;
                    } else {
                        return 1;
                    }

                } else if (o1.getWeekday() < o2.getWeekday()) {
                    return -1;
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

        for (TimeSlotDefault apmtDefault : postedOnes) {
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
                            (
                                    (s.compareTo(a) <= 0 && e.compareTo(a) > 0)
                                    || (s.compareTo(a) >= 0 && e.compareTo(b) <= 0)
                            )
                            && updateOnes.stream().anyMatch(slot -> slot.equals(bSlot))
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

        System.out.println(updateOnes.size());

        // handle delete ones
        if (deleteOnes.size() > 0) {
            deleteOnes.forEach(apmtDefault -> {
                apmtDefault.setDoctor(doctor);
                apmtDefault.setDoctorId(doctor.getId());
            });
            timeSlotDefaultService.deleteAll(deleteOnes);
        }

        doctor.setTimeSlotsDefault(updateOnes);
        timeSlotDefaultService.saveAll(updateOnes);

        LocalDateTime now = LocalDateTime.now();
        for (int i=0; i<14; i++) {
            LocalDateTime theDate = now.plusDays(i);
            List<TimeSlot> timeSlots = timeSlotService.findAllByDoctorOnDate(doctor, theDate);
            for (TimeSlot timeSlot : timeSlots) {
                if (!timeSlot.exists()) {
                    timeSlotService.saveAndFlush(timeSlot);
                } else if (!timeSlot.isOn()) {
                    if (!timeSlot.isBooked()) {
                        timeSlotService.delete(timeSlot);
                    } else {
                        // this should be handled
                    }
                }
            }
        }

        results.put("timeSlots", doctor.getTimeSlotsDefault());

        return AjaxResponse.responseSuccess(results,"success");
    }

}
