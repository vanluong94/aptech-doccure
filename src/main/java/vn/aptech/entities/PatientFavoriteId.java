package vn.aptech.entities;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PatientFavoriteId implements Serializable {
    private static final long serialVersionUID = -3388868467326734462L;
    @Column(name = "doctor_id", nullable = false)
    private Integer doctorId;
    @Column(name = "patient_id", nullable = false)
    private Integer patientId;

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctorId, patientId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PatientFavoriteId entity = (PatientFavoriteId) o;
        return Objects.equals(this.doctorId, entity.doctorId) &&
                Objects.equals(this.patientId, entity.patientId);
    }
}