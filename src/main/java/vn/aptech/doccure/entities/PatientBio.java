package vn.aptech.doccure.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "patient_bio")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PatientBio implements Serializable  {
    @Id
    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "patient_id", nullable = false, foreignKey = @ForeignKey(name = "patient_bio_user_fk"))
    private User patient;

    @Column(name = "bloodType", length = 10)
    private String bloodType;

    @Column(name = "weight")
    private Short weight;

    @Column(name = "height")
    private Short height;
}