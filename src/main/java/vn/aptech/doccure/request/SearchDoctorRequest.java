package vn.aptech.doccure.request;

import java.util.Date;
import java.util.List;

public class SearchDoctorRequest {
    private Date timeSlot;
    private Boolean gender;
    private List<Long> specialist;
    private String sort;

    public Date getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(Date timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public List<Long> getSpecialist() {
        return specialist;
    }

    public void setSpecialist(List<Long> specialist) {
        this.specialist = specialist;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
