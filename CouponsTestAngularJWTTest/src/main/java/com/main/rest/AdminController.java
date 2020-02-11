package com.main.rest;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.beans.Company;
import com.main.beans.Coupon;
import com.main.beans.Customer;
import com.main.beans.UserDetailsImpl;
import com.main.service.AdminService;

@CrossOrigin("*")
@RestController
@RequestMapping("admin")
//@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	@Autowired
	AdminService adminService;
	
	@GetMapping()
	public ResponseEntity<?> getAdminDetails() {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return adminService.getOneCompany(userDetailsImpl.getId());
	}

	@PutMapping()
	public ResponseEntity<?> updateAdmin(@Valid @RequestBody Company company) throws ResourceNotFoundException {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return adminService.updateCompany(company, userDetailsImpl.getId());
	}
	
	@GetMapping("company")
	public List<Company> getAllCompanies() {
		return adminService.getAllCompanies();
	}
	
	@GetMapping("company/{id}")
	public ResponseEntity<?> getCompany(@PathVariable(name = "id") long companyID) {
		return adminService.getOneCompany(companyID);
	}

	@PostMapping("company")
	public ResponseEntity<?> addCompany(@RequestBody Company company) {
		return adminService.addCompany(company);
	}

	@PutMapping("company/{id}")
	public ResponseEntity<?> updateCompany(@PathVariable(name = "id") long companyId,
			@Valid @RequestBody Company company) throws ResourceNotFoundException {
		return adminService.updateCompany(company, companyId);
	}

	@DeleteMapping("company/{id}")
	public Map<String, Boolean> deleteCompany(@PathVariable(name = "id") long companyId)
			throws ResourceNotFoundException {
		return adminService.deleteCompany(companyId);
	}

	@GetMapping("coupons")
	public List<Coupon> getAllCoupons() {
		return adminService.getAllCoupons();
	}
	
	@GetMapping("coupon/{id}")
	public Coupon getCoupon(@PathVariable(name = "id") long id) {
		return adminService.getCoupon(id);
	}

	@GetMapping("customer")
	public List<Customer> getAllCustomers() {
		return adminService.getAllCustomers();
	}

	@GetMapping("customer/{id}")
	public ResponseEntity<?> getCustomer(@PathVariable(name = "id") long customerId)  {
		return adminService.getOneCustomer(customerId);
	}

	@PostMapping("customer")
	public ResponseEntity<?> addCustomer(@RequestBody Customer customer) {
		return adminService.addCustomer(customer);
	}

	@PutMapping("customer/{id}")
	public ResponseEntity<?> updateCustomer(@PathVariable(name = "id") long customerId,
			@Valid @RequestBody Customer customer) throws ResourceNotFoundException {
		return adminService.updateCustomer(customer, customerId);
	}

	@DeleteMapping("customer/{id}")

	public Map<String, Boolean> deleteCustomer(@PathVariable(name = "id") long customerId)
			throws ResourceNotFoundException {
		return adminService.deleteCustomer(customerId);
	}

}
