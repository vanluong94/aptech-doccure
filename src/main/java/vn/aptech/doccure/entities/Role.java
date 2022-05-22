package vn.aptech.doccure.entities;

import lombok.*;

import org.springframework.data.relational.core.mapping.Table;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(value = "roles")
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "roles")
    Set<User> users;

    public Role(String name) {
        this.name = name;
    }
}