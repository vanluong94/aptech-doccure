package vn.aptech.doccure.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "doctor_time_slots")
public class DoctorTimeSlot implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private User doctor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    //TODO Reverse Engineering! Migrate other columns to the entity
}