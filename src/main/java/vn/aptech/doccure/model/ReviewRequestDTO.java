package vn.aptech.doccure.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDTO {
    private Double rating;
    private String title;
    private String content;
    private Long doctorId;
    private Long patientId;
}
