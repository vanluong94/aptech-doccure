package vn.aptech.doccure.entities;

import javax.persistence.*;

@Entity
@Table(name = "doctor_services")
public class DoctorService {
    @EmbeddedId
    private DoctorServiceId id;
}