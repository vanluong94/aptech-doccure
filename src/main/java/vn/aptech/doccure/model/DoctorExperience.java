package vn.aptech.doccure.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorExperience {
    String hospital;
    String from;
    String to;

    @JsonIgnore
    public boolean isValid() {
        return ! this.hospital.isEmpty() && ! this.from.isEmpty();
    }
}
