package com.helpdesk.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.helpdesk.entities.User;
import com.helpdesk.enums.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUserName(String username);

	Optional<User> findFirstByEmail(String email);

	Optional<User> findByUserRole(UserRole userRole);
	
	List<User> findByUserRoleAndUserNameContaining(UserRole userRole, String username);

}
