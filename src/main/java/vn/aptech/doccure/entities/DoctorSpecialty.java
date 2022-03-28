package vn.aptech.doccure.entities;

import javax.persistence.*;

@Entity
@Table(name = "doctor_specialty")
public class DoctorSpecialty {
    @EmbeddedId
    private DoctorSpecialtyId id;

    @MapsId("doctorId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @MapsId("specialtyId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "specialty_id", nullable = false)
    private Speciality specialty;

    public Speciality getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Speciality specialty) {
        this.specialty = specialty;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public DoctorSpecialtyId getId() {
        return id;
    }

    public void setId(DoctorSpecialtyId id) {
        this.id = id;
    }
}