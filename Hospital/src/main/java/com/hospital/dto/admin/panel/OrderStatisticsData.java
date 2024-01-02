package com.hospital.dto.admin.panel;

import lombok.Data;

@Data
public class OrderStatisticsData {
	private Long noOrderCreated;
	private Long noPaid;
	private Long noUnpaid;
	private Double revenue;
}
