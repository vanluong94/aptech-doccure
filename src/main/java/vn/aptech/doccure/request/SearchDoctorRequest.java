package vn.aptech.doccure.request;

import vn.aptech.doccure.entities.Speciality;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchDoctorRequest {

    @Null
    private Date timeSlot;
    @Null
    private List<Short> gender;
    @Null
    private List<Speciality> specialist = new ArrayList<>();
    private String sort;

    public Date getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(Date timeSlot) {
        this.timeSlot = timeSlot;
    }

    public List<Short> getGender() {
        return gender;
    }

    public void setGender(List<Short> gender) {
        this.gender = gender;
    }

    public List<Speciality> getSpecialist() {
        return specialist;
    }

    public void setSpecialist(List<Speciality> specialist) {
        this.specialist = specialist;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
