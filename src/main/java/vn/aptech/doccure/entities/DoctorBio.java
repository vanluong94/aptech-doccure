package vn.aptech.doccure.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.relational.core.mapping.Table;
import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(value = "doctor_bio")
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

    @Column(name = "rating", nullable = false, columnDefinition = "double(1,1) default 0")
    private Double rating;

    @Lob
    @Column(name = "educations", columnDefinition = "json")
    private String educations;

    @Lob
    @Column(name = "experiences", columnDefinition = "json")
    private String experiences;

    @Lob
    @Column(name = "awards", columnDefinition = "json")
    private String awards;

}