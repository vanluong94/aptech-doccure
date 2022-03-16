package vn.aptech.entities;

import javax.persistence.*;

@Entity
@Table(name = "user_addresses")
public class UserAddress {
    @Id
    @Column(name = "user_id", nullable = false)
    private Integer id;
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //TODO Reverse Engineering! Migrate other columns to the entity
}