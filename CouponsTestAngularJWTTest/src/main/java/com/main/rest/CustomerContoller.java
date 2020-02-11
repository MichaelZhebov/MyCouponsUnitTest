package com.main.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.beans.Coupon;
import com.main.beans.Customer;
import com.main.beans.UserDetailsImpl;
import com.main.service.CustomerService;

@CrossOrigin("*")
@RestController
@RequestMapping("customer")
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerContoller {
	
	@Autowired
	CustomerService customerService;
	
	@GetMapping()
	public ResponseEntity<?> getCustomerDetails() {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return customerService.getCustomerById(userDetailsImpl.getId());
	}
	
	@PutMapping()
	public ResponseEntity<?> updateCustomer(@RequestBody Customer customer) {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return customerService.updateCustomer(customer, userDetailsImpl.getId());
	}
	
	@GetMapping("buy/{id}")
	public ResponseEntity<?> purchaseCoupon(@PathVariable(name = "id") Long couponID) {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return customerService.purchaseCoupon(couponID, userDetailsImpl.getId());
	}
	
	@GetMapping("use/{id}")
	public ResponseEntity<?> useCoupon(@PathVariable(name = "id") long couponId) {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return customerService.useCoupon(couponId, userDetailsImpl.getId());
	}
	
	@GetMapping("coupons")
	public List<Coupon> getAllCoupons() {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return customerService.getAllCoupons(userDetailsImpl.getId());
	}
	
	@GetMapping("coupon/{id}")
	public Coupon getCoupon(@PathVariable(name = "id") long id) {
		return customerService.getCoupon(id);
	}
	
	@GetMapping("myCoupons")
	public List<Coupon> getCustomerCoupons() {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return customerService.getCustomerCoupons(userDetailsImpl.getId());
	}

//	@GetMapping("myCouponsCat/{category}")
//	public List<Coupon> getCustomerCoupons(@PathVariable String category) {
//		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		return customerService.getCustomerCoupons(userDetailsImpl.getId(), Category.valueOf(category));
//	}
//
//	@GetMapping("myCouponsPrice/{maxPrice}")
//	public List<Coupon> getCustomerCoupons(@PathVariable double maxPrice) {
//		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		return customerService.getCustomerCoupons(userDetailsImpl.getId(), maxPrice);
//	}
}
