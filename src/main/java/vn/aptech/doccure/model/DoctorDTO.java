package vn.aptech.doccure.model;

import lombok.Data;
import vn.aptech.doccure.SpringContext;
import vn.aptech.doccure.entities.TimeSlot;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.service.AppointmentService;
import vn.aptech.doccure.service.TimeSlotService;

import java.time.format.DateTimeFormatter;

@Data
public class DoctorDTO {

    private User doctor;

    public DoctorDTO(User doctor) {
        this.doctor = doctor;
    }

    public static DoctorDTO from(User doctor) {
        return new DoctorDTO(doctor);
    }

    public Long getId() {
        return this.doctor.getId();
    }

    public String getTitle() {
        return "Dr. " + this.doctor.getFullName();
    }

    public String getCity() {
        return this.doctor.getClinic().getCity() + ", " + this.doctor.getClinic().getCountry();
    }

    public String getUpcomingAvailableDate() {
        TimeSlot timeSlot = SpringContext.getBean(TimeSlotService.class).findUpcomingAvailable(this.doctor);
        if (timeSlot != null) {
            return timeSlot.getTimeStart().format(DateTimeFormatter.ofPattern("eee, dd MMM"));
        }
        return null;
    }

    public String getEduSpecialtiesText() {
//        StringBuilder builder = new StringBuilder();
//        DoctorBio bio = doctor.getBio();
//        return builder.toString();
        return "MDS - " + getSpecialtiesText(); // dummy data
    }

    public String getSpecialtiesText() {
//        List<String> services = new LinkedList<>();
//        for (Service service : doctor.getServices()) {
//            services.add(service.getName());
//        }
//        return StringUtils.join(services, ", ");
        return "Periodontology and Oral Implantology, BDS"; // dummy data
    }

    public Long getTotalAppointments() {
        return SpringContext.getBean(AppointmentService.class).countByDoctor(this.doctor);
    }

    public Long getTodayTotalAppointments() {
        return SpringContext.getBean(AppointmentService.class).countTodayByDoctor(this.doctor);
    }

    public Long getTotalPatients() {
        return SpringContext.getBean(AppointmentService.class).countPatientByDoctor(this.doctor);
    }

}
