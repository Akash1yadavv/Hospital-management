/**
 * 
 */
package com.hospital.media;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

/**
 * 
 */

@Entity
@Data
public class MediaResource {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private String fileName;
	private String contentType;
	private Long size;
    @CreationTimestamp
    private Instant createdOn;	
    @UpdateTimestamp
    private Instant lastUpdatedOn;
    @ManyToOne
    @JsonIgnore
    private User owner;

}
