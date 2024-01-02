package com.hospital.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Otp {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private String otp;
	private int readCount = 0;
	@CreationTimestamp
	private Instant createdOn;	
	private Instant expiresAt;
}
