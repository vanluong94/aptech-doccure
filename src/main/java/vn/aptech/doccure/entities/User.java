package vn.aptech.doccure.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(name = "dob", columnDefinition = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    @Column(name = "avatar")
    private String avatar;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private Set<Review> doctorReviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private Set<Review> patientReviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<TimeSlot> timeSlots = new LinkedHashSet<>();

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy("weekday ASC, time_start ASC")
    private Set<TimeSlotDefault> timeSlotsDefault = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "role_user_fk")),
            inverseJoinColumns = @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "user_role_fk"))
    )
    private Set<Role> roles = new LinkedHashSet<>();

    @Transient
    private MultipartFile avatarMultipartFile;

    @Column(name = "gender")
    private Short gender;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "doctor_speciality",
            joinColumns = @JoinColumn(name = "doctor_id", foreignKey = @ForeignKey(name = "doctor_specialty_fk")),
            inverseJoinColumns = @JoinColumn(name = "speciality_id", foreignKey = @ForeignKey(name = "specialty_doctor_fk"))
    )
    private Set<Speciality> specialities;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "doctor_services",
            joinColumns = @JoinColumn(name = "doctor_id", foreignKey = @ForeignKey(name = "doctor_service_fk")),
            inverseJoinColumns = @JoinColumn(name = "service_id", foreignKey = @ForeignKey(name = "service_doctor_fk"))
    )
    private Set<Service> services;

    @OneToOne(mappedBy = "doctor", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private DoctorClinic clinic;

    @OneToOne(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DoctorBio bio;

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private PatientBio patientBio;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserAddress patientAddress;

    @CreatedDate
    @Column(name = "created_date", columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdDate = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "modified_date", columnDefinition = "datetime default current_timestamp")
    private LocalDateTime modifiedDate = LocalDateTime.now();

    @Column(unique = true)
    private Long resetPasswordToken;

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

    public User(Long id) {
        this.id = id;
    }

    @Override
    public boolean isEnabled() {
        return this.getEnabled() == 1;
    }

    public boolean hasRole(String role) {
        for (Role uRole : this.getRoles()) {
            if (uRole.getName().equals(role)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDoctor() {
        return this.hasRole(Constants.Roles.ROLE_DOCTOR);
    }

    public boolean isPatient() {
        return this.hasRole(Constants.Roles.ROLE_PATIENT);
    }

    public boolean isAdmin() {
        return this.hasRole(Constants.Roles.ROLE_ADMIN);
    }

    public String getFullName() {
        return this.getFirstName() + " " + this.getLastName();
    }

    public String getDoctorTitle() {
        return "Dr. " + getFullName();
    }

    public boolean hasAnyRole(){
        return (this.isDoctor() | this.isPatient() | this.isAdmin());
    }
    public String getTheAvatar() {
        if (avatar != null && !avatar.isEmpty()) {
            return "/files/" + avatar;
        } else {
            String filename;
            if (hasRole(Constants.Roles.ROLE_DOCTOR)) {
                if (gender != null && gender.equals(Constants.Genders.MALE)) {
                    filename = "avatar-doctor-male.png";
                } else {
                    filename = "avatar-doctor-female.png";
                }
            } else if (this.hasRole(Constants.Roles.ROLE_PATIENT)) {
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

    public String getGenderText() {
        if (gender != null) {
            if (gender.equals(Constants.Genders.MALE)) {
                return "Male";
            } else if (gender.equals(Constants.Genders.FEMALE)) {
                return "Female";
            }
        }
        return "Unknown";
    }

    public String getGenderValueText(){
        return gender.toString();
    }

    public String getStatusValueText(){
        return enabled.toString();
    }

    public String getStatusText(){
        if(enabled != null){
            if(this.isEnabled()) return "Activated";
            else return "Locked";
        }
        return "Unknown";
    }

    public double getAvgReview() {
        if (doctorReviews.isEmpty()) {
            return 0;
        }
        double totalAvg = 0;
        int totalRate = 0;
        try {
            for (Review review : doctorReviews) {
                if (review.getRating() != null) {
                    totalRate++;
                    totalAvg += review.getRating();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return totalRate > 0 ? totalAvg / totalRate : 0;
    }

    @Override
    public boolean equals(Object another) {
        return ((User) another).getId().equals(id);
    }
}