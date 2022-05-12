package vn.aptech.doccure.controller.ajax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import vn.aptech.doccure.common.AjaxResponse;
import vn.aptech.doccure.entities.AppointmentDefault;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.repository.UserRepository;

import java.util.*;

@RestController
@RequestMapping("ajax/timeSlots")
@Secured("ROLE_DOCTOR")
public class TimeSlotController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("getAll")
    @ResponseBody
    @Transactional
    public ResponseEntity<Object> getAll(Authentication auth) {
        User doctor = (User) auth.getPrincipal();
        Map<String, Object> respData = new HashMap<>();
        respData.put("timeSlots", doctor.getAppointmentsDefault());
        return AjaxResponse.responseSuccess(respData, "success");
    }

    @PostMapping("saveAll")
    @ResponseBody
    public ResponseEntity<Object> updateAll(Authentication auth, @RequestBody(required = false) List<AppointmentDefault> postedTimeSlots) {

        Map<String, Object> results = new LinkedHashMap<>();

        User doctor = (User) auth.getPrincipal();

        Set<AppointmentDefault> updateTimeSlots = new TreeSet<>(new Comparator<AppointmentDefault>() {
            @Override
            public int compare(AppointmentDefault o1, AppointmentDefault o2) {
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

        for (AppointmentDefault apmtDefault : postedTimeSlots) {
            if (apmtDefault.isTimeRangeValid()) {
                apmtDefault.setDoctor(doctor);
                apmtDefault.setDoctorId(doctor.getId());
                updateTimeSlots.add(apmtDefault);
            }
        }

        doctor.setAppointmentsDefault(updateTimeSlots);
        userRepository.saveAndFlush(doctor);

        results.put("timeSlots", doctor.getAppointmentsDefault());

        return AjaxResponse.responseSuccess(results,"success");
    }

}
