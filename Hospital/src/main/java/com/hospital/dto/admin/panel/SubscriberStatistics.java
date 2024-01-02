package com.hospital.dto.admin.panel;

import lombok.Data;

@Data
public class SubscriberStatistics {
	private Long noSubscribers;
	private Long noActiveSubscribers;
	private Long noDeactiveSubscribers;
}
