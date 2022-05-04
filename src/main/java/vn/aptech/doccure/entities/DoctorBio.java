package vn.aptech.doccure.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "doctor_bio")
@Getter
@Setter
public class DoctorBio {
    @Id
    @Column(name = "doctor_id", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private User users;

    @Lob
    @Column(name = "biography")
    private String biography;

    @Column(name = "rating")
    private Double rating;

}