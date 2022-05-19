package vn.aptech.doccure.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Embeddable
public class PatientFavoriteId implements Serializable {
    @MapsId
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false, foreignKey = @ForeignKey(name = "favorite_doctor_fk"))
    private User doctor;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false, foreignKey = @ForeignKey(name = "favorite_patient_fk"))
    private User patient;
}