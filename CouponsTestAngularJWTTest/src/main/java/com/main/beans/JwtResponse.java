package com.main.beans;

public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private String email;
	private String role;
	private long id;
	private String fullName;

	public JwtResponse(String accessToken, String email, String role, long id, String fullName) {
		this.token = accessToken;
		this.email = email;
		this.role = role;
		this.id = id;
		this.fullName = fullName;
	}

	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public long getId() {
		return id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}
