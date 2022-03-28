package vn.aptech.doccure.entities;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DoctorSpecialtyId implements Serializable {
    private static final long serialVersionUID = -4775307575220419365L;
    @Column(name = "doctor_id", nullable = false)
    private Integer doctorId;
    @Column(name = "specialty_id", nullable = false)
    private Integer specialtyId;

    public Integer getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(Integer specialtyId) {
        this.specialtyId = specialtyId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctorId, specialtyId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DoctorSpecialtyId entity = (DoctorSpecialtyId) o;
        return Objects.equals(this.doctorId, entity.doctorId) &&
                Objects.equals(this.specialtyId, entity.specialtyId);
    }
}