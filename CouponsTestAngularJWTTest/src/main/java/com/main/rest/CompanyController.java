package com.main.rest;

import java.util.List;
import java.util.Map;

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
import com.main.beans.UserDetailsImpl;
import com.main.beans.Coupon.Category;
import com.main.service.CompanyService;

@CrossOrigin("*")
@RestController
@RequestMapping("company")
@PreAuthorize("hasRole('COMPANY')")
public class CompanyController {
	
	@Autowired
	private CompanyService companyService;
	
	@GetMapping()
	public ResponseEntity<?> getCompanyDetails() {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return companyService.getOneCompany(userDetailsImpl.getId());
	}
	
	@PutMapping()
	public ResponseEntity<?> updateCompany(@RequestBody Company company) {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return companyService.updateCompany(company, userDetailsImpl.getId());
	}
	
	@PostMapping("addCoupon")
	public ResponseEntity<?> addCoupon(@RequestBody Coupon coupon) {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return companyService.addCoupon(coupon, userDetailsImpl.getId());
	}
	
	@PutMapping("updateCoupon/{id}")
	public ResponseEntity<?> updateCoupon(@RequestBody Coupon coupon, @PathVariable(name = "id") long couponID) {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return companyService.updateCoupon(coupon, couponID, userDetailsImpl.getId());
	}
	
	@DeleteMapping("delete/{id}")
	public Map<String, Boolean> deleteCoupon(@PathVariable(name = "id") long couponID)
			throws ResourceNotFoundException {
		return companyService.deleteCoupon(couponID);
	}
	
	@GetMapping("coupon/{id}")
	public Coupon getCoupon(@PathVariable(name = "id") long id) {
		return companyService.getCoupon(id);
	}
	
	@GetMapping("coupons")
	public List<Coupon> getCompanyCoupons() {
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return companyService.getCompanyCoupons(userDetailsImpl.getId());
	}

//	@GetMapping("couponsCat/{category}")
//	public List<Coupon> getCompanyCoupons(@PathVariable String category) {
//		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		return companyService.getCompanyCoupons(userDetailsImpl.getId(), Category.valueOf(category));
//	}
//
//	@GetMapping("couponsPrice/{maxPrice}")
//	public List<Coupon> getCompanyCoupons(@PathVariable double maxPrice) {
//		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		return companyService.getCompanyCoupons(userDetailsImpl.getId(), maxPrice);
//	}

}
	