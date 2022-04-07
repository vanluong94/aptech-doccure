package vn.aptech.doccure.entities;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends AbstractEntity implements Serializable {
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @NotEmpty(message = "{user.password.empty}")
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    /*
     * Dùng annotation Transient báo cho Spring biết
     * không mapping field database
     */
    @Transient
    private String confirmPassword;

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

    @OneToMany(mappedBy = "doctor")
    private Set<Review> reviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "doctor")
    private Set<Appointment> appointments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "doctor")
    private Set<DoctorTimeSlot> doctorTimeSlots = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "patient")
    private Set<PatientBio> patientBios = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "doctor_specialty", joinColumns = @JoinColumn(name = "doctor_id"), inverseJoinColumns = @JoinColumn(name = "specialty_id"))
    private Set<Speciality> specialities = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "doctor_services", joinColumns = @JoinColumn(name = "doctor_id"), inverseJoinColumns = @JoinColumn(name = "service_id"))
    private Set<Service> services = new LinkedHashSet<>();

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }

    public Set<Speciality> getSpecialities() {
        return specialities;
    }

    public void setSpecialities(Set<Speciality> specialities) {
        this.specialities = specialities;
    }

    public Set<PatientBio> getPatientBios() {
        return patientBios;
    }

    public void setPatientBios(Set<PatientBio> patientBios) {
        this.patientBios = patientBios;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<DoctorTimeSlot> getDoctorTimeSlots() {
        return doctorTimeSlots;
    }

    public void setDoctorTimeSlots(Set<DoctorTimeSlot> doctorTimeSlots) {
        this.doctorTimeSlots = doctorTimeSlots;
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}