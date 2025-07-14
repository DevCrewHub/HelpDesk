package com.helpdesk.controller.auth;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.helpdesk.dto.AuthenticationRequest;
import com.helpdesk.dto.AuthenticationResponse;
import com.helpdesk.dto.SignupRequest;
import com.helpdesk.dto.UserDto;
import com.helpdesk.entities.User;
import com.helpdesk.repositories.UserRepository;
import com.helpdesk.services.auth.AuthService;
import com.helpdesk.services.jwt.UserService;
import com.helpdesk.utils.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

	private final AuthService authService;
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;
	private final UserService userService;
	private final AuthenticationManager authenticationManager;

	@PostMapping("/signup")
	public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
		log.info("Received signup request for username: {}", signupRequest.getUserName());
		if (authService.hasUserWithUsername(signupRequest.getUserName())) {
            log.warn("Signup failed: Username already exists - {}", signupRequest.getUserName());
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User already exists with this username");
		}
		if (authService.hasUserWithEmail(signupRequest.getEmail())) {
            log.warn("Signup failed: Email already exists - {}", signupRequest.getEmail());
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User already exists with this email");
		}
		UserDto createdUserDto = authService.signupUser(signupRequest);
		if (createdUserDto == null) {
            log.error("Signup failed for email: {}", signupRequest.getEmail());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not created");
		}
        log.info("User created successfully: {}", createdUserDto.getEmail());
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        log.info("Login attempt for user name: {}", authenticationRequest.getUserName());

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUserName(), authenticationRequest.getPassword()));
				} catch (BadCredentialsException e) {
             log.error("Login failed for user name: {}", authenticationRequest.getUserName());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
		}

		final UserDetails userDetails = userService.userDetailService()
				.loadUserByUsername(authenticationRequest.getUserName());
		Optional<User> optionalUser = userRepository.findByUserName(authenticationRequest.getUserName());

		final String jwtToken = jwtUtil.generateToken(userDetails);
		AuthenticationResponse authenticationResponse = new AuthenticationResponse();
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			authenticationResponse.setJwt(jwtToken);
			authenticationResponse.setUserId(user.getId());
			authenticationResponse.setUserRole(user.getUserRole());
            log.info("Login successful for user ID: {}, role: {}", user.getId(), user.getUserRole());
		} else {
            log.warn("User not found after successful authentication: {}", authenticationRequest.getUserName());
		}
		return ResponseEntity.ok(authenticationResponse);
	}

}
