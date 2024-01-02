/**
 * 
 */
package com.hospital.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ankit
 */
@Entity
@Getter
@Setter
public class Email {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@jakarta.validation.constraints.Email
	@NotBlank
//	@Column(unique = true)
	private String email;
	private boolean verified;
	private boolean prim;
    @CreationTimestamp
    private Instant createdOn;	
    @UpdateTimestamp
    private Instant lastUpdatedOn;
	@ManyToOne
	@JsonIgnore
	private User user;
}
