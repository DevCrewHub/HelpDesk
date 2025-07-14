package com.helpdesk.services.admin;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.helpdesk.dto.TicketDto;
import com.helpdesk.dto.UserDto;
import com.helpdesk.entities.Comment;
import com.helpdesk.entities.Ticket;
import com.helpdesk.entities.User;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.Status;
import com.helpdesk.enums.UserRole;
import com.helpdesk.repositories.CommentRepository;
import com.helpdesk.repositories.TicketRepository;
import com.helpdesk.repositories.UserRepository;
//import com.helpdesk.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final UserRepository userRepository;
//	private final JwtUtil jwtUtil;
	private final TicketRepository ticketRepository;
	private final CommentRepository commentRepository;

	@Override
	public List<UserDto> getCustomers() {
		return userRepository.findAll().stream().filter(user -> {
			UserRole role = user.getUserRole();
			return role == UserRole.CUSTOMER;
		}).map(User::getUserDto).collect(Collectors.toList());
	}
	
	@Override
	public List<UserDto> getAgents() {
		return userRepository.findAll().stream().filter(user -> {
			UserRole role = user.getUserRole();
			return role == UserRole.AGENT;
		}).map(User::getUserDto).collect(Collectors.toList());
	}
	
	@Override
	public List<UserDto> searchCustomersByUsername(String username) {
		return userRepository.findByUserRoleAndUserNameContaining(UserRole.CUSTOMER, username)
				.stream()
				.map(User::getUserDto)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<UserDto> searchAgentsByUsername(String username) {
		return userRepository.findByUserRoleAndUserNameContaining(UserRole.AGENT, username)
				.stream()
				.map(User::getUserDto)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<TicketDto> getAllTickets() {
		return ticketRepository.findAll()
				.stream()
				.sorted(Comparator.comparing(Ticket::getCreatedDate).reversed())
				.map(Ticket::getTicketDto)
				.collect(Collectors.toList());
	}
	
	@Override
	public TicketDto getTicketById(Long id) {
		Optional<Ticket> optionalTicket = ticketRepository.findById(id);
		return optionalTicket.map(Ticket::getTicketDto).orElse(null);
	}
	
	@Override
    public TicketDto assignTicket(Long ticketId, Long agentId) {
//        User admin = jwtUtil.getLoggedInUser();
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        Optional<User> optionalAgent = userRepository.findById(agentId);

        if (optionalTicket.isPresent() && optionalAgent.isPresent() && optionalAgent.get().getUserRole() == UserRole.AGENT) {
            Ticket ticket = optionalTicket.get();
            User agent = optionalAgent.get();

            if (ticket.getDepartment().equals(agent.getDepartment())) {
                ticket.setAssignedAgent(agent);
                ticket.setStatus(Status.INPROGRESS);
                return ticketRepository.save(ticket).getTicketDto();
            } else {
                throw new RuntimeException("Agent's department does not match the ticket's department.");
            }
        }
        throw new RuntimeException("Ticket or Agent not found or invalid role.");
    }
	
	@Override
	public List<TicketDto> searchTicketByTitle(String title) {
		return ticketRepository.findAllByTitleContaining(title)
				.stream()
				.sorted(Comparator.comparing(Ticket::getCreatedDate).reversed())
				.map(Ticket::getTicketDto)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<TicketDto> filterTicketsByPriority(Priority priority) {
		return ticketRepository.findByPriority(priority)
				.stream()
				.sorted(Comparator.comparing(Ticket::getCreatedDate).reversed())
				.map(Ticket::getTicketDto)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<TicketDto> filterTicketsByStatus(Status status) {
		return ticketRepository.findByStatus(status)
				.stream()
				.sorted(Comparator.comparing(Ticket::getCreatedDate).reversed())
				.map(Ticket::getTicketDto)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<TicketDto> filterTicketsByDepartmentName(String name) {
		return ticketRepository.findByDepartmentName(name)
				.stream()
				.sorted(Comparator.comparing(Ticket::getCreatedDate).reversed())
				.map(Ticket::getTicketDto)
				.collect(Collectors.toList());
	}
	
	@Override
	public void deleteCustomer(Long customerId) {
		Optional<User> optionalUser = userRepository.findById(customerId);
		if (optionalUser.isEmpty()) {
			throw new RuntimeException("Customer not found");
		}
		
		User user = optionalUser.get();
		if (user.getUserRole() != UserRole.CUSTOMER) {
			throw new RuntimeException("User is not a customer");
		}
		
		// Get all tickets created by this customer
		List<Ticket> customerTickets = ticketRepository.findByCustomer(user);
		
		// Delete all comments on customer's tickets first
		for (Ticket ticket : customerTickets) {
			List<Comment> ticketComments = commentRepository.findByTicket(ticket);
			commentRepository.deleteAll(ticketComments);
		}
		
		// Delete all tickets created by this customer
		ticketRepository.deleteAll(customerTickets);
		
		// Then delete the customer account
		userRepository.deleteById(customerId);
	}
	
	@Override
	public void deleteAgent(Long agentId) {
		Optional<User> optionalUser = userRepository.findById(agentId);
		if (optionalUser.isEmpty()) {
			throw new RuntimeException("Agent not found");
		}
		
		User user = optionalUser.get();
		if (user.getUserRole() != UserRole.AGENT) {
			throw new RuntimeException("User is not an agent");
		}
		
		// Delete all comments made by this agent
		List<Comment> agentComments = commentRepository.findByUser(user);
		commentRepository.deleteAll(agentComments);
		
		// Unassign all tickets from this agent
		List<Ticket> assignedTickets = ticketRepository.findByAssignedAgent(user);
		if (!assignedTickets.isEmpty()) {
			assignedTickets.forEach(ticket -> {
				ticket.setAssignedAgent(null);
				if (ticket.getStatus() == Status.INPROGRESS) {
					ticket.setStatus(Status.PENDING);
				}
			});
			ticketRepository.saveAll(assignedTickets);
		}
		
		// Delete the agent account
		userRepository.deleteById(agentId);
	}
	
}