package vn.aptech.doccure.entities;

import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DoctorServiceId implements Serializable {
    @MapsId
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;
}