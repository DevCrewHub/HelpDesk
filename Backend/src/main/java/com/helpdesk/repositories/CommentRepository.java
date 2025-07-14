package com.helpdesk.repositories;


import com.helpdesk.entities.Comment;
import com.helpdesk.entities.Ticket;
import com.helpdesk.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByTicketId(Long ticketId);
    
    List<Comment> findByUser(User user);
    
    List<Comment> findByTicket(Ticket ticket);
}