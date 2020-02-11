package com.main.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.beans.Company;
import com.main.beans.Customer;
import com.main.beans.JwtResponse;
import com.main.beans.UserDetailsImpl;
import com.main.config.JwtProvider;
import com.main.service.AdminService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtProvider jwtTokenProvider;

	@Autowired
	private AdminService adminService;

	@SuppressWarnings("rawtypes")
	@PostMapping("/login")
	public ResponseEntity login(@RequestBody AuthBody data) throws Exception {
		if (!data.getEmail().matches(
				"^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$")) {
			return new ResponseEntity<String>("Email is not valid!", HttpStatus.BAD_REQUEST);
		}

		Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtTokenProvider.generateJwtToken(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getRole(),
				userDetails.getId(), userDetails.getFullName()));
	}

	@SuppressWarnings("rawtypes")
	@PostMapping("/register/company")
	public ResponseEntity register(@RequestBody Company company) {
		if (!company.getEmail().matches(
				"^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$")) {
			return new ResponseEntity<String>("Email is not valid!", HttpStatus.BAD_REQUEST);
		}
		return adminService.addCompany(company);
	}

	@SuppressWarnings("rawtypes")
	@PostMapping("/register/customer")
	public ResponseEntity register(@RequestBody Customer customer) {
		if (!customer.getEmail().matches(
				"^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$")) {
			return new ResponseEntity<String>("Email is not valid!", HttpStatus.BAD_REQUEST);
		}
		return adminService.addCustomer(customer);
	}

}
