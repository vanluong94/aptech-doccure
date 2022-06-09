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
import vn.aptech.doccure.model.ClinicOpeningTimes;
import vn.aptech.doccure.utils.DateUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "time_slots")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "TimeSlotDTOMapping",
                classes = {
                        @ConstructorResult(
                                targetClass = TimeSlot.class,
                                columns = {
                                        @ColumnResult(name = "id", type = Long.class),
                                        @ColumnResult(name = "appointment_id", type = Long.class),
                                        @ColumnResult(name = "doctor_id", type = Long.class),
                                        @ColumnResult(name = "time_start", type = LocalDateTime.class),
                                        @ColumnResult(name = "time_end", type = LocalDateTime.class),
                                }
                        )
                }
        ),
        @SqlResultSetMapping(
                name = "ClinicOpeningTimesMapping",
                classes = {
                        @ConstructorResult(
                                targetClass = ClinicOpeningTimes.class,
                                columns = {
                                        @ColumnResult(name = "opening", type = LocalDateTime.class),
                                        @ColumnResult(name = "closing", type = LocalDateTime.class)
                                }
                        )
                }
        )
})
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false, foreignKey = @ForeignKey(name = "time_slot_doctor_fk"))
    @JsonIgnore
    private User doctor;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "appointment_id", foreignKey = @ForeignKey(name = "time_slot_apmt_fk"))
    @JsonIgnore
    private Appointment appointment;

    @Column(name = "time_start", nullable = false, columnDefinition = "datetime not null")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "h:m a")
    private LocalDateTime timeStart;

    @Column(name = "time_end", nullable = false, columnDefinition = "datetime not null")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "h:m a")
    private LocalDateTime timeEnd;

    @Column(name = "created_date", columnDefinition = "datetime default current_timestamp")
    @CreatedDate
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "modified_date", columnDefinition = "datetime default current_timestamp")
    @LastModifiedDate
    private LocalDateTime modifiedDate = LocalDateTime.now();

    public TimeSlot(Long id, Long appointmentId, Long doctorId, LocalDateTime timeStart, LocalDateTime timeEnd) {
        this.id = id;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;

        this.doctor = new User();
        this.doctor.setId(doctorId);

        if (appointmentId != null) {
            this.appointment = new Appointment();
            this.appointment.setId(appointmentId);
        }
    }

    public boolean exists() {
        return id != null && id > 0;
    }

    public boolean isBooked() {
        return appointment != null;
    }

    public boolean isPast() {
        return timeStart.isBefore(LocalDateTime.now());
    }

    public String getTimeText() {
        return timeStart.format(DateTimeFormatter.ofPattern("h:m a"));
    }

    @Override
    public String toString() {
        return String.format(
                "[id=%d, appointment_id=%d, doctorId=%d, timeStart=%s, timeEnd=%s, createdDate=%s, modifiedDate=%s]",
                this.id,
                this.appointment != null ? this.appointment.getId() : null,
                this.doctor.getId(),
                this.timeStart.toString(),
                this.timeEnd.toString(),
                this.createdDate.toString(),
                this.modifiedDate.toString()
        );
    }

    public String getDate() {
        return DateUtils.toStandardDate(this.timeStart);
    }
}
