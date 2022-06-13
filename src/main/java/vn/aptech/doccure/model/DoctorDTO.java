package vn.aptech.doccure.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import vn.aptech.doccure.SpringContext;
import vn.aptech.doccure.entities.Service;
import vn.aptech.doccure.entities.TimeSlot;
import vn.aptech.doccure.entities.User;
import vn.aptech.doccure.service.AppointmentService;
import vn.aptech.doccure.service.ServiceService;
import vn.aptech.doccure.service.TimeSlotService;
import vn.aptech.doccure.utils.DoctorUtils;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class DoctorDTO {

    @JsonIgnore
    private User user;

    public DoctorDTO(User doctor) {
        this.user = doctor;
    }

    public static DoctorDTO from(User doctor) {
        return new DoctorDTO(doctor);
    }

    public Long getId() {
        return this.user.getId();
    }

    public String getTitle() {
        return "Dr. " + this.user.getFullName();
    }

    public String getCity() {
        return this.user.getClinic().getCity() + ", " + this.user.getClinic().getCountry();
    }

    public String getUpcomingAvailableDate() {
        TimeSlot timeSlot = SpringContext.getBean(TimeSlotService.class).findUpcomingAvailable(this.user);
        if (timeSlot != null) {
            return timeSlot.getTimeStart().format(DateTimeFormatter.ofPattern("eee, dd MMM"));
        }
        return null;
    }

    public double getAvgRating() {
        return this.user.getAvgReview();
    }

    public Integer getTotalReviews() {
        return this.user.getDoctorReviews().size();
    }

    public String getAvatar() {
        return this.user.getTheAvatar();
    }

    public String getSpecialtiesText() {
        List<Service> services = SpringContext.getBean(ServiceService.class).findByDoctor(this.user);
        return StringUtils.join(
                services.stream().map((Service service) -> service.getName()).collect(Collectors.toList()),
                ", "
        );
    }

    public String getUrl() {
        return DoctorUtils.getDoctorProfileUrl(this.user);
    }

    public String getBookingUrl() {
        return DoctorUtils.getDoctorBookingUrl(this.user);
    }

    @JsonIgnore
    public Long getTotalAppointments() {
        return SpringContext.getBean(AppointmentService.class).countByDoctor(this.user);
    }

    @JsonIgnore
    public Long getTodayTotalAppointments() {
        return SpringContext.getBean(AppointmentService.class).countTodayByDoctor(this.user);
    }

    @JsonIgnore
    public Long getTotalPatients() {
        return SpringContext.getBean(AppointmentService.class).countPatientByDoctor(this.user);
    }

}
