package vn.aptech.doccure.entities;

import lombok.*;
import vn.aptech.doccure.common.Constants;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
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

    public String getRoleText(){
        if(name.equals(Constants.Roles.ROLE_ADMIN)) return "Admin";
        else if(name.equals(Constants.Roles.ROLE_DOCTOR)) return "Doctor";
        else if(name.equals(Constants.Roles.ROLE_PATIENT)) return "Patient";
        else return "Unknown";
    }
    public String getRoleValueText(){
        return this.id.toString();
    }
}