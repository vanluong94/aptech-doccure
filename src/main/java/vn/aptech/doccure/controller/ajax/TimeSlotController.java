package vn.aptech.doccure.controller.ajax;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.service.UserService;

@RestController
@RequestMapping("ajax/time_slots")
@PreAuthorize("hasRole('ROLE_DOCTOR')")
public class TimeSlotController {

    @Autowired
    UserService userService;

    @GetMapping("weekday/{weekday}")
    @ResponseBody
    @Transactional
    @javax.transaction.Transactional
    public String getWeekdayTimeSlots(@PathVariable Integer weekday) {
        User doctor = userService.getCurrentUser();
        if (doctor == null) {
            throw new UsernameNotFoundException(doctor.toString());
        }
//        AppointmentDefault apmtDefault = new AppointmentDefault();
//        apmtDefault.setWeekday();

//        return doctor.getEmail();
//        System.out.println(apmtDefaultRepo.findByDoctor(doctor));
        System.out.println(doctor.getReviews());
        return "ok";
    }
}
