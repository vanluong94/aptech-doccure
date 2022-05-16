package vn.aptech.doccure.controller.ajax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import vn.aptech.doccure.common.AjaxResponse;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.service.AppointmentService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("ajax/appointments")
public class AppointmentController {

    @Autowired
    AppointmentService appointmentService;

    @GetMapping("/getByDoctor")
    @ResponseBody
    @Transactional
    public ResponseEntity<Object> getByDoctor(@Param("doctorId") Long doctorId, @Param("offset") Integer offset, @Param("length") Integer length, Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        System.out.println("Check user role: " + user.hasRole("ROLE_PATIENT"));

        Map<String, Object> respData = new HashMap<>();

        List<Object> weekdays = new LinkedList<>();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter weekdayFormatter = DateTimeFormatter.ofPattern("eee");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

        for (int i=offset; i<offset+length; i++) {

            if (i > 14) { // do not get results for the date further than 2 weeks
                break;
            }

            LocalDateTime theDate = now.plusDays(i);
            Map<String, Object> weekdayData = new HashMap<>();

            weekdayData.put("textWeekday", theDate.format(weekdayFormatter));
            weekdayData.put("textDate", theDate.format(dateFormatter));
            weekdayData.put("slots", appointmentService.findAllByDoctorOnDate(doctorId, theDate));

            weekdays.add(weekdayData);
        }

        respData.put("results", weekdays);

        return AjaxResponse.responseSuccess(respData, "success");
    }

}
