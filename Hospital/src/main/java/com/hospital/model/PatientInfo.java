package com.hospital.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.enums.AuthProvider;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "Patient")
public class PatientInfo {
//	@JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@NotNull(message = "Patient name must not be null..")
	@NotBlank(message = "Please enter your Your full name")
    @Column(name="patient_name")
	private String patientName;
	
	@Column(name="gender")
	private String gender;
	
    @Column(name = "email")
    @Email(message = "Invalid email address")
    private String email;


    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Invalid mobile number")
    @Column(name = "mobile")
    private String mobile;

    @NotNull(message = "Aadhar must not be null..")
    @NotBlank(message = "Please enter your aadhar number")
    @Size(min = 12, max = 12, message = "Aadhar number must be exactly 12 digits long")
    @Column(name = "aadhar")
    private String aadhar;
    
    private int age; 
    private String address;
 
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<Appointment> appointment;
}
