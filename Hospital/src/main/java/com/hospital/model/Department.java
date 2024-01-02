package com.hospital.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name = "departments")
public class Department {
	@JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@NotNull(message = "department name must not be null...")
	@NotBlank(message = "department name must not be blank...")
	@NotEmpty(message = "department name must not be empty..")
	
    private String name;
    
 
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
    @JsonIgnore
    @OneToMany(mappedBy = "department")
    private List<Slot> slots;
    
    @JsonIgnore
    @ManyToMany(mappedBy= "departments",cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<User>();
    
}