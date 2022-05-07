package vn.aptech.doccure.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "doctor_bio")
public class DoctorBio {
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