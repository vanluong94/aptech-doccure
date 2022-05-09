package vn.aptech.doccure.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "doctor_bio")
public class DoctorBio implements Serializable {
    @Id
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @MapsId
    @OneToOne
    private User doctor;

    @Lob
    @Column(name = "biography")
    private String biography;

    @Column(name = "rating")
    private Double rating;
}