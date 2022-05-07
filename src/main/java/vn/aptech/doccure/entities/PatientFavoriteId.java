package vn.aptech.doccure.entities;

import lombok.Data;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class PatientFavoriteId implements Serializable {
    @MapsId
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;
}