package com.helpdesk.services.customer;

import java.util.List;

import com.helpdesk.dto.DepartmentDto;
import com.helpdesk.dto.TicketDto;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.Status;

public interface CustomerService {

	TicketDto createTicket(TicketDto ticketDto);
	
	List<TicketDto> getAllTicketsCreated();
	
	void deleteTicket(Long id);

	TicketDto updateTicketStatus(Long ticketId, Status newStatus);
	
	List<TicketDto> searchTicketByTitle(String title);
	
	TicketDto getTicketById(Long id);
	
	List<DepartmentDto> getAllDepartments();
	
	List<TicketDto> filterTicketsByPriority(Priority priority);	
	List<TicketDto> filterTicketsByStatus(Status status);
	List<TicketDto> filterTicketsByDepartmentName(String name);

}
