package com.main.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.main.beans.Company;
import com.main.beans.Coupon;
import com.main.beans.Customer;
import com.main.repo.CompanyRepository;
import com.main.repo.CouponRepository;
import com.main.repo.CustomerRepository;

@Service
public class CompanyService {

	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private CouponRepository couponRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private BCryptPasswordEncoder encoder;

	public ResponseEntity<?> addCoupon(Coupon coupon, long companyID) {
//		if (coupon.getStartDate().before(new Date())) {
//			return new ResponseEntity<String>("The coupon Start Date is not valid!", HttpStatus.BAD_REQUEST);
//		}
		if (coupon.getEndDate().before(coupon.getStartDate())) {
			return new ResponseEntity<String>("The coupon End Date is not valid!", HttpStatus.BAD_REQUEST);
		}
		if (coupon.getAmount() == null || coupon.getAmount() <= 0) {
			return new ResponseEntity<String>("The coupon Amount is not valid!", HttpStatus.BAD_REQUEST);
		}
		if (coupon.getPrice() == null || coupon.getPrice() <= 0) {
			return new ResponseEntity<String>("The coupon Price is not valid!", HttpStatus.BAD_REQUEST);
		}
		Optional<Company> company = companyRepository.findById(companyID);
		List<Coupon> coupons = couponRepository.findBycompanyId(companyID);
		for (Coupon coupon2 : coupons) {
			if (coupon2.getTitle().equalsIgnoreCase(coupon.getTitle())) {
				return new ResponseEntity<String>("The coupon with same Title is already exist!",
						HttpStatus.BAD_REQUEST);
			}
		}
		coupon.setCompanyId(companyID);
		coupon.setCompanyName(company.get().getFullName());
		if (coupon.getImage() == null) {
			coupon.setImage(
					"https://lh3.googleusercontent.com/1tOWHST-BBYRCZs1CC8JADKEattBCcS9CcbpEASWQzkCVb6MSOmTRTI36AmV9ZLRz95h6Hyc-8uu2_XrJJR_dymwlCq1Tlb2To6L9UogIRxpQu8y4jx_EJDq9psuwbjR2rdEL9O_qDL5-WzVQhoaoLr5xSo0xOecAV3EVOGOvhtHaKLpWLoQH4AY0jqMfPUx4g7_AyZjZUmB3U1pGSBvtBDqFsifozLzpMW_XTJUUx2M7fFCn59m2EgfDwVaYES-L0EreHEMmwMIWzKe5wQjYYG6-3zMsECm7nUsjnTooJIyATQG8dzkNhtXxOlNLVW2YiNc7-iCCmyCgpHxVum1HJh2KYAYcnf4ua0eSjjchNW-coBZ9RKDnwKGOG3K1aht4SzAns-TAviuxi0h-AtNIhPXTYrFfdlceWXabEGpZ2Kc0hZJma39IVYAqimiGHrKniPW00ECzfEfpYA7APfrfqg1AwsBCWfUqr_JCwZyA9iAlQAjSVNMJcMLxsiHByHkgZ-N22trBmP26IfHRMK0It6-_7j13LNegdkvv181SMRJVxQkCRJBWZluIXCe-DTKyGDmCgCEsA9Ui8rRONgj85eIDN8RSg1zx4qUL-bY3qQcBFBVKGW5tl75I-ANRjxwg_e9UgI4QqENccbTS_r4hjEs3vR7Tr2SjQ31ia9QKQO44wt-p_O88ug=w366-h142-no");
		}
		company.get().addCoupon(coupon);
		companyRepository.save(company.get());
		couponRepository.save(coupon);
		return new ResponseEntity<Coupon>(coupon, HttpStatus.OK);
	}

	public ResponseEntity<?> updateCompany(Company newCompany, long companyId) {
		if (!newCompany.getEmail().matches(
				"^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$")) {
			return new ResponseEntity<String>("Email is not valid!", HttpStatus.BAD_REQUEST);
		}
		if (newCompany.getPassword().length() < 4) {
			return new ResponseEntity<String>("Password must be at least 4 characters", HttpStatus.BAD_REQUEST);
		}
		Company company = companyRepository.findById(companyId).get();
		if (!newCompany.getEmail().equalsIgnoreCase(company.getEmail())) {
			Optional<Company> existByEmail = companyRepository.findByEmailIgnoreCase(newCompany.getEmail());
			Optional<Customer> existCustomer = customerRepository.findByEmailIgnoreCase(newCompany.getEmail());
			if (!existByEmail.isEmpty() || !existCustomer.isEmpty()) {
				return new ResponseEntity<String>("This email is already used!", HttpStatus.BAD_REQUEST);
			}
		}
		if (!newCompany.getFullName().equalsIgnoreCase(company.getFullName())) {
			Optional<Company> existByName = companyRepository.findByFullNameIgnoreCase(newCompany.getFullName());
			if (!existByName.isEmpty()) {
				return new ResponseEntity<String>("This name is already used!", HttpStatus.BAD_REQUEST);
			}
		}

		if (newCompany.getFullName() != null) {
			company.setFullName(newCompany.getFullName());
			List<Coupon> coupons = couponRepository.findBycompanyId(companyId);
			for (Coupon coupon : coupons) {
				coupon.setCompanyName(newCompany.getFullName());
			}
		}
		if (newCompany.getEmail() != null) {
			company.setEmail(newCompany.getEmail());
		}
		if (newCompany.getPassword() != null && !newCompany.getPassword().equals(company.getPassword())) {
			company.setPassword(encoder.encode(newCompany.getPassword()));
		}
		if (newCompany.isActive() != company.isActive()) {
			company.setActive(newCompany.isActive());
		}
		final Company updatedCompany = companyRepository.save(company);
		return ResponseEntity.ok(updatedCompany);
	}

