package vn.aptech.doccure.entities;

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
    private Appointment appointment;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "made_by", nullable = false, foreignKey = @ForeignKey(name = "appointment_log_user_fk"))
    private User madeBy;

    @Column(name = "created_date", nullable = false, columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdDate;

}