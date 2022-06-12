package vn.aptech.doccure.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAward {
    String award;
    String year;

    @JsonIgnore
    public boolean isValid() {
        return ! award.isEmpty() && ! year.isEmpty();
    }
}
