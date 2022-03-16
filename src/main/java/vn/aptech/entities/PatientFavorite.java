package vn.aptech.entities;

import javax.persistence.*;

@Entity
@Table(name = "patient_favorites")
public class PatientFavorite {
    @EmbeddedId
    private PatientFavoriteId id;
    @MapsId("doctorId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PatientFavoriteId getId() {
        return id;
    }

    public void setId(PatientFavoriteId id) {
        this.id = id;
    }

    //TODO Reverse Engineering! Migrate other columns to the entity
}