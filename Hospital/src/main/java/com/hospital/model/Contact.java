package com.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "contacts")
public class Contact 
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name")
    private String name;
    
	@Pattern(regexp = "^[6-9][0-9]{9}$", message = "Invalid mobile number")
    @Column(name = "mobile_no")
    private String mobileNo;
	
    @Email(message = "Invalid email address")
    @Column(name = "email")
    private String email;
    
    @Column(name="message")
    private String message;
    
}