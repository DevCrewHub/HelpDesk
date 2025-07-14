package com.helpdesk.dto;

import java.util.Date;
import java.util.List;

import com.helpdesk.enums.Priority;
import com.helpdesk.enums.Status;

import lombok.Data;

@Data
public class TicketDto {

	private Long id;

	private String title;

	private String description;
	
	// Auto-set
	private Date createdDate;

	private Priority priority;

	private Status status;

	// Auto-set from logged-in user, not required in request
	private Long customerId;

	// Auto-set from logged-in user, not required in request
	private String customerName;

	private Long agentId;

	private String agentName;

	private Long departmentId;

	private String departmentName;

	private List<CommentDto> comments;


}