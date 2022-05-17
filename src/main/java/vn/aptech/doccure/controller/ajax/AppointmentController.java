package vn.aptech.doccure.controller.ajax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import vn.aptech.doccure.common.AjaxResponse;
import vn.aptech.doccure.entities.Appointment;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.repository.AppointmentRepository;
import vn.aptech.doccure.service.AppointmentService;
import vn.aptech.doccure.utils.AppointmentUtils;
import vn.aptech.doccure.utils.DateUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("ajax/appointments")
public class AppointmentController {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    AppointmentService appointmentService;

    @GetMapping("/getByDoctor")
    @ResponseBody
    @Transactional
    public ResponseEntity<Object> getByDoctor(@RequestParam Long doctorId, @RequestParam Integer offset, @RequestParam Integer length, Authentication authentication) {

        User user = (User) authentication.getPrincipal();

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

    @GetMapping("/mine")
    @ResponseBody
    @Transactional
    public ResponseEntity<Object> getByPatient(@RequestParam Integer page, @RequestParam Integer length, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Map<String, Object> response = new HashMap<>();

        Pageable pageable = PageRequest.of(page, length);
        Page<Appointment> results = appointmentRepository.findAllByPatient(user, pageable);

        List<Object> rows = new LinkedList<>();
        for (Appointment apmt : results.getContent()) {
            Map<String, Object> row = new LinkedHashMap<>();
            Map<String, Object> doctor = new HashMap<>();

            row.put("doctor", AppointmentUtils.getDoctorItemOutput(apmt.getDoctor()));
            row.put("apmtDate", DateUtils.toStandardDate(apmt.getTimeStart()));
            row.put("timeStart", "<span class=\"text-info\">" + DateUtils.toStandardTime(apmt.getTimeStart()) + "</span>");
            row.put("timeEnd", "<span class=\"text-info\">" + DateUtils.toStandardTime(apmt.getTimeEnd()) + "</span>");
            row.put("bookingDate", DateUtils.toStandardDate(apmt.getBookedDate()));
            row.put("status", AppointmentUtils.getStatusBadgeOutput(apmt));
            row.put("action", "");

            rows.add(row);
        }

        response.put("draw", results.isEmpty() ? 0 : 1);
        response.put("data", rows);
        response.put("recordsTotal", results.getTotalElements());
        response.put("recordsFiltered", results.getTotalElements());

        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

}
