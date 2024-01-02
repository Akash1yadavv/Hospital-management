package com.hospital.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.enums.AppointmentStatus;
import com.hospital.enums.PaymentStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "appointments")
public class Appointment {

//	@JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "start")
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;

//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "end")
    private LocalDateTime endTime;

//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    private PatientInfo patientInfo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AppointmentStatus status;
    
    @CreationTimestamp
    private Instant createdOn;	
    
    @Enumerated(EnumType.STRING)
    @Column(name = "paymentStatus")
    private PaymentStatus paymentStatus;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_slot")
    private Slot slot;

//    @JsonIgnore
//    @OneToMany(mappedBy = "appointment")
//    private List<ChatMessage> chatMessages;
//    
//    @JsonIgnore
//    @OneToOne(cascade=CascadeType.ALL)
//    @JoinColumn(name = "id_invoice")
//    private Invoice invoice;
    
//    @JsonIgnore
//    @OneToOne(mappedBy = "requested", cascade = {CascadeType.ALL})
//    private ExchangeRequest exchangeRequest;


}
