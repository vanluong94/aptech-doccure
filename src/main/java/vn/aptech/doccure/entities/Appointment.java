package vn.aptech.doccure.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "appointments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SqlResultSetMapping(
        name = "AppointmentDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = Appointment.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "doctor_id", type = Long.class),
                                @ColumnResult(name = "patient_id", type = Long.class),
                                @ColumnResult(name = "time_start", type = LocalDateTime.class),
                                @ColumnResult(name = "time_end", type = LocalDateTime.class),
                                @ColumnResult(name = "status", type = Short.class),
                        }
                )
        }
)
public class Appointment implements Serializable {

    public static interface STATUS {
        Short QUEUE = -1;
        Short PENDING = 0;
        Short CONFIRMED = 1;
        Short COMPLETED = 2;
        Short CANCELED = 3;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    @JsonIgnore
    private User doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", columnDefinition = "BIGINT NULL")
    @JsonIgnore
    private User patient;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<AppointmentLog> logs = new LinkedHashSet<>();

    @Column(name = "time_start", nullable = false)
    private LocalDateTime timeStart;

    @Column(name = "time_end", nullable = false)
    private LocalDateTime timeEnd;

    @Column(name = "status", nullable = false)
    private Short status = STATUS.QUEUE;

    public Appointment (Long id, Long doctorId, Long patientId, LocalDateTime timeStart, LocalDateTime timeEnd, Short status) {
        this.id = id;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.status = status != null ? status : STATUS.QUEUE;

        this.doctor = new User();
        this.doctor.setId(doctorId);

        if (patientId != null && patientId > 0) {
            this.patient = new User();
            this.patient.setId(patientId);
        }
    }

    public String getTimeText() {
        return timeStart.format(DateTimeFormatter.ofPattern("h:m a"));
    }

    public boolean isPast() {
        return timeStart.isBefore(LocalDateTime.now());
    }

    public boolean exists() {
        return id != null && id > 0;
    }

    public boolean isBooked() {
        return patient != null;
    }

}