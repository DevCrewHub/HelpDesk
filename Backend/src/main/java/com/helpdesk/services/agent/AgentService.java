package com.helpdesk.services.agent;

import java.util.List;

import com.helpdesk.dto.DepartmentDto;
import com.helpdesk.dto.TicketDto;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.Status;

public interface AgentService {
	
    List<TicketDto> getAssignedTickets();
    TicketDto getAssignedTicketById(Long id);    
    List<TicketDto> searchAssignedTicketByTitle(String title);    
    List<TicketDto> filterAssignedTicketsByPriority(Priority priority);
    List<TicketDto> filterAssignedTicketsByStatus(Status status);
    List<TicketDto> filterAssignedTicketsByDepartmentName(String name);
    TicketDto updatAssignedTicketePriority(Long ticketId, Priority priority);
    TicketDto updateAssignedTicketStatus(Long ticketId, Status newStatus);
	
	List<TicketDto> getAllTickets();
    TicketDto getTicketById(Long id);    
    List<TicketDto> searchTicketByTitle(String title);    
    List<TicketDto> filterTicketsByPriority(Priority priority);
    List<TicketDto> filterTicketsByStatus(Status status);
    List<TicketDto> filterTicketsByDepartmentName(String name);
    TicketDto assignTicketToMe(Long ticketId);
    
    List<DepartmentDto> getAllDepartments();
     
}
