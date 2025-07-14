package com.helpdesk.entities;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.helpdesk.dto.UserDto;
import com.helpdesk.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Data;

@Data
@Entity
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="user_name", unique = true, nullable = false)
	private String userName;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(name = "full_name", nullable = false)
	private String fullName;

	@Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number")
	@Column(name = "phone_number", length = 20)
	private String phoneNumber;

	@Column(name = "user_role", nullable = false)
	private UserRole userRole;

	@ManyToOne
	@JoinColumn(name = "department_id")
	private Department department;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + this.userRole.name()));
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getUsername() {
		return this.userName;
	}

	public UserDto getUserDto() {
		UserDto userDto = new UserDto();
		userDto.setId(id);
		userDto.setUserName(userName);
		userDto.setEmail(email);
		userDto.setFullName(fullName);
		userDto.setPhoneNumber(phoneNumber);
		userDto.setUserRole(userRole);
		if (department != null) {
			userDto.setDepartmentId(department.getId());
			userDto.setDepartmentName(department.getName());
		}
		return userDto;
	}

}
