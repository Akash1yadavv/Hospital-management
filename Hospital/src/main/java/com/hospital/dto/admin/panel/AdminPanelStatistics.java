/**
 * 
 */
package com.hospital.dto.admin.panel;

import lombok.Data;

/**
 * @author ankit
 */

@Data
public class AdminPanelStatistics {
	private UserStatisticsData userStatisticsData;
	private OrderStatistics orderStatistics;
	private InvoiceStatistics invoiceStatistics;
	private SubscriberStatistics subscriberStatistics;
	private RevenueStatistics revenueStatistics;
}
