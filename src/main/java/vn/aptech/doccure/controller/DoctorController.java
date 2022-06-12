package vn.aptech.doccure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.doccure.common.Constants;
import vn.aptech.doccure.entities.*;
import vn.aptech.doccure.model.DoctorDTO;
import vn.aptech.doccure.model.ReviewRequestDTO;
import vn.aptech.doccure.service.*;
import vn.aptech.doccure.utils.DateUtils;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("doctor")
public class DoctorController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private TimeSlotService timeSlotService;

    @Autowired
    private PatientFavoriteService favoriteService;

    @Autowired
    private ReviewService reviewService;

    @ModelAttribute("now")
    public LocalDateTime nowModelAttribute() {
        return LocalDateTime.now();
    }

    @GetMapping("/profile/{id}")
    public ModelAndView profile(@PathVariable("id") Long id, Authentication authentication) {

        ModelAndView modelAndView;

        Optional<User> user = userService.findById(id);
        if (!user.isPresent() || !user.get().hasRole(Constants.Roles.ROLE_DOCTOR)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        modelAndView = new ModelAndView("pages/doctor/doctor-profile");
        modelAndView.addObject("doctor", user);
        Iterable<Review> reviews = reviewService.findAllByDoctorId(user.get().getId());
        modelAndView.addObject("reviews", reviews);

        if (authentication != null) {
            User currentUser = (User) authentication.getPrincipal();
            ReviewRequestDTO review = new ReviewRequestDTO();
            review.setDoctorId(id);
            review.setPatientId(currentUser.getId());
            modelAndView.addObject("review", review);
            modelAndView.addObject("isDoctorFavorite", favoriteService.isDoctorFavorited(user.get(), currentUser));
        } else {
            modelAndView.addObject("isDoctorFavorite", false);
        }
        modelAndView.addObject("clinicOpeningTimes", timeSlotService.getAllOpeningTimes(user.get()));

        return modelAndView;

    }

    @PostMapping("/profile/{id}")
    public String submitReview(@PathVariable("id") Long id, @ModelAttribute("review") ReviewRequestDTO requestDTO, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "redirect:/doctor/profile/" + id;
        }
        Review review = new Review();
        review.setRating(requestDTO.getRating());
        review.setTitle(requestDTO.getTitle());
        review.setContent(requestDTO.getContent());
        review.setDoctor(new User(requestDTO.getDoctorId()));
        review.setPatient(new User(requestDTO.getPatientId()));
        try {
            if (reviewService.save(review) != null) {
                redirect.addFlashAttribute(Constants.MESSAGE.SUCCESS, "Your comment was posted successfully! Thank you!");
            } else {
                redirect.addFlashAttribute(Constants.MESSAGE.ERROR, "Can't publish your rating and review right now. Try again?");
            }
        } catch (Exception e) {
            redirect.addFlashAttribute(Constants.MESSAGE.ERROR, e.getMessage());
        }
        return "redirect:/doctor/profile/" + id + "?tabActive=reviews#doc_reviews";
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

        for (int i=0; i<8; i++) {
            LocalDateTime theDate = now.plusDays(i);
            Map<String, Object> weekdayData = new HashMap<>();

            weekdayData.put("textWeekday", DateUtils.toShortenWeekday(theDate));
            weekdayData.put("textDate", DateUtils.toStandardDate(theDate));
            weekdayData.put("slots", timeSlotService.findAllByDoctorOnDate(user.get().getId(), theDate));

            weekdays.add(weekdayData);
        }

        modelAndView.addObject("nowDateText", DateUtils.toStandardDate(now));
        modelAndView.addObject("nowWeekdayText", DateUtils.toStandardWeekday(now));
        modelAndView.addObject("doctor", user.get());
        modelAndView.addObject("doctorDto", DoctorDTO.from(user.get()));
        modelAndView.addObject("weekdays", weekdays);

        return modelAndView;
    }

    @Secured("ROLE_PATIENT")
    @PostMapping("profile/{id}/booking")
    public String booking(Authentication authentication, @PathVariable("id") Long doctorId, @RequestParam Long timeSlotId, RedirectAttributes redirect, Model model) {

        Optional<User> doctorResult = userService.findById(doctorId);

        if (!doctorResult.isPresent() || !doctorResult.get().hasRole(Constants.Roles.ROLE_DOCTOR)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Optional<TimeSlot> timeSlotResult = timeSlotService.findById(timeSlotId);
        if (!timeSlotResult.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        User doctor = doctorResult.get();
        User patient = (User) authentication.getPrincipal();
        TimeSlot timeSlot = timeSlotResult.get();

        if (timeSlot.isBooked()) {
            redirect.addFlashAttribute(Constants.MESSAGE.ERROR, "This time slot is already booked, please book another.");
            model.addAttribute("id", timeSlot.getDoctor().getId());
            return "redirect:/doctor/profile/{id}/booking";
        } else if (timeSlot.isPast()) {
            redirect.addFlashAttribute(Constants.MESSAGE.ERROR, "This time slot is not available to be booked anymore, please book another.");
            model.addAttribute("id", timeSlot.getDoctor().getId());
            return "redirect:/doctor/profile/{id}/booking";
        }

        Appointment appointment = new Appointment(doctor, patient, timeSlot, Appointment.STATUS.PENDING);
        timeSlot.setAppointment(appointment);
        appointment.setTimeSlot(timeSlot);

        AppointmentLog log = new AppointmentLog(appointment, "The appointment has been made", patient);
        appointment.getLogs().add(log);

        timeSlotService.saveAndFlush(timeSlot);

        model.addAttribute("appointment", appointment);
        return "pages/doctor/doctor-booking-success";
    }

}
