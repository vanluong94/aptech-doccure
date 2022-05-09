package vn.aptech.doccure.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "patient_favorites")
public class PatientFavorite {
    @EmbeddedId
    private PatientFavoriteId id;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}