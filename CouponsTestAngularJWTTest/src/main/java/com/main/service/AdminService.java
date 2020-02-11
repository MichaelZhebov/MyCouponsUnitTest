package com.main.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.main.beans.ClientType;
import com.main.beans.Company;
import com.main.beans.Coupon;
import com.main.beans.Customer;
import com.main.repo.CompanyRepository;
import com.main.repo.CouponRepository;
import com.main.repo.CustomerRepository;

@Service
public class AdminService {

	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private CouponRepository couponRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private BCryptPasswordEncoder encoder;

	@PostConstruct
	private void postConstruct() {
		Optional<Company> adminExist = companyRepository.findByEmailIgnoreCase("admin@admin.com");
		if (adminExist.isEmpty()) {
			Company admin = new Company();
			admin.setFullName("Michael Zhebov");
			admin.setEmail("admin@admin.com");
			admin.setPassword("$2a$10$oinP34L8LB9kn3u9n2lZouygXzwtaCqfO4TeOcLKGq.8qjfT/pAkO");
			admin.setActive(true);
			admin.setRole(ClientType.ROLE_ADMIN.toString());
			companyRepository.save(admin);
		}
		return;
	}

	public ResponseEntity<?> addCompany(Company company) {
		if (!company.getEmail().matches("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$")) {
			return new ResponseEntity<String>("Email is not valid!", HttpStatus.BAD_REQUEST);
		}
		if (company.getPassword().length() < 4) {
			return new ResponseEntity<String>("Password must be at least 4 characters", HttpStatus.BAD_REQUEST);
		}
		Optional<Company> existCompany = companyRepository.findByEmailIgnoreCase(company.getEmail());
		Optional<Company> esixtByNameCompany = companyRepository.findByFullNameIgnoreCase(company.getFullName());
		Optional<Customer> existCustomer = customerRepository.findByEmailIgnoreCase(company.getEmail());
		if (!esixtByNameCompany.isEmpty()) {
			return new ResponseEntity<String>("This name is already used!", HttpStatus.BAD_REQUEST);
		}
		if (company.getEmail() == null || company.getFullName() == null || company.getPassword() == null) {
			return new ResponseEntity<String>("Please write all fields!", HttpStatus.BAD_REQUEST);
		}
		if (existCompany.isEmpty() && existCustomer.isEmpty()) {
			Company newCompany = new Company();
			newCompany.setActive(true);
			newCompany.setEmail(company.getEmail());
			newCompany.setFullName(company.getFullName());
			newCompany.setPassword(encoder.encode(company.getPassword()));
			newCompany.setRole(ClientType.ROLE_COMPANY.name());
			companyRepository.save(newCompany);
			return new ResponseEntity<Company>(newCompany, HttpStatus.OK);
		}
		return new ResponseEntity<String>("This email is already used!", HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<?> updateCompany(Company newCompany, long companyId) {
		if (!newCompany.getEmail().matches("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$")) {
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

	public Map<String, Boolean> deleteCompany(long companyID) {
		Company company = companyRepository.findById(companyID)
				.orElseThrow(() -> new ResourceNotFoundException("Company not found for this id : " + companyID));
		companyRepository.delete(company);
		List<Coupon> coupons = couponRepository.findBycompanyId(companyID);
		for (Coupon coupon : coupons) {
			Customer customer = customerRepository.findByCouponsId(coupon.getId());
			if (customer != null) {
				customer.getCoupons().removeAll(coupons);
				customerRepository.save(customer);
			}
		}
		couponRepository.deleteAll(coupons);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	public List<Company> getAllCompanies() {
		List<Company> companies = companyRepository.findAll();
		for (Company company : companies) {
			List<Coupon> coupons = couponRepository.findBycompanyId(company.getId());
			company.setCoupons(coupons);
		}
		companies.removeIf(c -> c.getRole().equalsIgnoreCase("ROLE_ADMIN"));
		companies.sort((c1, c2) -> c1.getFullName().compareTo(c2.getFullName()));
		return companies;
	}

	public ResponseEntity<Company> getOneCompany(long companyID) {
		Company company = companyRepository.findById(companyID)
				.orElseThrow(() -> new ResourceNotFoundException("Company not found for this id : " + companyID));
		List<Coupon> coupons = couponRepository.findBycompanyId(companyID);
		company.setCoupons(coupons);
		return ResponseEntity.ok(company);
	}

	public List<Coupon> getAllCoupons() {
//		List<Coupon> coupons = couponRepository.findAll();
//		coupons.stream().filter(c -> c.getEndDate().before(new Date()))
//						.map(c -> companyService.deleteCoupon(c.getId())).collect(Collectors.toList());
//		return coupons;
		return couponRepository.findAll();
	}

	public Coupon getCoupon(long id) {
		return couponRepository.findById(id).get();
	}

	public List<Coupon> getCompanyCoupons(long id) {
		return couponRepository.findBycompanyId(id);
	}

	public ResponseEntity<?> addCustomer(Customer customer) {
		if (!customer.getEmail().matches("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$")) {
			return new ResponseEntity<String>("Email is not valid!", HttpStatus.BAD_REQUEST);
		}
		if (customer.getPassword().length() < 4) {
			return new ResponseEntity<String>("Password must be at least 4 characters", HttpStatus.BAD_REQUEST);
		}
		if (customer.getEmail() == null || customer.getFullName() == null || customer.getPassword() == null) {
			return new ResponseEntity<String>("Please write all fields!", HttpStatus.BAD_REQUEST);
		}
		Optional<Customer> existCustomer = customerRepository.findByEmailIgnoreCase(customer.getEmail());
		Optional<Company> existCompany = companyRepository.findByEmailIgnoreCase(customer.getEmail());
		if (existCompany.isEmpty() && existCustomer.isEmpty()) {
			Customer newCustomer = new Customer();
			newCustomer.setActive(true);
			newCustomer.setEmail(customer.getEmail());
			newCustomer.setFullName(customer.getFullName());
			newCustomer.setPassword(encoder.encode(customer.getPassword()));
			newCustomer.setRole(ClientType.ROLE_CUSTOMER.name());
			customerRepository.save(newCustomer);
			return new ResponseEntity<Customer>(newCustomer, HttpStatus.OK);
		}
		return new ResponseEntity<String>("This email is already used!", HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<?> updateCustomer(Customer newCustomer, long customerID) {
		if (!newCustomer.getEmail().matches("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$")) {
			return new ResponseEntity<String>("Email is not valid!", HttpStatus.BAD_REQUEST);
		}
		if (newCustomer.getPassword().length() < 4) {
			return new ResponseEntity<String>("Password must be at least 4 characters", HttpStatus.BAD_REQUEST);
		}
		Customer customer = customerRepository.findById(customerID).get();
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

	public Map<String, Boolean> deleteCustomer(long customerID) {
		Customer customer = customerRepository.findById(customerID)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found for this id : " + customerID));

		customerRepository.delete(customer);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	public List<Customer> getAllCustomers() {
		List<Customer> customers = customerRepository.findAll();
		customers.sort((c1, c2) -> c1.getFullName().compareTo(c2.getFullName()));
		return customers;
	}

	public ResponseEntity<Customer> getOneCustomer(long customerID) {
		Customer customer = customerRepository.findById(customerID)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id : " + customerID));
		return ResponseEntity.ok(customer);
	}
}
