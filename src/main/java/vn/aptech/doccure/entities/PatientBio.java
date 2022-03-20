package vn.aptech.doccure.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "patient_bio")
public class PatientBio implements Serializable  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private User patient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    //TODO Reverse Engineering! Migrate other columns to the entity
}