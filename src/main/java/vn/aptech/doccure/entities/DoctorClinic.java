package vn.aptech.doccure.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.web.multipart.MultipartFile;
import vn.aptech.doccure.utils.JSONUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctor_clinics")
@Getter
@Setter
public class DoctorClinic {
    @Id
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "specialities", nullable = false)
    private String specialities;

    @Column(name = "lat", precision = 10, scale = 8)
    private BigDecimal lat;

    @Column(name = "lng", precision = 11, scale = 8)
    private BigDecimal lng;

    @Column(name = "address_line_1", nullable = false)
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "state", nullable = false, length = 50)
    private String state;

    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @Column(name = "postal_code", nullable = false)
    private Integer postalCode;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Column(name = "images")
    private String images;

    @Transient
    private List<String> parsedImages = new ArrayList<>();

    @Transient
    private List<MultipartFile> postedImages = new ArrayList<>();

    @Transient
    private List<String> deletedImages = new ArrayList<>();

    public void parseImages() {
        ObjectMapper jsonMapper = new ObjectMapper();
        try {
            this.parsedImages = jsonMapper.readValue(images, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void syncParsedImages() {
        this.setImages(JSONUtils.stringify(this.getParsedImages()));
    }
}