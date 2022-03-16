package vn.aptech.entities;

import javax.persistence.*;

@Entity
@Table(name = "authorities")
public class Authority {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //TODO Reverse Engineering! Migrate other columns to the entity
}