package com.main.service;

import java.util.Date;
import java.util.List;
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
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private CouponRepository couponRepository;
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private BCryptPasswordEncoder encoder;

	public ResponseEntity<?> purchaseCoupon(Long couponsId, Long customerId) {
		Optional<Customer> customer = customerRepository.findById(customerId);
		Optional<Coupon> coupon = couponRepository.findById(couponsId);
		List<Coupon> customerCoupons = (List<Coupon>) customer.get().getCoupons();
		for (Coupon c : customerCoupons) {
			if (c.getId() == couponsId) {
				return new ResponseEntity<String>("Customer had already bought this coupon.", HttpStatus.BAD_REQUEST);
			}
		}
		if (coupon.get().getAmount() <= 0) {
			return new ResponseEntity<String>("Coupon amount is 0.", HttpStatus.BAD_REQUEST);
		}
		if (coupon.get().getEndDate().before(new Date())) {
			return new ResponseEntity<String>("Coupon expired.", HttpStatus.BAD_REQUEST);
		}
		customer.get().getCoupons().add(coupon.get());
		customerRepository.save(customer.get());
		coupon.get().setAmount(coupon.get().getAmount() - 1);
		couponRepository.save(coupon.get());
		return ResponseEntity.ok(coupon);
	}
	
	public ResponseEntity<?> useCoupon(long couponId, long customerId) {
		Optional<Customer> customer = customerRepository.findById(customerId);
		List<Coupon> coupons = (List<Coupon>) customer.get().getCoupons();
		coupons.removeIf(c -> c.getId() == couponId);
		customer.get().setCoupons(coupons);
		customerRepository.save(customer.get());
		return ResponseEntity.ok(customer);
	}
	
	public ResponseEntity<?> updateCustomer(Customer newCustomer, long customerId) {
		if (!newCustomer.getEmail().matches("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$")) {
			return new ResponseEntity<String>("Email is not valid!", HttpStatus.BAD_REQUEST);
		}
		if (newCustomer.getPassword().length() < 4) {
			return new ResponseEntity<String>("Password must be at least 4 characters", HttpStatus.BAD_REQUEST);
		}
		Customer customer = customerRepository.findById(customerId).get();
		if (!newCustomer.getEmail().equalsIgnoreCase(customer.getEmail())) {
			Optional<Company> existByEmail = companyRepository.findByEmailIgnoreCase(newCustomer.getEmail());
			if (!existByEmail.isEmpty()) {
				return new ResponseEntity<String>("This email is already used!", HttpStatus.BAD_REQUEST);
			}
		}
		if (newCustomer.getFullName() != null) {
			customer.setFullName(newCustomer.getFullName());
		}
		if (newCustomer.getEmail() != null) {
			customer.setEmail(newCustomer.getEmail());
		}
		if (newCustomer.getPassword() != null && !newCustomer.getPassword().equals(customer.getPassword())) {
			customer.setPassword(encoder.encode(newCustomer.getPassword()));
		}
		if (newCustomer.getCoupons() != null) {
			customer.setCoupons(newCustomer.getCoupons());
		}
		if (newCustomer.isActive() != customer.isActive()) {
			customer.setActive(newCustomer.isActive());
		}
		final Customer updatedCustomer = customerRepository.save(customer);
		return ResponseEntity.ok(updatedCustomer);
	}
	
	public Coupon getCoupon(long id) {
		return couponRepository.findById(id).get();
	}
	
	public List<Coupon> getAllCoupons(long id) {
		List<Coupon> coupons = couponRepository.findAll();
	//	coupons.stream().filter(c -> c.getEndDate().before(new Date())).map(c -> companyService.deleteCoupon(c.getId())).collect(Collectors.toList());
		coupons.removeAll(getCustomerCoupons(id));
		coupons.removeIf(c -> c.getAmount() == 0);
		coupons.removeIf(c -> c.getEndDate().before(new Date()));
		return coupons;
	}
	
	public List<Coupon> getCustomerCoupons(long id) {
		List<Coupon> coupons = (List<Coupon>) customerRepository.findById(id).get().getCoupons();
//		coupons.stream().filter(c -> c.getEndDate().before(new Date()))
//						.map(c -> companyService.deleteCoupon(c.getId())).collect(Collectors.toList());
		coupons.removeIf(c -> c.getEndDate().before(new Date()));
		return coupons;
	}

//	public List<Coupon> getCustomerCoupons(long id, Category category) {
//		return (List<Coupon>) customerRepository.findById(id).get().getCoupons()
//				.stream().filter(x -> x.getCategory().equals(category))
//				.collect(Collectors.toList());
//	}
//
//	public List<Coupon> getCustomerCoupons(long id, double maxPrice) {
//		return (List<Coupon>) customerRepository.findById(id).get().getCoupons()
//				.stream().filter(x -> x.getPrice() <= maxPrice)
//				.collect(Collectors.toList());
//	}

	public ResponseEntity<Customer> getCustomerById(long id){
		Customer existCustomer = customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found for this id : " + id));
		return ResponseEntity.ok(existCustomer);
	}


}
