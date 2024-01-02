package com.hospital.dto.admin.panel;

import lombok.Data;

@Data
public class InvoiceStatisticsData {
	private Long noInvoiceCreated;
	private Long noPaid;
	private Long noUnpaid;
	private Double revenue;
}
