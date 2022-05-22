package vn.aptech.doccure.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(value = "patient_bio")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PatientBio implements Serializable  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "patient_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "patient_bio_user_fk")
    )
    private User patient;

    @Column(name = "bloodType", length = 10)
    private String bloodType;

    @Column(name = "weight")
    private Short weight;

    @Column(name = "height")
    private Short height;
}