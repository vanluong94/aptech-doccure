package vn.aptech.doccure.controller.ajax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vn.aptech.doccure.common.AjaxResponse;
import vn.aptech.doccure.common.Constants;
import vn.aptech.doccure.entities.Appointment;
import vn.aptech.doccure.entities.AppointmentLog;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.model.CalendarAppointmentDTO;
import vn.aptech.doccure.service.AppointmentService;
import vn.aptech.doccure.service.TimeSlotService;
import vn.aptech.doccure.service.UserService;
import vn.aptech.doccure.utils.AppointmentUtils;
import vn.aptech.doccure.utils.DateUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("ajax/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private TimeSlotService timeSlotService;

    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Object> getAll(@RequestParam Integer page, @RequestParam Integer length) {
        Map<String, Object> response = new HashMap<>();

        Pageable pageable = PageRequest.of(page, length, Sort.by("createdDate").descending());
        Page<Appointment> results = appointmentService.findAll(pageable);

        response.put("data", AppointmentUtils.toDataTable(results.getContent()));
        response.put("recordsTotal", results.getTotalElements());
        response.put("recordsFiltered", results.getTotalElements());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

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
            weekdayData.put("slots", timeSlotService.findAllByDoctorOnDate(doctorId, theDate));

            weekdays.add(weekdayData);
        }

        respData.put("results", weekdays);

        return AjaxResponse.responseSuccess(respData, "success");
    }

    @GetMapping("/myCalendar")
    @ResponseBody
    @Secured("ROLE_DOCTOR")
    public ResponseEntity<Object> getMyCalendar(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start, @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        LocalDateTime startingDate = DateUtils.convertDateToLocalDateTime(start);
        LocalDateTime endingDate = DateUtils.convertDateToLocalDateTime(end);

        List<Appointment> appointments = appointmentService.findAvailableByDoctorTimeRange(user, startingDate, endingDate);
        List<Object> events = new LinkedList<>();
        for (Appointment appointment : appointments) {
            events.add(CalendarAppointmentDTO.from(appointment));
        }

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/mine")
    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    @ResponseBody
    public ResponseEntity<Object> getMine(@RequestParam Integer page, @RequestParam Integer length, Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        Map<String, Object> response = new HashMap<>();

        Pageable pageable = PageRequest.of(page, length);
        Page<Appointment> results;

        if (user.isDoctor()) {
            results = appointmentService.findAllByDoctor(user, pageable);
        } else if (user.isPatient()) {
            results = appointmentService.findAllByPatient(user, pageable);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        response.put("data", AppointmentUtils.toDataTable(results.getContent()));
        response.put("recordsTotal", results.getTotalElements());
        response.put("recordsFiltered", results.getTotalElements());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/mine/upcoming")
    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    @ResponseBody
    public ResponseEntity<Object> getMineUpcoming(@RequestParam Integer page, @RequestParam Integer length, Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        Map<String, Object> response = new HashMap<>();

        Pageable pageable = PageRequest.of(page, length);
        Page<Appointment> results;

        if (user.isDoctor()) {
            results = appointmentService.findUpcomingByDoctor(user, pageable);
        } else if (user.isPatient()) {
            results = appointmentService.findUpcomingByPatient(user, pageable);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        response.put("data", AppointmentUtils.toDataTable(results.getContent()));
        response.put("recordsTotal", results.getTotalElements());
        response.put("recordsFiltered", results.getTotalElements());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/mine/today")
    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT"})
    @ResponseBody
    public ResponseEntity<Object> getMineToday(@RequestParam Integer page, @RequestParam Integer length, Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        Map<String, Object> response = new HashMap<>();

        Pageable pageable = PageRequest.of(page, length);
        Page<Appointment> results;

        if (user.isDoctor()) {
            results = appointmentService.findTodayByDoctor(user, pageable);
        } else if (user.isPatient()) {
            results = appointmentService.findTodayByPatient(user, pageable);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        response.put("data", AppointmentUtils.toDataTable(results.getContent()));
        response.put("recordsTotal", results.getTotalElements());
        response.put("recordsFiltered", results.getTotalElements());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/mine/byPatient/{id}")
    @Secured("ROLE_DOCTOR")
    @ResponseBody
    public ResponseEntity<Object> getMineByPatient(@PathVariable("id") Long id, @RequestParam Integer page, @RequestParam Integer length, Authentication authentication) {

        User doctor = (User) authentication.getPrincipal();

        Map<String, Object> response = new HashMap<>();

        if (doctor.isDoctor()) {
            Optional<User> userResult = userService.findById(id);
            if (userResult.isPresent()) {
                Pageable pageable = PageRequest.of(page, length, Sort.by("createdDate").descending());
                Page<Appointment> results = appointmentService.findByDoctorAndPatient(doctor, userResult.get(), pageable);

                response.put("data", AppointmentUtils.toDataTable(results.getContent()));
                response.put("recordsTotal", results.getTotalElements());
                response.put("recordsFiltered", results.getTotalElements());
            }
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}/get")
    @ResponseBody
    @Secured({"ROLE_DOCTOR", "ROLE_PATIENT", "ROLE_ADMIN"})
    public ResponseEntity<Object> getById(@PathVariable Long id, Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        if (authentication == null) {
            return AjaxResponse.responseFail(response, "login required");
        }

        Optional<Appointment> appointmentOptional = appointmentService.findById(id);

        if (!appointmentOptional.isPresent()) {
            return AjaxResponse.responseFail(response, "appointment not found");
        }

        User user = (User) authentication.getPrincipal();
        Appointment appointment = appointmentOptional.get();

        if (!user.hasRole(Constants.Roles.ROLE_ADMIN) && !appointment.getDoctor().equals(user) && !appointment.getPatient().equals(user) ) {
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

        Optional<Appointment> appointmentOptional = appointmentService.findById(id);

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

        appointmentService.saveAndFlush(appointment);
        timeSlotService.saveAndFlush(appointment.getTimeSlot());

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

        Optional<Appointment> appointmentOptional = appointmentService.findById(id);

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

        appointmentService.saveAndFlush(appointment);

        return AjaxResponse.responseSuccess(response, "success");

    }

}
