package com.helpdesk.services.admin;

import java.util.List;

import com.helpdesk.dto.TicketDto;
import com.helpdesk.dto.UserDto;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.Status;

public interface AdminService {

	List<UserDto> getCustomers();
	List<UserDto> getAgents();
	
	List<UserDto> searchCustomersByUsername(String username);
	List<UserDto> searchAgentsByUsername(String username);
	
	List<TicketDto> getAllTickets();
	TicketDto getTicketById(Long id);
	
	TicketDto assignTicket(Long ticketId, Long agentId);
	
	List<TicketDto> searchTicketByTitle(String title);
	
	List<TicketDto> filterTicketsByPriority(Priority priority);	
	List<TicketDto> filterTicketsByStatus(Status status);
	List<TicketDto> filterTicketsByDepartmentName(String name);
	
	void deleteCustomer(Long customerId);
	void deleteAgent(Long agentId);

}
