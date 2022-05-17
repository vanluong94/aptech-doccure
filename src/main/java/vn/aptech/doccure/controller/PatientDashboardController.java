package vn.aptech.doccure.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("dashboard")
public class PatientDashboardController {

    @GetMapping("/patient-appointments")
    public String appointmentsPage() {
        return "pages/dashboard/patientAppointments";
    }
}
