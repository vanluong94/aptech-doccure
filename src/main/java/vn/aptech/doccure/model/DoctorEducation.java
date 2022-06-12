package vn.aptech.doccure.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorEducation {
    private String degree;
    private String college;
    private String from;
    private String to;

    @JsonIgnore
    public boolean isValid() {
        return ! this.degree.isEmpty() && ! this.college.isEmpty() && ! this.from.isEmpty();
    }
}
