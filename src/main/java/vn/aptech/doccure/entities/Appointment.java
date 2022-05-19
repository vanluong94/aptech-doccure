package vn.aptech.doccure.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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

    public static interface STATUS {
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

    @OneToOne(mappedBy = "appointment", fetch = FetchType.EAGER)
    private TimeSlot timeSlot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "time_slot_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "appointment_time_slot_fk"
            )
    )
    private TimeSlot originalTimeSlot;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<AppointmentLog> logs = new LinkedHashSet<>();

    @Column(name = "status", nullable = false)
    private Short status = STATUS.PENDING;

    @Column(name = "created_date", columnDefinition = "datetime default current_timestamp")
    @CreatedDate
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "modified_date", columnDefinition = "datetime default current_timestamp")
    @LastModifiedDate
    private LocalDateTime modifiedDate = LocalDateTime.now();

    public Appointment(User doctor, User patient, TimeSlot originalTimeSlot, Short status) {
        this.doctor = doctor;
        this.patient = patient;
        this.originalTimeSlot = originalTimeSlot;
        this.status = status;
    }
}