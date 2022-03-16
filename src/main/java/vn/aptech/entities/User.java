package vn.aptech.entities;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "enabled", nullable = false)
    private Integer enabled;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "doctor")
    private Set<Review> reviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "doctor")
    private Set<Appointment> appointments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "doctor")
    private Set<DoctorTimeSlot> doctorTimeSlots = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Authority> authorities = new LinkedHashSet<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private UserAddress userAddresses;

    @OneToMany(mappedBy = "user")
    private Set<PatientFavorite> patientFavorites = new LinkedHashSet<>();

    @OneToMany(mappedBy = "patient")
    private Set<PatientBio> patientBios = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "doctor_specialty",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "specialty_id"))
    private Set<Speciality> specialities = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "doctor_services",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private Set<Service> services = new LinkedHashSet<>();

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

    public Set<PatientFavorite> getPatientFavorites() {
        return patientFavorites;
    }

    public void setPatientFavorites(Set<PatientFavorite> patientFavorites) {
        this.patientFavorites = patientFavorites;
    }

    public UserAddress getUserAddresses() {
        return userAddresses;
    }

    public void setUserAddresses(UserAddress userAddresses) {
        this.userAddresses = userAddresses;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
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
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}