package com.helpdesk.controller.agent;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.helpdesk.dto.TicketDto;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.Status;
import com.helpdesk.services.agent.AgentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/agent")
public class AgentController {
	
	private final AgentService agentService;
	
	@GetMapping("/tickets")
    public ResponseEntity<?> getAllTickets() {
        return ResponseEntity.ok(agentService.getAllTickets());
    }
	
	@GetMapping("/ticket/{id}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable Long id) {
    	TicketDto ticket = agentService.getTicketById(id);
        if (ticket == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(ticket);
    }
	
	@GetMapping("/tickets/search/{title}")
    public ResponseEntity<List<TicketDto>> searchTicket(@PathVariable String title) {
        return ResponseEntity.ok(agentService.searchTicketByTitle(title));
    }
    
    @GetMapping("/tickets/priority/{priority}")
    public ResponseEntity<List<TicketDto>> filterTicketsByPriority(@PathVariable String priority) {
        try {
            Priority priorityEnum = Priority.valueOf(priority.toUpperCase());
            List<TicketDto> tickets = agentService.filterTicketsByPriority(priorityEnum);
            return ResponseEntity.ok(tickets);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/tickets/status/{status}")
    public ResponseEntity<List<TicketDto>> filterTicketsByStatus(@PathVariable String status) {
        try {
        	Status statusEnum = Status.valueOf(status.toUpperCase());
            List<TicketDto> tickets = agentService.filterTicketsByStatus(statusEnum);
            return ResponseEntity.ok(tickets);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/tickets/department/{name}")
    public ResponseEntity<List<TicketDto>> filterTicketsByDepartmentName(@PathVariable String name) {
    	return ResponseEntity.ok(agentService.filterTicketsByDepartmentName(name));
    }
    
    @PutMapping("/tickets/{ticketId}/assign")
    public ResponseEntity<?> assignTicketToMe(@PathVariable Long ticketId) {
    	try {
            TicketDto updatedTicket = agentService.assignTicketToMe(ticketId);
            return ResponseEntity.ok(updatedTicket);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @GetMapping("/departments")
	public ResponseEntity<?> getAllDepartments() {
		return ResponseEntity.ok(agentService.getAllDepartments());
	}

}
