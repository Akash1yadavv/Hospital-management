package com.hospital.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.enums.AppointmentStatus;
import com.hospital.enums.SlotAvailabilityStatus;
import com.hospital.model.Category;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name = "slots")
public class Slot {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(unique=true)
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private SlotAvailabilityStatus availabilityStatus;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id")
    private Department department;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Slot other = (Slot) obj;
		return availabilityStatus == other.availabilityStatus && Objects.equals(category, other.category)
				&& Objects.equals(department, other.department) && Objects.equals(endTime, other.endTime)
				&& Objects.equals(id, other.id) && Objects.equals(startTime, other.startTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(availabilityStatus, category, department, endTime, id, startTime);
	}

	@Override
	public String toString() {
		return "Slot [id=" + id + ", startTime=" + startTime + ", endTime=" + endTime + ", availabilityStatus="
				+ availabilityStatus + ", category=" + category + ", department=" + department + "]";
	}
    
}
