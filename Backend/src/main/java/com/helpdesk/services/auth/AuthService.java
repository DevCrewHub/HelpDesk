package com.helpdesk.services.auth;

import com.helpdesk.dto.SignupRequest;
import com.helpdesk.dto.UserDto;

public interface AuthService {

	UserDto signupUser(SignupRequest signupRequest);
	UserDto signupAgent(SignupRequest signupRequest);

	boolean hasUserWithUsername(String username);

	boolean hasUserWithEmail(String email);

}
