package com.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.helpdesk.entities.Ticket;
import com.helpdesk.entities.User;
import com.helpdesk.enums.Priority;
import com.helpdesk.enums.Status;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

	//customer
    List<Ticket> findByCustomer(User customer);
    List<Ticket> findByCustomerAndTitleContaining(User customer, String title);
    Ticket findTicketByCustomerAndId(User assignedAgent, Long id);
    List<Ticket> findByCustomerAndPriority(User customer, Priority priority);
    List<Ticket> findByCustomerAndStatus(User customer, Status status);
    List<Ticket> findByCustomerAndDepartmentName(User customer, String name);
    
    //admin
    List<Ticket> findAllByTitleContaining(String title);
    List<Ticket> findByPriority(Priority priority);    
    List<Ticket> findByStatus(Status Status);
    List<Ticket> findByDepartmentName(String name);

    //agent
    List<Ticket> findByAssignedAgent(User assignedAgent);
    Ticket findTicketByAssignedAgentAndId(User assignedAgent, Long id);
    List<Ticket> findByAssignedAgentAndTitleContaining(User assignedAgent, String title);
    List<Ticket> findByAssignedAgentAndPriority(User assignedAgentr, Priority priority);
    List<Ticket> findByAssignedAgentAndStatus(User assignedAgentr, Status status);
    List<Ticket> findByAssignedAgentAndDepartment_NameContaining(User agent, String departmentName);  

}
