package vn.aptech.doccure.entities;

import javax.persistence.*;

@Entity
@Table(name = "doctor_services")
public class DoctorService {
    @EmbeddedId
    private DoctorServiceId id;

    @MapsId("doctorId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @MapsId("serviceId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public DoctorServiceId getId() {
        return id;
    }

    public void setId(DoctorServiceId id) {
        this.id = id;
    }
}