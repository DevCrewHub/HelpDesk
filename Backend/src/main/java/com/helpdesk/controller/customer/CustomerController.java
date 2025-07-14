package com.helpdesk.controller.customer;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.helpdesk.dto.TicketDto;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.Status;
import com.helpdesk.services.customer.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class CustomerController {

	private final CustomerService customerService;

	@PostMapping("/ticket")
	public ResponseEntity<?> createTicket(@RequestBody TicketDto ticketDto) {
		TicketDto createTicketDto = customerService.createTicket(ticketDto);
		if (createTicketDto == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(Map.of("error", "Failed to create ticket", "message", "Ticket creation failed"));
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(createTicketDto);
	}
	
	@GetMapping("/ticketsCreated")
    public ResponseEntity<?> getAllTicketsCreated() {
		return ResponseEntity.ok(customerService.getAllTicketsCreated());
    }
	
	@DeleteMapping("/ticket/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
		customerService.deleteTicket(id);
        return ResponseEntity.ok(null);
    }
    
    @GetMapping("/tickets/search/{title}")
    public ResponseEntity<?> searchTicketsByTitle(@PathVariable String title) {
        return ResponseEntity.ok(customerService.searchTicketByTitle(title));
    }
    
    @GetMapping("/ticket/{id}")
    public ResponseEntity<?> getTicketById(@PathVariable Long id) {
    	TicketDto ticket = customerService.getTicketById(id);
        if (ticket == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Ticket not found", "message", "No ticket found with ID: " + id));
        }
        return ResponseEntity.ok(ticket);
    }
    
    @PutMapping("/tickets/{ticketId}/status")
    public ResponseEntity<?> updateTicketStatus(@PathVariable Long ticketId, @RequestParam String status) {
        try {
            Status newStatus = Status.valueOf(status.toUpperCase());
            TicketDto updatedTicket = customerService.updateTicketStatus(ticketId, newStatus);
            return ResponseEntity.ok(updatedTicket);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Invalid status", "message", "Status '" + status + "' is not valid. Valid statuses are: " + 
                    String.join(", ", java.util.Arrays.stream(Status.values()).map(Enum::name).toArray(String[]::new))));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Update failed", "message", e.getMessage()));
        }
    }
    
    @GetMapping("/departments")
    public ResponseEntity<?> getAllDepartments() {
        return ResponseEntity.ok(customerService.getAllDepartments());
    }
    
    @GetMapping("/tickets/priority/{priority}")
    public ResponseEntity<List<TicketDto>> filterTicketsByPriority(@PathVariable String priority) {
        try {
            Priority priorityEnum = Priority.valueOf(priority.toUpperCase());
            List<TicketDto> tickets = customerService.filterTicketsByPriority(priorityEnum);
            return ResponseEntity.ok(tickets);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/tickets/status/{status}")
    public ResponseEntity<List<TicketDto>> filterTicketsByStatus(@PathVariable String status) {
        try {
            Status statusEnum = Status.valueOf(status.toUpperCase());
            List<TicketDto> tickets = customerService.filterTicketsByStatus(statusEnum);
            return ResponseEntity.ok(tickets);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/tickets/department/{name}")
    public ResponseEntity<List<TicketDto>> filterTicketsByDepartmentName(@PathVariable String name) {
        return ResponseEntity.ok(customerService.filterTicketsByDepartmentName(name));
    }

}