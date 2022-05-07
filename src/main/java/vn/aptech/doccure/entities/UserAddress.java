package vn.aptech.doccure.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_addresses")
public class UserAddress {
    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @MapsId
    @OneToOne
    private User user;

    @Column(name = "address_line_1")
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "state", length = 50)
    private String state;

    @Column(name = "country", length = 50)
    private String country;

    @Column(name = "postal_code")
    private Integer postalCode;
}