package vn.aptech.doccure.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "appointments_default")
@IdClass(AppointmentDefaultId.class)
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDefault {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", insertable = false, updatable = false)
    @JsonIgnore
    private User doctor;

    @Id
    @Column(name = "doctor_id", nullable = false)
    @JsonIgnore
    private Long doctorId;

    @Id
    @Column(name = "weekday", nullable = false)
    private Integer weekday;

    @Id
    @Column(name = "time_start", nullable = false)
    private LocalTime timeStart;

    @Id
    @Column(name = "time_end", nullable = false)
    private LocalTime timeEnd;

    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @JsonIgnore
    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("AppointmentDefault: ");
        str.append("[doctor_id = " + this.getDoctorId() + "]");
        str.append("[weekday = " + this.getWeekday() + "]");
        str.append("[time_start = " + (this.getTimeStart() != null ? this.getTimeStart().toString() : "null") + "]");
        str.append("[time_end = " + (this.getTimeEnd() != null ? this.getTimeEnd().toString() : "null") + "]");
        str.append("[status = " + this.getStatus() + "]");
        str.append("[created_at = " + this.getCreatedAt() + "]");
        return str.toString();
    }

    @JsonIgnore
    public boolean isTimeRangeValid() {
        return this.getTimeStart() != null && this.getTimeEnd() != null && this.getTimeEnd().isAfter(this.getTimeStart());
    }
}