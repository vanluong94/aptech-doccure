package vn.aptech.entities;

import javax.persistence.*;

@Entity
@Table(name = "doctor_time_slots")
public class DoctorTimeSlot {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private User doctor;

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    //TODO Reverse Engineering! Migrate other columns to the entity
}