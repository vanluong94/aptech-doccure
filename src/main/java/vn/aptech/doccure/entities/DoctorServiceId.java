package vn.aptech.doccure.entities;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DoctorServiceId implements Serializable {
    private static final long serialVersionUID = 612886675747513053L;
    @Column(name = "doctor_id", nullable = false)
    private Integer doctorId;
    @Column(name = "service_id", nullable = false)
    private Integer serviceId;

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctorId, serviceId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DoctorServiceId entity = (DoctorServiceId) o;
        return Objects.equals(this.doctorId, entity.doctorId) &&
                Objects.equals(this.serviceId, entity.serviceId);
    }
}