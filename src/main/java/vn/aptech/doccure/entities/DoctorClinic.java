package vn.aptech.doccure.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.web.multipart.MultipartFile;
import vn.aptech.doccure.utils.JSONUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doctor_clinics")
public class DoctorClinic implements Serializable {
    @Id
    @Column(name = "doctor_id")
    private Long doctorId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "doctor_id", nullable = false, foreignKey = @ForeignKey(name = "doctor_clinic_user_fk"))
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

    @Column(name = "postal_code", nullable = false, length = 10)
    private Integer postalCode;

    @Column(name = "created_date", columnDefinition = "datetime default current_timestamp")
    @CreatedDate
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "modified_date", columnDefinition = "datetime default current_timestamp")
    @LastModifiedDate
    private LocalDateTime modifiedDate = LocalDateTime.now();

    @Column(name = "images", columnDefinition = "json")
    private String images;

    @Transient
    private List<String> parsedImages = new LinkedList<>();

    @Transient
    private List<MultipartFile> postedImages = new LinkedList<>();

    @Transient
    private List<String> deletedImages = new LinkedList<>();

    public void parseImages() {
        if (images != null && ! images.isEmpty()) {
            ObjectMapper jsonMapper = new ObjectMapper();
            try {
                this.parsedImages = jsonMapper.readValue(images, new TypeReference<List<String>>() {
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[DoctorClinic:");
        builder.append(" doctorId=" + doctorId);
        builder.append("; name=" + name);
        builder.append("; specialties=" + specialities);
        builder.append("; lat=" + lat);
        builder.append("; lng=" + lng);
        builder.append("; addressLine1=" + addressLine1);
        builder.append("; addressLine2=" + addressLine2);
        builder.append("; postCode=" + postalCode);
        builder.append("; images=" + images);
        builder.append("]");
        return builder.toString();
    }

    public void syncParsedImages() {
        this.setImages(JSONUtils.stringify(this.getParsedImages()));
    }

    public boolean hasLocation() {
        return this.lat != null && this.lng != null;
    }

    public String getAddress() {

        List<String> parts = new LinkedList<>();

        if (this.addressLine1 != null && this.addressLine1.length() > 0) {
            parts.add(this.addressLine1);
        }

        if (this.addressLine2 != null && this.addressLine2.length() > 0) {
            parts.add(this.addressLine2);
        }

        if (this.city != null && this.city.length() > 0) {
            parts.add(this.city);
        }

        if (this.state != null && this.state.length() > 0) {
            parts.add(this.state);
        }

        if (this.country != null && this.country.length() > 0) {
            parts.add(this.country);
        }

        return String.join(", ", parts);
    }
}