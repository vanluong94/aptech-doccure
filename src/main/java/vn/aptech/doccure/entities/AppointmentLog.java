package vn.aptech.doccure.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "appointment_logs")
public class AppointmentLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_id", nullable = false, foreignKey = @ForeignKey(name = "appointment_log_apmt_fk"))
    @JsonIgnore
    private Appointment appointment;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "made_by", foreignKey = @ForeignKey(name = "appointment_log_user_fk"))
    @JsonIgnore
    private User madeBy;

    @Column(name = "created_date", nullable = false, columnDefinition = "datetime default current_timestamp")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMM yyyy kk:mm")
    private LocalDateTime createdDate = LocalDateTime.now();

    public AppointmentLog(Appointment appointment, String content, User madeBy) {
        this.appointment = appointment;
        this.content = content;
        this.madeBy = madeBy;
        this.createdDate = LocalDateTime.now();
    }

}