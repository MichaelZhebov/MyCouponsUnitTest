package com.main.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "companies")
public class Company {

	private long id;
	private String fullName;
	private String email;
	private String password;
	private Collection<Coupon> coupons = new ArrayList<Coupon>();
	private String role;
	private boolean isActive;

	public Company() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Column
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Column
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Transient
	public Collection<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Collection<Coupon> coupons) {
		this.coupons = coupons;
	}
	
	public void addCoupon(Coupon coupon) {
		this.coupons.add(coupon);
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", fullName=" + fullName + ", email=" + email + ", password=" + password
				+ ", coupons=" + coupons + ", role=" + role + ", isActive=" + isActive + "]";
	}

	

}
