package vn.aptech.doccure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import vn.aptech.doccure.entities.DoctorClinic;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.repository.DoctorClinicRepository;
import vn.aptech.doccure.storage.StorageService;

import java.util.List;

@Controller
@RequestMapping("dashboard")
@Secured("ROLE_DOCTOR")
public class DoctorDashboardController {

    @Autowired
    DoctorClinicRepository doctorClinicRepository;

    @Autowired
    StorageService storageService;

    @GetMapping("time-slot-settings")
    public String timeSlotsPage() {
        return "pages/dashboard/doctorTimeSlots";
    }

    @GetMapping("clinic-settings")
    public ModelAndView clinicPage(Authentication auth) {
        ModelAndView modelAndView = new ModelAndView("pages/dashboard/doctorClinic");
        User doctor = (User) auth.getPrincipal();
        DoctorClinic clinic = doctor.getDoctorClinic();
        if (clinic == null) {
            clinic = new DoctorClinic();
//            clinic.setName("Smile Cute Dental Care Center");
//            clinic.setSpecialities("MDS - Periodontology and Oral Implantology, BDS");
//            clinic.setAddressLine1("2286 Sundown Lane");
//            clinic.setCity("Austin");
//            clinic.setState("Texas");
//            clinic.setCountry("USA");
//            clinic.setPostalCode(78749);
        }
        clinic.parseImages();
        modelAndView.addObject("clinic", clinic);
        return modelAndView;
    }

    @PostMapping("clinic-settings/save")
    public String clinicPageSave(@Validated @ModelAttribute DoctorClinic clinic, BindingResult result, Authentication auth) {
        if (!result.hasErrors()) {

            User doctor = (User) auth.getPrincipal();
            doctor.getDoctorClinic().parseImages();
            List<String> images = doctor.getDoctorClinic().getParsedImages();

                for (String img : clinic.getDeletedImages()) {
                    images.remove(img);
                }

                for (MultipartFile image : clinic.getPostedImages()) {
                    String filename = storageService.storeUnderRandomName(image, "clinic_" + doctor.getId());
                    images.add(filename);
                }

            clinic.setParsedImages(images);
            clinic.syncParsedImages();

            doctor.setDoctorClinic(clinic);
            clinic.setDoctor(doctor);
            clinic.setDoctorId(doctor.getId());
            doctorClinicRepository.save(clinic);
        }

        return "redirect:/dashboard/clinic-settings";
    }

}
