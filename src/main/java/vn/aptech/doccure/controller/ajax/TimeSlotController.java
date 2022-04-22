package vn.aptech.doccure.controller.ajax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.aptech.doccure.common.AjaxResponse;
import vn.aptech.doccure.entities.AppointmentDefault;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.repository.AppointmentDefaultRepository;
import vn.aptech.doccure.service.UserService;

import java.util.*;

@RestController
@RequestMapping("ajax/timeSlots")
@Secured("ROLE_DOCTOR")
public class TimeSlotController {

    @Autowired
    UserService userService;

    @Autowired
    AppointmentDefaultRepository apmtDefaultRepository;

    @GetMapping("getAll")
    @ResponseBody
    public ResponseEntity<Object> getWeekdayTimeSlots(Authentication auth) {
        User doctor = (User) auth.getPrincipal();

        HashMap<String, Set<AppointmentDefault>> respData = new HashMap<>();
        respData.put("timeSlots", doctor.getDoctorAppointmentsDefault());
        return AjaxResponse.responseSuccess(respData, "success");
    }

    @PostMapping("save")
    @ResponseBody
    public ResponseEntity<Object> updateWeekdayTimeSlots(Authentication auth, @RequestParam int weekday, @RequestBody(required = false) List<AppointmentDefault> timeSlots) {

        Map<String, Object> results = new LinkedHashMap<>();

        User doctor = (User) auth.getPrincipal();
        Set<AppointmentDefault> doctorTimeSlots = new TreeSet<>(new Comparator<AppointmentDefault>() {
            @Override
            public int compare(AppointmentDefault o1, AppointmentDefault o2) {
                if (o1.getTimeStart().isBefore(o2.getTimeStart())) {
                    return -1;
                } else if (o1.getTimeStart().equals(o2.getTimeEnd())) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        apmtDefaultRepository.deleteDoctorWeekdayTimeSlots(doctor.getId(), weekday);

        if (timeSlots != null) {
            for (AppointmentDefault apmt : timeSlots) {
                if (apmt.isTimeRangeValid()) {
                    apmt.setDoctor(doctor);
                    apmt.setDoctorId(doctor.getId());
                    apmt.setWeekday(weekday);
                    doctorTimeSlots.add(apmt);
                }
            }
        }

        if (doctorTimeSlots.size() > 0) {
            doctor.setDoctorAppointmentsDefault(doctorTimeSlots);
            apmtDefaultRepository.saveAll(doctorTimeSlots);
        } else if (timeSlots != null) {
            return AjaxResponse.responseFail(results, "Failed to save the time slots, please check again");
        }

        results.put("timeSlots", doctor.getDoctorAppointmentsDefault());

        return AjaxResponse.responseSuccess(results,"success");
    }

}
