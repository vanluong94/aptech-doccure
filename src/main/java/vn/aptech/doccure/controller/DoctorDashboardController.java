package vn.aptech.doccure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.doccure.entities.DoctorClinic;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.model.PatientDTO;
import vn.aptech.doccure.service.AppointmentService;
import vn.aptech.doccure.service.ClinicService;
import vn.aptech.doccure.service.UserService;
import vn.aptech.doccure.storage.StorageService;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/dashboard")
@Secured("ROLE_DOCTOR")
public class DoctorDashboardController {

    @Autowired
    ClinicService clinicService;

    @Autowired
    UserService userService;

    @Autowired
    StorageService storageService;

    @Autowired
    AppointmentService appointmentService;

    @GetMapping("/time-slot-settings")
    public String timeSlotsPage() {
        return "/pages/dashboard/doctorTimeSlots";
    }

    @GetMapping("/clinic-settings")
    public ModelAndView clinicPage(Authentication auth) {

        ModelAndView modelAndView = new ModelAndView("/pages/dashboard/doctorClinic");
        User user = (User) auth.getPrincipal();
        DoctorClinic clinic;

        if (user.getClinic() == null) {
            clinic = new DoctorClinic();
        } else {
            clinic = user.getClinic();
        }

        modelAndView.addObject("clinic", clinic);
        return modelAndView;
    }

    @PostMapping("/clinic-settings/save")
    public String clinicPageSave(@Validated @ModelAttribute DoctorClinic clinic, BindingResult result, Authentication auth, RedirectAttributes redirect) {
        if (!result.hasErrors()) {

            User doctor = (User) auth.getPrincipal();

            List<String> images;

            if (doctor.getClinic() != null) {
                images = doctor.getClinic().getImages();

                for (String img : clinic.getDeletedImages()) {
                    images.remove(img);
                }
            } else {
                images = new LinkedList<>();
            }

            for (MultipartFile image : clinic.getPostedImages()) {
                String filename = storageService.storeUnderRandomName(image, "clinic_" + doctor.getId());
                images.add(filename);
            }

            clinic.setImages(images);

            clinic.setDoctorId(doctor.getId());
            clinic.setDoctor(doctor);
            doctor.setClinic(clinic);
            clinicService.save(clinic);

            redirect.addFlashAttribute("successMessage", "Successfully updated Clinic profile.");
        }

        return "redirect:/dashboard/clinic-settings";
    }

    @GetMapping("/calendar")
    public String calendarPage() {
        return "pages/dashboard/doctorCalendar";
    }

    @GetMapping("/my-patients")
    public String myPatientsPage(@RequestParam(name = "page", defaultValue = "1") Integer page, Authentication authentication, Model model) {
        User doctor = (User) authentication.getPrincipal();
        Pageable pageable = PageRequest.of(--page, 10);

        List<PatientDTO> patients = new LinkedList<>();
        Page<User> results = appointmentService.findPatientsByDoctor(doctor, pageable);

        for (User patient : results.getContent()) {
            patients.add(PatientDTO.from(patient));
        }

        model.addAttribute("patients", patients);
        model.addAttribute("results", results);

        return "pages/dashboard/doctorMyPatients";
    }

    @GetMapping("/my-patients/patient/profile/{id}")
    public String patientProfilePage(@PathVariable("id") Long id, Authentication authentication, Model model) {

        Optional<User> userResult = userService.findById(id);

        if (!userResult.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("patient", PatientDTO.from(userResult.get()));

        return "pages/dashboard/patientProfile";
    }
}
