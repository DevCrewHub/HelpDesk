package com.helpdesk.dto;

import com.helpdesk.enums.UserRole;

import lombok.Data;

@Data
public class UserDto {

	private Long id;

	private String userName;

	private String email;

	private String password;

	private String fullName;

	private String phoneNumber;

	private UserRole userRole;

	private Long departmentId;

	private String departmentName;

}
