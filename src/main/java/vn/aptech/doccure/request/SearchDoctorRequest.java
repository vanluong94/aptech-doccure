package vn.aptech.doccure.request;

import lombok.Getter;
import lombok.Setter;
import vn.aptech.doccure.entities.Speciality;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SearchDoctorRequest {
    private Date timeSlot;
    private List<Short> gender;
    private List<Speciality> specialist = new ArrayList<>();
    private String sort;
    private String location;
    private String query;
}
