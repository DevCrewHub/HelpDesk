package com.helpdesk.entities;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.helpdesk.dto.TicketDto;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.Status;

import lombok.Data;
import java.util.stream.Collectors;




@Data
@Entity
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String description;

	@Column(name = "created_date",nullable = false)
	private Date createdDate;

	@Column(nullable = false)
	private Priority priority;

	@Column(nullable = false)
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "customer_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private User customer;

	@ManyToOne
	@JoinColumn(name = "assigned_agent_id")
	private User assignedAgent;

	@ManyToOne
	@JoinColumn(name = "department_id", nullable = false)
	private Department department;

	public TicketDto getTicketDto() {
		TicketDto ticketDto = new TicketDto();
		ticketDto.setId(id);
		ticketDto.setTitle(title);
		ticketDto.setDescription(description);

		ticketDto.setCustomerId(customer.getId());
		ticketDto.setCustomerName(customer.getUsername());

		if (assignedAgent != null) {
			ticketDto.setAgentId(assignedAgent.getId());
			ticketDto.setAgentName(assignedAgent.getUsername());
		}

		ticketDto.setDepartmentId(department.getId());
		ticketDto.setDepartmentName(department.getName());
		ticketDto.setStatus(status);
		ticketDto.setCreatedDate(createdDate);
		ticketDto.setPriority(priority);

		//  <<<<< ADD THIS BLOCK >>>>>
		// This maps the list of Comment entities to a list of CommentDto objects
		if (comments != null) {
			ticketDto.setComments(comments.stream().map(Comment::getCommentDto).collect(Collectors.toList()));
		}

		return ticketDto;
	}

	@OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
	private List<Comment> comments;


}
