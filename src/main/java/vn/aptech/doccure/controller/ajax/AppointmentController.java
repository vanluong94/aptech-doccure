package vn.aptech.doccure.controller.ajax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.aptech.doccure.common.AjaxResponse;
import vn.aptech.doccure.entities.Appointment;
import vn.aptech.doccure.entities.AppointmentLog;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.repository.AppointmentRepository;
import vn.aptech.doccure.repository.TimeSlotRepository;
import vn.aptech.doccure.service.TimeSlotService;
import vn.aptech.doccure.utils.DateUtils;
import vn.aptech.doccure.utils.DoctorUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("ajax/appointments")
public class AppointmentController {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    TimeSlotService timeSlotService;

    @Autowired
    TimeSlotRepository timeSlotRepository;

    @GetMapping("/getByDoctor")
    @ResponseBody
    @Secured("ROLE_PATIENT")
    public ResponseEntity<Object> getByDoctor(@RequestParam Long doctorId, @RequestParam Integer offset, @RequestParam Integer length, Authentication authentication) {

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
            weekdayData.put("slots", timeSlotRepository.findAllByDoctorOnDate(doctorId, theDate));

            weekdays.add(weekdayData);
        }

        respData.put("results", weekdays);

        return AjaxResponse.responseSuccess(respData, "success");
    }

    @GetMapping("/mine")
    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    @ResponseBody
    public ResponseEntity<Object> getByPatient(@RequestParam Integer page, @RequestParam Integer length, Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        Map<String, Object> response = new HashMap<>();

        Pageable pageable = PageRequest.of(page, length);
        Page<Appointment> results;

        if (user.isDoctor()) {
            results = appointmentRepository.findAllByDoctorOrderByCreatedDateDesc(user, pageable);
        } else if (user.isPatient()) {
            results = appointmentRepository.findAllByPatientOrderByCreatedDateDesc(user, pageable);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        List<Object> rows = new LinkedList<>();
        for (Appointment apmt : results.getContent()) {
            Map<String, Object> row = new LinkedHashMap<>();
            Map<String, Object> doctor = new HashMap<>();
            Map<String, Object> patient = new HashMap<>();

            doctor.put("avatar", apmt.getDoctor().getTheAvatar());
            doctor.put("url", DoctorUtils.getDoctorProfileUrl(apmt.getDoctor()));
            doctor.put("title", apmt.getDoctor().getDoctorTitle());
            doctor.put("subtitle", "Dental");

            patient.put("avatar", apmt.getPatient().getTheAvatar());
            patient.put("url", "#");
            patient.put("title", apmt.getPatient().getFullName());
            patient.put("subtitle", "#" + apmt.getPatient().getId());

//            row.put("doctor", AppointmentUtils.getDoctorItemOutput(apmt.getDoctor()));
//            row.put("apmtDate", DateUtils.toStandardDate(apmt.getTimeSlot().getTimeStart()));
//            row.put("timeStart", "<span class=\"text-info\">" + DateUtils.toStandardTime(apmt.getTimeSlot().getTimeStart()) + "</span>");
//            row.put("timeEnd", "<span class=\"text-info\">" + DateUtils.toStandardTime(apmt.getTimeSlot().getTimeEnd()) + "</span>");
//            row.put("bookingDate", DateUtils.toStandardDate(apmt.getCreatedDate()));
//            row.put("status", AppointmentUtils.getStatusBadgeOutput(apmt));
//            row.put("action", AppointmentUtils.getPatientAppointmentActionsOutput(apmt));

            row.put("id", apmt.getId());
            row.put("doctor", doctor);
            row.put("patient", patient);
            row.put("apmtDate", DateUtils.toStandardDate(apmt.getTimeSlot().getTimeStart()));
            row.put("timeStart", DateUtils.toStandardTime(apmt.getTimeSlot().getTimeStart()));
            row.put("timeEnd", DateUtils.toStandardTime(apmt.getTimeSlot().getTimeEnd()));
            row.put("bookingDate", DateUtils.toStandardDate(apmt.getCreatedDate()));
            row.put("status", apmt.getStatus());
            row.put("action", "");

            rows.add(row);
        }

//        response.put("draw", results.isEmpty() ? 0 : 1);
        response.put("data", rows);
        response.put("recordsTotal", results.getTotalElements());
        response.put("recordsFiltered", results.getTotalElements());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}/get")
    @ResponseBody
    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    public ResponseEntity<Object> getById(@PathVariable Long id, Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        if (authentication == null) {
            return AjaxResponse.responseFail(response, "login required");
        }

        Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);

        if (!appointmentOptional.isPresent()) {
            return AjaxResponse.responseFail(response, "appointment not found");
        }

        User user = (User) authentication.getPrincipal();
        Appointment appointment = appointmentOptional.get();

        if (!appointment.getDoctor().equals(user) && !appointment.getPatient().equals(user) ) {
            return AjaxResponse.responseFail(response, "you are not allowed to perform action on this appointment");
        }

        response.put("appointment", appointment);

        return AjaxResponse.responseSuccess(response, "success");
    }

    @PostMapping("/{id}/cancel")
    @ResponseBody
    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    public ResponseEntity<Object> cancel(@PathVariable Long id, Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        if (authentication == null) {
            return AjaxResponse.responseFail(response, "login required");
        }

        Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);

        if (!appointmentOptional.isPresent()) {
            return AjaxResponse.responseFail(response, "appointment not found");
        }

        User user = (User) authentication.getPrincipal();
        Appointment appointment = appointmentOptional.get();

        AppointmentLog log = new AppointmentLog();
        log.setAppointment(appointment);
        log.setMadeBy(user);

        if (appointment.getDoctor().equals(user)) {
            log.setContent(String.format("Appointment was [canceled] by [%s]", appointment.getDoctor().getDoctorTitle()));
        } else if (appointment.getPatient().equals(user)) {
            log.setContent(String.format("Appointment was [canceled] by [%s]", appointment.getPatient().getFullName()));
        } else if (appointment.getTimeSlot().isPast()){
            return AjaxResponse.responseFail(response, "This appointment cannot be canceled due to overdue");
        } else {
            return AjaxResponse.responseFail(response, "you are not allowed to perform action on this appointment");
        }

        appointment.getTimeSlot().setAppointment(null);
        appointment.setStatus(Appointment.STATUS.CANCELED);
        appointment.getLogs().add(log);

        appointmentRepository.saveAndFlush(appointment);
        timeSlotRepository.saveAndFlush(appointment.getTimeSlot());

        return AjaxResponse.responseSuccess(response, "success");

    }

    @PostMapping("/{id}/confirm")
    @ResponseBody
    @Secured("ROLE_DOCTOR")
    public ResponseEntity<Object> confirm(@PathVariable Long id, Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        if (authentication == null) {
            return AjaxResponse.responseFail(response, "login required");
        }

        Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);

        if (!appointmentOptional.isPresent()) {
            return AjaxResponse.responseFail(response, "appointment not found");
        }

        User doctor = (User) authentication.getPrincipal();
        Appointment appointment = appointmentOptional.get();

        if (!appointment.getDoctor().equals(doctor)) {
            return AjaxResponse.responseFail(response, "you are not allowed to perform action on this appointment");
        }

        AppointmentLog log = new AppointmentLog(
                appointment,
                String.format("[%s] has confirmed the appointment", doctor.getDoctorTitle()),
                doctor
        );

        appointment.setStatus(Appointment.STATUS.CONFIRMED);
        appointment.getLogs().add(log);

        appointmentRepository.saveAndFlush(appointment);

        return AjaxResponse.responseSuccess(response, "success");

    }

}
