package vn.aptech.entities;

import javax.persistence.*;

@Entity
@Table(name = "patient_bio")
public class PatientBio {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private User patient;

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    //TODO Reverse Engineering! Migrate other columns to the entity
}