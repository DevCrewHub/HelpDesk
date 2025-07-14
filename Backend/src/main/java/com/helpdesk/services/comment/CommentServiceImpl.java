package com.helpdesk.services.comment;

import com.helpdesk.dto.CommentDto;
import com.helpdesk.entities.Comment;
import com.helpdesk.entities.Ticket;
import com.helpdesk.entities.User;
import com.helpdesk.enums.UserRole;
import com.helpdesk.repositories.CommentRepository;
import com.helpdesk.repositories.TicketRepository;
import com.helpdesk.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final TicketRepository ticketRepository;
    private final JwtUtil jwtUtil;
    private final CommentRepository commentRepository;

    // ✅ POST comment: Only assigned agent or ticket's customer can post
    @Override
    public CommentDto createComment(Long ticketId, String body) {
        User currentUser = jwtUtil.getLoggedInUser();
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);

        if (currentUser != null && optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();

            boolean isCustomer = currentUser.getUserRole() == UserRole.CUSTOMER &&
                    ticket.getCustomer().getId().equals(currentUser.getId());

            boolean isAssignedAgent = currentUser.getUserRole() == UserRole.AGENT &&
                    ticket.getAssignedAgent() != null &&
                    ticket.getAssignedAgent().getId().equals(currentUser.getId());

            if (isCustomer || isAssignedAgent) {
                Comment comment = new Comment();
                comment.setBody(body);
                comment.setCreatedAt(new Date());
                comment.setTicket(ticket);
                comment.setUser(currentUser);
                return commentRepository.save(comment).getCommentDto();
            } else {
                throw new RuntimeException("User is not authorized to comment on this ticket.");
            }
        }

        throw new RuntimeException("User or Ticket not found.");
    }

    // ✅ GET comments: Anyone (customer, agent, admin) can view comments
    @Override
    public List<CommentDto> getCommentsByTicketId(Long ticketId) {
        User currentUser = jwtUtil.getLoggedInUser();
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);

        if (currentUser != null && optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();

            boolean isCustomer = currentUser.getUserRole() == UserRole.CUSTOMER &&
                    ticket.getCustomer().getId().equals(currentUser.getId());

            boolean isAssignedAgent = currentUser.getUserRole() == UserRole.AGENT &&
                    ticket.getAssignedAgent() != null &&
                    ticket.getAssignedAgent().getId().equals(currentUser.getId());

            boolean isAgent = currentUser.getUserRole() == UserRole.AGENT;

            boolean isAdmin = currentUser.getUserRole() == UserRole.ADMIN;

            if (isCustomer || isAssignedAgent || isAgent || isAdmin) {
                return commentRepository.findAllByTicketId(ticketId).stream()
                        .map(Comment::getCommentDto)
                        .collect(Collectors.toList());
            } else {
                throw new RuntimeException("User is not authorized to view comments on this ticket.");
            }
        }

        throw new RuntimeException("User or Ticket not found.");
    }
}