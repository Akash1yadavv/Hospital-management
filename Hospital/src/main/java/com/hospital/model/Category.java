package com.hospital.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.List;

import com.hospital.enums.AppointmentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "cotegories")
public class Category {
	@JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@NotNull(message = "category name must not be null...")
	@NotBlank(message = "category name must not be blank...")
	@NotEmpty(message = "category name must not be empty")
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Department> departments;
//    @JsonIgnore
//    @OneToMany
//    private List<Slot> slot; 
}