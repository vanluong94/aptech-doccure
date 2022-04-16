package vn.aptech.doccure.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends AbstractEntity implements UserDetails {
    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @NotEmpty(message = "Mật khẩu là bắt buộc")
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "enabled", nullable = false)
    private Integer enabled = 1;

    @Email
    @NotEmpty(message = "Email là bắt buộc")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Size(min = 2, max = 30, message = "Tên từ 2 đên 30 kí tự")
    @NotEmpty(message = "Tên là bắt buộc")
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Size(min = 2, max = 30, message = "Tên từ 2 đên 30 kí tự")
    @NotEmpty(message = "Tên là bắt buộc")
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "avatar")
    private String avatar;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private Set<Review> doctorReviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private Set<Review> patientReviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private Set<Appointment> doctorAppointments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("time_start ASC")
    private Set<AppointmentDefault> doctorAppointmentsDefault = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "doctor_specialty", joinColumns = @JoinColumn(name = "doctor_id"), inverseJoinColumns = @JoinColumn(name = "specialty_id"))
    private Set<Speciality> doctorSpecialities = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "doctor_services", joinColumns = @JoinColumn(name = "doctor_id"), inverseJoinColumns = @JoinColumn(name = "service_id"))
    private Set<Service> doctorServices = new LinkedHashSet<>();

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private Set<Appointment> patientAppointments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "patient")
    private Set<PatientBio> patientBios = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new LinkedHashSet<>();

    public User() {
    }

    public User(Integer enabled, Set<Role> roles) {
        this.enabled = enabled;
        this.roles = roles;
    }

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