	public Coupon getCoupon(long id) {
		return couponRepository.findById(id).get();
	}

	public ResponseEntity<?> updateCoupon(Coupon newCoupon, long couponID, long companyId) {
//		if (newCoupon.getStartDate().before(new Date())) {
//			return new ResponseEntity<String>("The coupon Start Date is not valid!", HttpStatus.BAD_REQUEST);
//		}
		if (newCoupon.getEndDate().before(newCoupon.getStartDate())) {
			return new ResponseEntity<String>("The coupon End Date is not valid!", HttpStatus.BAD_REQUEST);
		}
		if (newCoupon.getAmount() == null || newCoupon.getAmount() <= 0) {
			return new ResponseEntity<String>("The coupon Amount is not valid!", HttpStatus.BAD_REQUEST);
		}
		if (newCoupon.getPrice() == null || newCoupon.getPrice() <= 0) {
			return new ResponseEntity<String>("The coupon Price is not valid!", HttpStatus.BAD_REQUEST);
		}
		Coupon coupon = couponRepository.findById(couponID)
				.orElseThrow(() -> new ResourceNotFoundException("Coupon not found for this id : " + couponID));
		if (newCoupon.getTitle() != null) {
			Optional<Company> company = companyRepository.findById(companyId);
			for (Coupon c : company.get().getCoupons()) {
				if (c.getTitle() == newCoupon.getTitle()) {
					return new ResponseEntity<String>("The coupon with this title is already exist!",
							HttpStatus.BAD_REQUEST);
				}
			}
			coupon.setTitle(newCoupon.getTitle());
		}

		if (newCoupon.getAmount() != null) {
			coupon.setAmount(newCoupon.getAmount());
		}
		if (newCoupon.getCategory() != null) {
			coupon.setCategory(newCoupon.getCategory());
		}
		if (newCoupon.getDescription() != null) {
			coupon.setDescription(newCoupon.getDescription());
		}
		if (newCoupon.getStartDate() != null) {
			coupon.setStartDate(newCoupon.getStartDate());
		}
		if (newCoupon.getEndDate() != null) {
			coupon.setEndDate(newCoupon.getEndDate());
		}
		if (newCoupon.getImage() != null) {
			coupon.setImage(newCoupon.getImage());
		}
		if (newCoupon.getPrice() != null) {
			coupon.setPrice(newCoupon.getPrice());
		}

		final Coupon updatedCoupon = couponRepository.save(coupon);
		return ResponseEntity.ok(updatedCoupon);
	}

	public Map<String, Boolean> deleteCoupon(long couponID) {
		Coupon coupon = couponRepository.findById(couponID)
				.orElseThrow(() -> new ResourceNotFoundException("Company not found for this id : " + couponID));
		Customer customer = customerRepository.findByCouponsId(coupon.getId());
		if (customer != null) {
			customer.getCoupons().remove(coupon);
			customerRepository.save(customer);
		}
		couponRepository.delete(coupon);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	public List<Coupon> getCompanyCoupons(long id) {
//		List<Coupon> coupons = couponRepository.findBycompanyId(id);
//		coupons.stream().filter(c -> c.getEndDate().before(new Date())).map(c -> deleteCoupon(c.getId()))
//				.collect(Collectors.toList());
//		return coupons;
		return couponRepository.findBycompanyId(id);
	}

//	public List<Coupon> getCompanyCoupons(Long id, Category category) {
//		return couponRepository.findByCompanyIdAndCategory(id, category);
//	}
//
//	public List<Coupon> getCompanyCoupons(Long id, double maxPrice) {
//		return couponRepository.findByCompanyIdAndPriceLessThanEqual(id, maxPrice);
//	}

	public ResponseEntity<Company> getOneCompany(long companyID) {
		Company company = companyRepository.findById(companyID)
				.orElseThrow(() -> new ResourceNotFoundException("Company not found for this id : " + companyID));
		return ResponseEntity.ok(company);
	}

}
