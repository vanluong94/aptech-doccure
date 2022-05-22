package vn.aptech.doccure.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.relational.core.mapping.Table;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(value = "patient_favorites")
public class PatientFavorite {
    @EmbeddedId
    private PatientFavoriteId id;

    @Column(name = "created_at", nullable = false, columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    public PatientFavorite(PatientFavoriteId id) {
        this.id = id;
    }
}