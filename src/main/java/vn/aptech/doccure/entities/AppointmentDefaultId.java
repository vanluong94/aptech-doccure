package vn.aptech.doccure.entities;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalTime;

@Data
public class AppointmentDefaultId  implements Serializable {
    private Long doctorId;
    private Integer weekday;
    private LocalTime timeStart;
    private LocalTime timeEnd;
}
