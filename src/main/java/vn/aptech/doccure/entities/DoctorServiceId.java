package vn.aptech.doccure.entities;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class DoctorServiceId implements Serializable {
    @MapsId
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false, foreignKey = @ForeignKey(name = "service_doctor_fk"))
    private User doctor;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false, foreignKey = @ForeignKey(name = "service_service_fk"))
    private Service service;
}