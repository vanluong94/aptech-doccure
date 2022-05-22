package vn.aptech.doccure.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(value = "time_slots_default")
@IdClass(TimeSlotDefaultId.class)
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotDefault {

    @Id
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "time_slot_default_user_fk"))
    @JsonIgnore
    private User doctor;

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
    @Column(name = "created_at", columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("AppointmentDefault: ");
        str.append("[doctor_id = " + this.getDoctor().getId() + "]");
        str.append("[weekday = " + this.getWeekday() + "]");
        str.append("[time_start = " + (this.getTimeStart() != null ? this.getTimeStart().toString() : "null") + "]");
        str.append("[time_end = " + (this.getTimeEnd() != null ? this.getTimeEnd().toString() : "null") + "]");
        str.append("[status = " + this.getStatus() + "]");
        str.append("[created_at = " + this.getCreatedAt() + "]");
        return str.toString();
    }

    @Override
    public boolean equals(Object another) {
        if (another instanceof TimeSlotDefault) {
            TimeSlotDefault anotherApmtDefault = (TimeSlotDefault) another;
            if (
                    anotherApmtDefault.getDoctor().getId().equals(this.getDoctor().getId())
                    && anotherApmtDefault.getWeekday().equals(this.getWeekday())
                    && anotherApmtDefault.getTimeStart().equals(this.getTimeStart())
                    && anotherApmtDefault.getTimeEnd().equals(this.getTimeEnd())
            ) {
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public boolean isTimeRangeValid() {
        return this.getTimeStart() != null && this.getTimeEnd() != null && this.getTimeEnd().isAfter(this.getTimeStart());
    }
}