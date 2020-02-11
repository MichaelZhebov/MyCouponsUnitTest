package com.main.rest;

import static org.springframework.http.ResponseEntity.ok;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.beans.Customer;
import com.main.repo.CompanyRepository;
import com.main.repo.CouponRepository;
import com.main.repo.CustomerRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("info")
@PreAuthorize("hasRole('ADMIN') or hasRole('COMPANY') or hasRole('CUSTOMER')")
public class InfoController {
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CouponRepository couponRepository;
	
	@SuppressWarnings("rawtypes")
	@GetMapping
	public ResponseEntity getInfo() {
		Map<Object, Object> model = new HashMap<>();
		model.put("companiesCount", companyRepository.count() - 1);
		model.put("customersCount", customerRepository.count());
		model.put("couponsCount", couponRepository.count());
		Long transactionsCount = customerRepository.findAll().stream()
													.map(Customer::getCoupons)
													.filter(c -> c != null)
													.mapToLong(Collection::size)
													.sum();
//		List<Customer> customers = customerRepository.findAll();
//		for (Customer customer : customers) {
//			for (Coupon coupon : customer.getCoupons()) {
//				transactionsCount = transactionsCount.longValue() + 1;
//			}
//		}
		model.put("transCount", transactionsCount);
		return ok(model);
	}

}
