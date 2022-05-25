package vn.aptech.doccure.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import vn.aptech.doccure.model.DoctorDTO;
import vn.aptech.doccure.model.PatientDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "appointments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Appointment implements Serializable {

    public interface STATUS {
        short PENDING = 0;
        short CONFIRMED = 1;
        short COMPLETED = 2;
        short CANCELED = 3;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "doctor_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "appointment_doctor_fk"
            )
    )
    @JsonIgnore
    private User doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "patient_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "appointment_patient_fk"
            )
    )
    @JsonIgnore
    private User patient;

//    @OneToOne(mappedBy = "appointment", fetch = FetchType.EAGER)
//    private TimeSlot timeSlot;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "time_slot_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "appointment_time_slot_fk"
            )
    )
    private TimeSlot timeSlot;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("created_date DESC")
    private Set<AppointmentLog> logs = new LinkedHashSet<>();

    @Column(name = "status", nullable = false)
    private Short status = STATUS.PENDING;

    @Column(name = "confirmed_date", columnDefinition = "datetime default current_timestamp")
    @LastModifiedDate
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMM yyyy h:m a")
    private LocalDateTime confirmedDate;

    @Column(name = "created_date", columnDefinition = "datetime default current_timestamp")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMM yyyy h:m a")
    @CreatedDate
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "modified_date", columnDefinition = "datetime default current_timestamp")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMM yyyy h:m a")
    @LastModifiedDate
    private LocalDateTime modifiedDate = LocalDateTime.now();

    @Transient
    @JsonIgnore
    private DoctorDTO doctorDTO;

    @Transient
    @JsonIgnore
    private PatientDTO patientDTO;

    public Appointment(User doctor, User patient, TimeSlot originalTimeSlot, Short status) {
        this.doctor = doctor;
        this.patient = patient;
        this.timeSlot = originalTimeSlot;
        this.status = status;
    }

    public String getStatusText() {
        switch (this.status) {
            case STATUS.PENDING:
                return "Pending";
            case STATUS.CANCELED:
                return "Cancelled";
            case STATUS.CONFIRMED:
                return "Confirm";
            case STATUS.COMPLETED:
                return "Completed";
            default:
                return "";
        }
    }

    public DoctorDTO getDoctorDTO() {
        if (doctorDTO == null) {
            doctorDTO = DoctorDTO.from(this.doctor);
        }
        return doctorDTO;
    }

    public PatientDTO getPatientDTO() {
        if (patientDTO == null) {
            patientDTO = PatientDTO.from(this.patient);
        }
        return patientDTO;
    }
}