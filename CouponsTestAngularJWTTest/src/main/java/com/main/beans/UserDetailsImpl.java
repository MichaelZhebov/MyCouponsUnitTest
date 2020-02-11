package com.main.beans;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String username; // email
	private String password;
	private String fullName;
	private boolean isActive;
	private String role;
	private List<GrantedAuthority> authorities;

	public UserDetailsImpl(Customer customer) {
		this.id = customer.getId();
		this.username = customer.getEmail();
		this.password = customer.getPassword();
		this.isActive = customer.isActive();
		this.fullName = customer.getFullName();
		this.role = customer.getRole();
		this.authorities = Arrays.stream(customer.getRole().split(",")).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	public UserDetailsImpl(Company company) {
		this.id = company.getId();
		this.username = company.getEmail();
		this.password = company.getPassword();
		this.isActive = company.isActive();
		this.fullName = company.getFullName();
		this.role = company.getRole();
		this.authorities = Arrays.stream(company.getRole().split(",")).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	public UserDetailsImpl() {
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
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
		return isActive;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthorities(List<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "UserDetailsImpl [id=" + id + ", username=" + username + ", password=" + password + ", fullName="
				+ fullName + ", isActive=" + isActive + ", authorities=" + authorities + "]";
	}

}
