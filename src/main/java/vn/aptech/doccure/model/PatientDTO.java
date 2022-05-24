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

    private User user;

    public PatientDTO(User patient) {
        this.user = patient;
    }

    public static PatientDTO from(User patient) {
        return new PatientDTO(patient);
    }

    public String getUrl() {
        return "#";
    }

    public String getCity() {
        if (this.user.getPatientAddress() != null) {
            return this.user.getPatientAddress().getCity() + ", " + this.user.getPatientAddress().getCountry();
        }
        return null;
    }

    public String getDob() {
        if (this.user.getDob() != null) {
            DateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
            return formatter.format(this.user.getDob());
        }
        return null;
    }

    public Integer getAge() {
        if (this.user.getDob() != null) {
            LocalDate dob = this.user.getDob().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate now = LocalDate.now();
            Period period = Period.between(dob, now);
            return period.getYears();
        }
        return null;
    }

    public String getGender() {
        return this.user.getGenderText();
    }

    public String getBloodType() {
        if (this.user.getPatientBio() != null) {
            return this.user.getPatientBio().getBloodType();
        }
        return "Unknown";
    }

    public String getHeight() {
        if (this.user.getPatientBio() != null) {
            return this.user.getPatientBio().getHeight().toString();
        }
        return "Unknown";
    }

    public String getWeight() {
        if (this.user.getPatientBio() != null) {
            return this.user.getPatientBio().getWeight().toString();
        }
        return "Unknown";
    }
}
