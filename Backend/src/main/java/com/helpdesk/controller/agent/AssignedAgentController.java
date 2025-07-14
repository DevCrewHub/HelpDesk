package com.helpdesk.controller.agent;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.helpdesk.dto.TicketDto;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.Status;
import com.helpdesk.services.agent.AgentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/agent/assigned")
@RequiredArgsConstructor
public class AssignedAgentController {

    private final AgentService agentService;

    @GetMapping("/tickets")
    public ResponseEntity<?> getAssignedTickets() {
        return ResponseEntity.ok(agentService.getAssignedTickets());
    }
    
    @GetMapping("/ticket/{id}")
    public ResponseEntity<TicketDto> getAssignedTicketById(@PathVariable Long id) {
    	TicketDto ticket = agentService.getAssignedTicketById(id);
        if (ticket == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(ticket);
    }
    
    @GetMapping("/tickets/search/{title}")
    public ResponseEntity<List<TicketDto>> searchAssignedTicketByTitle(@PathVariable String title) {
        return ResponseEntity.ok(agentService.searchAssignedTicketByTitle(title));
    }
    
    @GetMapping("/tickets/priority/{priority}")
    public ResponseEntity<List<TicketDto>> filterAssignedTicketsByPriority(@PathVariable String priority) {
        try {
            Priority priorityEnum = Priority.valueOf(priority.toUpperCase());
            List<TicketDto> tickets = agentService.filterAssignedTicketsByPriority(priorityEnum);
            return ResponseEntity.ok(tickets);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/tickets/status/{status}")
    public ResponseEntity<List<TicketDto>> filterAssignedTicketsByStatus(@PathVariable String status) {
        try {
            Status statusEnum = Status.valueOf(status.toUpperCase());
            List<TicketDto> tickets = agentService.filterAssignedTicketsByStatus(statusEnum);
            return ResponseEntity.ok(tickets);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/tickets/department/{name}")
    public ResponseEntity<List<TicketDto>> filterAssignedTicketsByDepartmentName(@PathVariable String name) {
    	List<TicketDto> tickets = agentService.filterAssignedTicketsByDepartmentName(name);
        return ResponseEntity.ok(tickets);
    }
    
    @PutMapping("/tickets/{ticketId}/priority")
    public ResponseEntity<?> updatAssignedTicketePriority(@PathVariable Long ticketId, @RequestParam String priority) {
    	try {
            Priority priorityEnum = Priority.valueOf(priority.toUpperCase());
            TicketDto updatedTicket = agentService.updatAssignedTicketePriority(ticketId, priorityEnum);
            return ResponseEntity.ok(updatedTicket);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid priority: " + priority);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/tickets/{ticketId}/status")
    public ResponseEntity<?> updateAssignedTicketStatus(@PathVariable Long ticketId, @RequestParam String status) {
        try {
            Status newStatus = Status.valueOf(status.toUpperCase());
            TicketDto updatedTicket = agentService.updateAssignedTicketStatus(ticketId, newStatus);
            return ResponseEntity.ok(updatedTicket);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status: " + status);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/departments")
	public ResponseEntity<?> getAllDepartments() {
		return ResponseEntity.ok(agentService.getAllDepartments());
	}
    
}
