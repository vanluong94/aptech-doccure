package vn.aptech.doccure.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.aptech.doccure.model.DoctorAward;
import vn.aptech.doccure.model.DoctorEducation;
import vn.aptech.doccure.model.DoctorExperience;
import vn.aptech.doccure.utils.converter.DoctorAwardJsonConverter;
import vn.aptech.doccure.utils.converter.DoctorEducationJsonConverter;
import vn.aptech.doccure.utils.converter.DoctorExperienceJsonConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doctor_bio")
public class DoctorBio implements Serializable {

    @Id
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "doctor_id", nullable = false, foreignKey = @ForeignKey(name = "doctor_bio_user_fk"))
    private User doctor;

    @Lob
    @Column(name = "biography")
    private String biography;

    @Column(name = "rating", columnDefinition = "double(1,1) default 0")
    private Double rating = 0d;

    @Lob
    @Column(name = "educations", columnDefinition = "json")
    @Convert(converter = DoctorEducationJsonConverter.class)
    private List<DoctorEducation> educations;

    @Lob
    @Column(name = "experiences", columnDefinition = "json")
    @Convert(converter = DoctorExperienceJsonConverter.class)
    private List<DoctorExperience> experiences;

    @Lob
    @Column(name = "awards", columnDefinition = "json")
    @Convert(converter = DoctorAwardJsonConverter.class)
    private List<DoctorAward> awards;

    @PrePersist
    @PreUpdate
    public void sanitizeFields () {
        if (this.educations != null) {
            this.educations = this.educations.stream().filter(a -> a.isValid()).collect(Collectors.toList());
        }

        if (this.experiences != null) {
            this.experiences = this.experiences.stream().filter(a -> a.isValid()).collect(Collectors.toList());
        }

        if (this.awards != null) {
            this.awards = this.awards.stream().filter(a -> a.isValid()).collect(Collectors.toList());
        }
    }

}