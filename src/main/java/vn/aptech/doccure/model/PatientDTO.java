package vn.aptech.doccure.model;

import lombok.Data;
import vn.aptech.doccure.entities.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

@Data
public class PatientDTO {

    private User patient;

    public PatientDTO(User patient) {
        this.patient = patient;
    }

    public static PatientDTO from(User patient) {
        return new PatientDTO(patient);
    }

    public String getUrl() {
        return "#";
    }

    public String getCity() {
        if (this.patient.getPatientAddress() != null) {
            return this.patient.getPatientAddress().getCity() + ", " + this.patient.getPatientAddress().getCountry();
        }
        return null;
    }

    public String getDob() {
        if (this.patient.getDob() != null) {
            DateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
            return formatter.format(this.patient.getDob());
        }
        return null;
    }

    public Integer getAge() {
        if (this.patient.getDob() != null) {
            LocalDate dob = this.patient.getDob().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate now = LocalDate.now();
            Period period = Period.between(dob, now);
            return period.getYears();
        }
        return null;
    }

    public String getGender() {
        return this.patient.getGenderText();
    }

    public String getBloodType() {
        if (this.patient.getPatientBio() != null) {
            return this.patient.getPatientBio().getBloodType();
        }
        return "Unknown";
    }

    public String getHeight() {
        if (this.patient.getPatientBio() != null) {
            return this.patient.getPatientBio().getHeight().toString();
        }
        return "Unknown";
    }

    public String getWeight() {
        if (this.patient.getPatientBio() != null) {
            return this.patient.getPatientBio().getWeight().toString();
        }
        return "Unknown";
    }
}
