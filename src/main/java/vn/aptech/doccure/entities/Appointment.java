package vn.aptech.doccure.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Appointment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private User patient;

}