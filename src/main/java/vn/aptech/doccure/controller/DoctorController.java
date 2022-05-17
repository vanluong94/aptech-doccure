package vn.aptech.doccure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.doccure.common.Constants;
import vn.aptech.doccure.entities.Appointment;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.repository.AppointmentRepository;
import vn.aptech.doccure.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Optional;

@Controller
@RequestMapping("doctor")
public class DoctorController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @GetMapping("/profile/{id}")
    public ModelAndView profile(@PathVariable("id") Long id) {
        ModelAndView modelAndView;
        Optional<User> user = userService.findById(id);
        if (user.isPresent() && user.get().hasRole(Constants.Roles.ROLE_DOCTOR)) {
            modelAndView = new ModelAndView("/pages/doctor/doctor-profile");
            user.get().getClinic().parseImages();
            modelAndView.addObject("doctor", user.get());
        } else {
            modelAndView = new ModelAndView("/pages/404");
        }
        return modelAndView;
    }

    @Secured("ROLE_PATIENT")
    @GetMapping("/profile/{id}/booking")
    public ModelAndView bookingView(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("pages/doctor/doctor-booking");

        Optional<User> user = userService.findById(id);

        if (!user.isPresent() || !user.get().hasRole(Constants.Roles.ROLE_DOCTOR)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        List<Object> weekdays = new LinkedList<>();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter weekdayFormatter = DateTimeFormatter.ofPattern("eee");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

        for (int i=0; i<8; i++) {
            LocalDateTime theDate = now.plusDays(i);
            Map<String, Object> weekdayData = new HashMap<>();

            weekdayData.put("textWeekday", theDate.format(weekdayFormatter));
            weekdayData.put("textDate", theDate.format(dateFormatter));
            weekdayData.put("slots", appointmentRepository.findAllByDoctorOnDate(user.get().getId(), theDate));

            weekdays.add(weekdayData);
        }

        modelAndView.addObject("now", now);
        modelAndView.addObject("doctor", user.get());
        modelAndView.addObject("weekdays", weekdays);

        return modelAndView;
    }

    @Secured("ROLE_PATIENT")
    @PostMapping("profile/{id}/booking")
    public String booking(Authentication authentication, @PathVariable("id") Long doctorId, @RequestParam Long appointmentId, RedirectAttributes redirect, Model model) {

        Optional<User> doctorResult = userService.findById(doctorId);

        if (!doctorResult.isPresent() || !doctorResult.get().hasRole(Constants.Roles.ROLE_DOCTOR)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Optional<Appointment> appointmentResult = appointmentRepository.findById(appointmentId);
        if (!appointmentResult.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        User patient = (User) authentication.getPrincipal();
        Appointment appointment = appointmentResult.get();

        if (appointment.isBooked()) {
            redirect.addFlashAttribute("errorMessage", "This appointment is already booked, please book another.");
            model.addAttribute("id", appointment.getDoctor().getId());
            return "redirect:/doctor/profile/{id}/booking";
        } else if (appointment.isPast()) {
            redirect.addFlashAttribute("errorMessage", "This appointment is not available to be booked anymore, please book another.");
            model.addAttribute("id", appointment.getDoctor().getId());
            return "redirect:/doctor/profile/{id}/booking";
        }

        appointment.setPatient(patient);
        appointment.setStatus(Appointment.STATUS.PENDING);
        appointment.setBookedDate(LocalDateTime.now());

        appointmentRepository.saveAndFlush(appointment);

        model.addAttribute("appointment", appointment);
        return "pages/doctor/doctor-booking-success";
    }

}
