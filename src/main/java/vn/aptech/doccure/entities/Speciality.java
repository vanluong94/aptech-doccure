package vn.aptech.doccure.entities;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(value = "specialities")
public class Speciality implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotEmpty(message = "Name must not be null or empty!")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "slug", nullable = false, unique = true, length = 100)
    private String slug;

    @Column(name = "image")
    private String image;

//    @ManyToMany(targetEntity = vn.aptech.doccure.entities.User.class, mappedBy = "specialities")
//    private Set<User> users;

    @Transient
    private MultipartFile imageData;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Speciality that = (Speciality) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}