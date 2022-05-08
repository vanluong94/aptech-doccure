package vn.aptech.doccure.entities;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import vn.aptech.doccure.common.Constants;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    //    @NotEmpty(message = "Mật khẩu là bắt buộc")
    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "enabled", nullable = false)
    private Integer enabled = 1;

    @Email
    @NotEmpty(message = "Email must not be null or empty!")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Size(min = 2, max = 55, message = "First name size must be between 2 and 55")
    @NotEmpty(message = "First name must not be null or empty!")
    @Column(name = "first_name", nullable = false, length = 55)
    private String firstName;

    @Size(min = 2, max = 55, message = "Last name size must be between 2 and 55")
    @NotEmpty(message = "Last name must not be null or empty!")
    @Column(name = "last_name", nullable = false, length = 55)
    private String lastName;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "dob")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    @Column(name = "avatar")
    private String avatar;

    @Transient
    private MultipartFile avatarMultipartFile;

    @Column(name = "gender")
    private Short gender;

    //    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
//    private Set<Review> doctorReviews = new LinkedHashSet<>();
//
//    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
//    private Set<Review> patientReviews = new LinkedHashSet<>();
//
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Appointment> appointments;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("timeStart ASC")
    private Set<AppointmentDefault> appointmentDefaults;
//

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "doctor_speciality", joinColumns = {@JoinColumn(name = "doctor_id")}, inverseJoinColumns = {@JoinColumn(name = "speciality_id")})
    private Set<Speciality> specialities;
    //
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "doctor_services", joinColumns = {@JoinColumn(name = "doctor_id")}, inverseJoinColumns = {@JoinColumn(name = "service_id")})
    private Set<Service> services;

    @OneToOne(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private DoctorClinic clinic;

    @OneToOne(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private DoctorBio bio;
    //
//    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
//    private Set<Appointment> patientAppointments = new LinkedHashSet<>();
//
//    @OneToMany(mappedBy = "patient")
//    private Set<PatientBio> patientBios = new LinkedHashSet<>();
//
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @CreatedDate
    private LocalDateTime createdDate = LocalDateTime.now();
    @LastModifiedDate
    private LocalDateTime modifiedDate = LocalDateTime.now();

    public User() {
    }

    public User(Integer enabled, Set<Role> roles) {
        this.enabled = enabled;
//        this.roles = roles;
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
        return enabled == 1;
    }

    public boolean hasRole(String role) {
//        for (Role uRole : roles) {
//            if (uRole.getName().equals(role)) {
//                return true;
//            }
//        }
        return false;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getTheAvatar() {
        if (avatar != null && !avatar.isEmpty()) {
            return avatar;
        } else {
            String filename;
            if (hasRole(Constants.Roles.ROLE_DOCTOR)) {
                if (gender != null && gender.equals(Constants.Genders.MALE)) {
                    filename = "avatar-doctor-male.png";
                } else {
                    filename = "avatar-doctor-female.png";
                }
            } else if (hasRole(Constants.Roles.ROLE_PATIENT)) {
                if (gender != null && gender.equals(Constants.Genders.MALE)) {
                    filename = "avatar-patient-male.png";
                } else {
                    filename = "avatar-patient-female.png";
                }
            } else {
                filename = "avatar-admin.png";
            }

            return "/assets/img/" + filename;
        }
    }
}