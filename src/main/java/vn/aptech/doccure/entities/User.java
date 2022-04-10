package vn.aptech.doccure.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends AbstractEntity implements UserDetails {
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @NotEmpty(message = "{user.password.empty}")
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "enabled", nullable = false)
    private Integer enabled;

    @Email
    @NotEmpty(message = "{user.email.empty}")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Size(min = 2, max = 30, message = "{user.name.length}")
    @NotEmpty(message = "{name.required}")
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Size(min = 2, max = 30, message = "{user.name.length}")
    @NotEmpty(message = "{name.required}")
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "avatar")
    private String avatar;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private Set<Review> reviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private Set<Appointment> appointments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    @JoinTable(name = "appointments_default", joinColumns = @JoinColumn(name = "doctor_id"))
    private Set<AppointmentDefault> appointmentsDefault = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private Set<PatientBio> patientBios = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "doctor_specialty", joinColumns = @JoinColumn(name = "doctor_id"), inverseJoinColumns = @JoinColumn(name = "specialty_id"))
    private Set<Speciality> specialities = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "doctor_services", joinColumns = @JoinColumn(name = "doctor_id"), inverseJoinColumns = @JoinColumn(name = "service_id"))
    private Set<Service> services = new LinkedHashSet<>();

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    /*
     * Dùng annotation Transient báo cho Spring biết
     * không mapping field database
     */
    @Transient
    private String confirmPassword;

    /**
     * For UserDetails implementation
     */
    @Transient
    private Collection<? extends GrantedAuthority> authorities = new HashSet<>();
    @Transient
    private boolean accountNonExpired = true;
    @Transient
    private boolean accountNonLocked = true;
    @Transient
    private boolean credentialsNonExpired = true;

    @Override
    public boolean isEnabled() {
        return this.getEnabled() == 1;
    }
}