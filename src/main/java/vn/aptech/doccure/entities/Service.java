package vn.aptech.doccure.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "services")
public class Service implements Serializable  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotEmpty(message = "Name must not be null or empty!")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

//    @ManyToMany(targetEntity = vn.aptech.doccure.entities.User.class, mappedBy = "services")
//    private Set<User> users;

    public Service(String name) {
        this.name = name;
    }
}