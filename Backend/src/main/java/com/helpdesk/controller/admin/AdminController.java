package com.helpdesk.controller.admin;

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

import com.helpdesk.dto.SignupRequest;
import com.helpdesk.dto.TicketDto;
import com.helpdesk.dto.UserDto;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.Status;
import com.helpdesk.services.admin.AdminService;
import com.helpdesk.services.auth.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
	
	private final AdminService adminService;
	private final AuthService authService;
	
	@PostMapping("/register")
	public ResponseEntity<?> signupAgent(@RequestBody SignupRequest signupRequest) {
		if (authService.hasUserWithUsername(signupRequest.getUserName())) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
				.body(Map.of("error", "Username exists", "message", "Agent already exists with this username"));
		}
		if (authService.hasUserWithEmail(signupRequest.getEmail())) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
				.body(Map.of("error", "Email exists", "message", "Agent already exists with this email"));
		}
		UserDto createdUserDto = authService.signupAgent(signupRequest);
		if (createdUserDto == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(Map.of("error", "Creation failed", "message", "Agent not created"));
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
	}
	
	// Get all customers
	@GetMapping("/customers")
	public ResponseEntity<?> getCustomers() {
		return ResponseEntity.ok(adminService.getCustomers());
	}
	
	// Search customers by username
	@GetMapping("/customers/search/{username}")
	public ResponseEntity<?> searchCustomersByUsername(@PathVariable String username) {
		return ResponseEntity.ok(adminService.searchCustomersByUsername(username));
	}
	// Search agents by username
	@GetMapping("/agents/search/{username}")
	public ResponseEntity<?> searchAgentsByUsername(@PathVariable String username) {
		return ResponseEntity.ok(adminService.searchAgentsByUsername(username));
	}
	
	// Get all agents
	@GetMapping("/agents")
	public ResponseEntity<?> getAgents() {
		return ResponseEntity.ok(adminService.getAgents());
	}
	
	@GetMapping("/tickets")
    public ResponseEntity<?> getAllTickets() {
        return ResponseEntity.ok(adminService.getAllTickets());
    }
	
	@GetMapping("/ticket/{id}")
    public ResponseEntity<?> getTicketById(@PathVariable Long id) {
    	TicketDto ticket = adminService.getTicketById(id);
        if (ticket == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Ticket not found", "message", "No ticket found with ID: " + id));
        }
        return ResponseEntity.ok(ticket);
    }
	
	@PutMapping("/tickets/{ticketId}/assign")
    public ResponseEntity<?> assignTicket(@PathVariable Long ticketId, @RequestParam Long agentId) {
        try {
            TicketDto updatedTicket = adminService.assignTicket(ticketId, agentId);
            return ResponseEntity.ok(updatedTicket);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Assignment failed", "message", e.getMessage()));
        }
    }
	
	@GetMapping("/tickets/search/{title}")
    public ResponseEntity<List<TicketDto>> searchTicket(@PathVariable String title) {
        return ResponseEntity.ok(adminService.searchTicketByTitle(title));
    }
    
    @GetMapping("/tickets/priority/{priority}")
    public ResponseEntity<?> filterTicketsByPriority(@PathVariable String priority) {
        try {
            Priority priorityEnum = Priority.valueOf(priority.toUpperCase());
            List<TicketDto> tickets = adminService.filterTicketsByPriority(priorityEnum);
            return ResponseEntity.ok(tickets);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Invalid priority", "message", "Priority '" + priority + "' is not valid. Valid priorities are: " + 
                    String.join(", ", java.util.Arrays.stream(Priority.values()).map(Enum::name).toArray(String[]::new))));
        }
    }
    
    @GetMapping("/tickets/status/{status}")
    public ResponseEntity<?> filterTicketsByStatus(@PathVariable String status) {
        try {
        	Status statusEnum = Status.valueOf(status.toUpperCase());
            List<TicketDto> tickets = adminService.filterTicketsByStatus(statusEnum);
            return ResponseEntity.ok(tickets);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Invalid status", "message", "Status '" + status + "' is not valid. Valid statuses are: " + 
                    String.join(", ", java.util.Arrays.stream(Status.values()).map(Enum::name).toArray(String[]::new))));
        }
    }
    
    @GetMapping("/tickets/department/{name}")
    public ResponseEntity<List<TicketDto>> filterTicketsByDepartmentName(@PathVariable String name) {
    	return ResponseEntity.ok(adminService.filterTicketsByDepartmentName(name));
    }
    
    @DeleteMapping("/customers/{customerId}")
	public ResponseEntity<?> deleteCustomer(@PathVariable Long customerId) {
		try {
			adminService.deleteCustomer(customerId);
			return ResponseEntity.ok(Map.of("message", "Customer and all their tickets deleted successfully"));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest()
				.body(Map.of("error", "Delete failed", "message", e.getMessage()));
		}
	}
	
	// Delete agent account
	@DeleteMapping("/agents/{agentId}")
	public ResponseEntity<?> deleteAgent(@PathVariable Long agentId) {
		try {
			adminService.deleteAgent(agentId);
			return ResponseEntity.ok(Map.of("message", "Agent deleted successfully. Their assigned tickets have been unassigned."));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest()
				.body(Map.of("error", "Delete failed", "message", e.getMessage()));
		}
	}

}
