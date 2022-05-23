package vn.aptech.doccure.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import vn.aptech.doccure.entities.Appointment;

import java.time.LocalDateTime;

@Data
public class CalendarAppointmentDTO {

    private Long id;
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;
    private String className;

    @JsonIgnore
    private Appointment appointment;

    public static CalendarAppointmentDTO from(Appointment appointment) {
        return new CalendarAppointmentDTO(appointment);
    }

    public CalendarAppointmentDTO(Appointment appointment) {
        this.appointment = appointment;
        this.id = appointment.getId();
        this.title = appointment.getPatient().getFullName();
        this.start = appointment.getTimeSlot().getTimeStart();
        this.end = appointment.getTimeSlot().getTimeEnd();

        if (this.appointment.getStatus().equals(Appointment.STATUS.CONFIRMED)) {
            this.className = "bg-info";
        } else {
            this.className = "bg-secondary";
        }
    }

}
