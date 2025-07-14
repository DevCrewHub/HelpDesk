package com.helpdesk.dto;

import com.helpdesk.enums.UserRole;
import lombok.Data;

@Data
public class AuthenticationResponse {

	private String jwt;

	private long userId;

	private UserRole userRole;

}
