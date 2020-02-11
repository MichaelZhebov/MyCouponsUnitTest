package com.main.service;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.collection.IsMapContaining;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import com.main.beans.Company;
import com.main.beans.Coupon;
import com.main.beans.Coupon.Category;
import com.main.beans.Customer;
import com.main.repo.CompanyRepository;
import com.main.repo.CouponRepository;
import com.main.repo.CustomerRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@RunWith(SpringRunner.class)
public class AdminServiceUnitTest {

	@MockBean
	private CompanyRepository companyRepository;
	@MockBean
	private CouponRepository couponRepository;
	@MockBean
	private CustomerRepository customerRepository;

	@Autowired
	private AdminService adminService;

	@Test
	public void test_add_company_valid() {
		Company company = new Company();
		company.setEmail("test@test.com");
		company.setPassword("1234");
		company.setFullName("test name");

		assertEquals(adminService.addCompany(company).getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void test_add_company_not_valid_email() {
		Company company = new Company();
		company.setEmail("test@testcom");
		company.setPassword("1234");
		company.setFullName("test name");

		assertEquals(adminService.addCompany(company).getBody(), "Email is not valid!");
	}

	@Test
	public void test_add_company_not_valid_password() {
		Company company = new Company();
		company.setEmail("test@test.com");
		company.setPassword("123");
		company.setFullName("test name");

		assertEquals(adminService.addCompany(company).getBody(), "Password must be at least 4 characters");
	}

	@Test
	public void test_add_company_not_valid_field() {
		Company company = new Company();
		company.setEmail("test@test.com");
		company.setPassword("1234");

		assertEquals(adminService.addCompany(company).getBody(), "Please write all fields!");
	}

	@Test
	public void test_add_company_not_valid_email_used() {
		Company company = new Company();
		company.setEmail("test@test.com");
		company.setPassword("1234");
		company.setFullName("test name");

		Company company2 = new Company();
		company2.setEmail("test@test.com");
		company2.setPassword("1234");
		company2.setFullName("test name");

		when(companyRepository.findByEmailIgnoreCase(company.getEmail())).thenReturn(Optional.of(company));

		assertEquals(adminService.addCompany(company2).getBody(), "This email is already used!");
	}

	@Test
	public void test_add_company_not_valid_name_used() {
		Company company = new Company();
		company.setEmail("test@test.com");
		company.setPassword("1234");
		company.setFullName("test name");

		Company company2 = new Company();
		company2.setEmail("test@test.com");
		company2.setPassword("1234");
		company2.setFullName("test name");

		when(companyRepository.findByFullNameIgnoreCase(company.getFullName())).thenReturn(Optional.of(company));

		assertEquals(adminService.addCompany(company2).getBody(), "This name is already used!");
	}

	@Test
	public void test_update_company_valid() {
		Company company = new Company();
		company.setEmail("test@test.com");
		company.setPassword("1234");
		company.setFullName("test name");

		when(companyRepository.findById((long) 1)).thenReturn(Optional.of(company));

		Company updateCompany = new Company();
		updateCompany.setEmail("update@update.com");
		updateCompany.setPassword("1234");
		updateCompany.setFullName("update name");

		assertEquals(adminService.updateCompany(updateCompany, 1).getStatusCode(), HttpStatus.OK);
		assertEquals(adminService.getOneCompany(1).getBody().getFullName(), updateCompany.getFullName());
	}

	@Test
	public void test_update_company_not_valid_email() {
		Company company = new Company();
		company.setEmail("test@test.com");
		company.setPassword("1234");
		company.setFullName("test name");

		when(companyRepository.findById((long) 1)).thenReturn(Optional.of(company));

		Company updateCompany = new Company();
		updateCompany.setEmail("update@updatecom");
		updateCompany.setPassword("1234");
		updateCompany.setFullName("update name");

		assertEquals(adminService.updateCompany(updateCompany, 1).getBody(), "Email is not valid!");
	}

	@Test
	public void test_update_company_not_valid_password() {
		Company company = new Company();
		company.setEmail("test@test.com");
		company.setPassword("1234");
		company.setFullName("test name");

		when(companyRepository.findById((long) 1)).thenReturn(Optional.of(company));

		Company updateCompany = new Company();
		updateCompany.setEmail("update@update.com");
		updateCompany.setPassword("123");
		updateCompany.setFullName("update name");

		assertEquals(adminService.updateCompany(updateCompany, 1).getBody(), "Password must be at least 4 characters");
	}

	@Test
	public void test_update_company_not_valid_email_used() {
		Company company = new Company();
		company.setEmail("test@test.com");
		company.setPassword("1234");
		company.setFullName("test name");

		Company company2 = new Company();
		company2.setEmail("test@test.com");
		company2.setPassword("1234");
		company2.setFullName("test name");

		Company company3 = new Company();
		company3.setEmail("testtttt@test.com");
		company3.setPassword("1234");
		company3.setFullName("test name");

		Customer customer = new Customer();
		customer.setEmail("testtt@test.com");
		customer.setPassword("1234");
		customer.setFullName("test name");

		when(companyRepository.findById((long) 1)).thenReturn(Optional.of(company3));

		when(companyRepository.findByEmailIgnoreCase(company.getEmail())).thenReturn(Optional.of(company));
		when(customerRepository.findByEmailIgnoreCase(customer.getEmail())).thenReturn(Optional.of(customer));

		assertEquals(adminService.updateCompany(company2, 1).getBody(), "This email is already used!");
	}

	@Test
	public void test_update_company_not_valid_name_used() {
		Company company = new Company();
		company.setEmail("test@test.com");
		company.setPassword("1234");
		company.setFullName("test name");

		Company company2 = new Company();
		company2.setEmail("testaaa@test.com");
		company2.setPassword("1234");
		company2.setFullName("test name");

		Company company3 = new Company();
		company3.setEmail("testtttt@test.com");
		company3.setPassword("1234");
		company3.setFullName("test test name");

		when(companyRepository.findById((long) 1)).thenReturn(Optional.of(company3));

		when(companyRepository.findByFullNameIgnoreCase(company.getFullName())).thenReturn(Optional.of(company));

		assertEquals(adminService.updateCompany(company2, 1).getBody(), "This name is already used!");
	}

	@Test
	public void test_delete_company_valid() {
		Company company = new Company();
		company.setEmail("test@test.com");
		company.setPassword("1234");
		company.setFullName("test name");

		when(companyRepository.findById((long) 1)).thenReturn(Optional.of(company));

		assertThat(adminService.deleteCompany(1), IsMapContaining.hasEntry("deleted", true));
	}

	@Test(expected = ResourceNotFoundException.class)
	public void test_delete_company_not_valid_id() {
		adminService.deleteCompany(123);
	}

	@Test
	public void test_get_all_companies() {
		Company company = new Company();
		company.setEmail("test1@test.com");
		company.setPassword("1234");
		company.setFullName("test1 name");
		company.setRole("ROLE_ADMIN");

		Company company2 = new Company();
		company2.setEmail("test2@test.com");
		company2.setPassword("1234");
		company2.setFullName("test2 name");
		company2.setRole("ROLE_COMPANY");

		Company company3 = new Company();
		company3.setEmail("test3@test.com");
		company3.setPassword("1234");
		company3.setFullName("test3 name");
		company3.setRole("ROLE_COMPANY");

		List<Company> companies = new ArrayList<Company>();

		companies.add(company);
		companies.add(company2);
		companies.add(company3);

		when(companyRepository.findAll()).thenReturn(companies);

		companies.remove(company); // remove admin

		assertEquals(adminService.getAllCompanies(), companies);
	}

	@Test
	public void test_get_one_company_valid() {
		Company company = new Company();
		company.setEmail("test@test.com");
		company.setPassword("1234");
		company.setFullName("test name");

		Coupon coupon = new Coupon();
		coupon.setAmount(100);
		coupon.setCategory(Category.Computer);
		coupon.setCompanyId(0);
		coupon.setCompanyName(company.getFullName());
		coupon.setDescription("desc");

		company.addCoupon(coupon);

		when(companyRepository.findById((long) 1)).thenReturn(Optional.of(company));

		assertEquals(adminService.getOneCompany(1).getBody(), company);
	}

	@Test(expected = ResourceNotFoundException.class)
	public void test_get_one_company_not_valid_id() {
		adminService.getOneCompany(123);
	}

	@Test
	public void test_get_all_coupons() {
		Coupon coupon = new Coupon();
		coupon.setAmount(100);
		coupon.setCategory(Category.Computer);
		coupon.setCompanyId(0);
		coupon.setCompanyName("test");
		coupon.setDescription("desc");

		List<Coupon> coupons = new ArrayList<Coupon>();

		coupons.add(coupon);

		when(couponRepository.findAll()).thenReturn(coupons);

		assertEquals(adminService.getAllCoupons(), coupons);
	}

	@Test
	public void test_get_one_coupon() {
		Coupon coupon = new Coupon();
		coupon.setAmount(100);
		coupon.setCategory(Category.Computer);
		coupon.setCompanyId(0);
		coupon.setCompanyName("test");
		coupon.setDescription("desc");

		when(couponRepository.findById((long) 1)).thenReturn(Optional.of(coupon));

		assertEquals(adminService.getCoupon(1), coupon);
	}
	
	@Test
	public void test_get_company_coupon() {
		Coupon coupon = new Coupon();
		coupon.setAmount(100);
		coupon.setCategory(Category.Computer);
		coupon.setCompanyId(0);
		coupon.setCompanyName("test");
		coupon.setDescription("desc");
		
		List<Coupon> coupons = new ArrayList<Coupon>();
		
		coupons.add(coupon);

		when(couponRepository.findBycompanyId((long) 1)).thenReturn(coupons);

		assertEquals(adminService.getCompanyCoupons(1), coupons);
	}

	@Test
	public void test_add_customer_valid() {
		Customer customer = new Customer();
		customer.setEmail("test@test.com");
		customer.setPassword("1234");
		customer.setFullName("test name");

		assertEquals(adminService.addCustomer(customer).getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void test_add_customer_not_valid_email() {
		Customer customer = new Customer();
		customer.setEmail("test@testcom");
		customer.setPassword("1234");
		customer.setFullName("test name");

		assertEquals(adminService.addCustomer(customer).getBody(), "Email is not valid!");
	}

	@Test
	public void test_add_customer_not_valid_password() {
		Customer customer = new Customer();
		customer.setEmail("test@test.com");
		customer.setPassword("123");
		customer.setFullName("test name");

		assertEquals(adminService.addCustomer(customer).getBody(), "Password must be at least 4 characters");
	}

	@Test
	public void test_add_customer_not_valid_field() {
		Customer customer = new Customer();
		customer.setEmail("test@test.com");
		customer.setPassword("1234");

		assertEquals(adminService.addCustomer(customer).getBody(), "Please write all fields!");
	}

	@Test
	public void test_add_customer_not_valid_email_used() {
		Customer customer = new Customer();
		customer.setEmail("test@test.com");
		customer.setPassword("1234");
		customer.setFullName("test name");

		Customer customer2 = new Customer();
		customer2.setEmail("test@test.com");
		customer2.setPassword("1234");
		customer2.setFullName("test name");

		when(customerRepository.findByEmailIgnoreCase(customer.getEmail())).thenReturn(Optional.of(customer));

		assertEquals(adminService.addCustomer(customer2).getBody(), "This email is already used!");
	}

	@Test
	public void test_update_customer_valid() {
		Customer customer = new Customer();
		customer.setEmail("test@test.com");
		customer.setPassword("1234");
		customer.setFullName("test name");

		when(customerRepository.findById((long) 1)).thenReturn(Optional.of(customer));

		Customer updateCustomer = new Customer();
		updateCustomer.setEmail("update@update.com");
		updateCustomer.setPassword("1234");
		updateCustomer.setFullName("update name");

		assertEquals(adminService.updateCustomer(updateCustomer, 1).getStatusCode(), HttpStatus.OK);
		assertEquals(adminService.getOneCustomer(1).getBody().getFullName(), updateCustomer.getFullName());
	}

	@Test
	public void test_update_customer_not_valid_email() {
		Customer customer = new Customer();
		customer.setEmail("test@test.com");
		customer.setPassword("1234");
		customer.setFullName("test name");

		when(customerRepository.findById((long) 1)).thenReturn(Optional.of(customer));

		Customer updateCustomer = new Customer();
		updateCustomer.setEmail("update@updatecom");
		updateCustomer.setPassword("1234");
		updateCustomer.setFullName("update name");

		assertEquals(adminService.updateCustomer(updateCustomer, 1).getBody(), "Email is not valid!");
	}

	@Test
	public void test_update_customer_not_valid_password() {
		Customer customer = new Customer();
		customer.setEmail("test@test.com");
		customer.setPassword("1234");
		customer.setFullName("test name");

		when(customerRepository.findById((long) 1)).thenReturn(Optional.of(customer));

		Customer updateCustomer = new Customer();
		updateCustomer.setEmail("update@update.com");
		updateCustomer.setPassword("123");
		updateCustomer.setFullName("update name");

		assertEquals(adminService.updateCustomer(updateCustomer, 1).getBody(),
				"Password must be at least 4 characters");
	}

	@Test
	public void test_delete_customer() {
		Customer customer = new Customer();
		customer.setEmail("test@test.com");
		customer.setPassword("1234");
		customer.setFullName("test name");

		when(customerRepository.findById((long) 1)).thenReturn(Optional.of(customer));

		assertThat(adminService.deleteCustomer(1), IsMapContaining.hasEntry("deleted", true));
	}

	@Test(expected = ResourceNotFoundException.class)
	public void test_delete_customer_not_valid_id() {
		adminService.deleteCustomer(123);
	}

	@Test
	public void test_get_all_customers() {
		Customer customer = new Customer();
		customer.setEmail("test1@test.com");
		customer.setPassword("1234");
		customer.setFullName("test1 name");
		customer.setRole("ROLE_ADMIN");

		Customer customer2 = new Customer();
		customer2.setEmail("test2@test.com");
		customer2.setPassword("1234");
		customer2.setFullName("test2 name");
		customer2.setRole("ROLE_COMPANY");

		Customer customer3 = new Customer();
		customer3.setEmail("test3@test.com");
		customer3.setPassword("1234");
		customer3.setFullName("test3 name");
		customer3.setRole("ROLE_COMPANY");

		List<Customer> customers = new ArrayList<Customer>();

		customers.add(customer);
		customers.add(customer2);
		customers.add(customer3);

		when(customerRepository.findAll()).thenReturn(customers);

		assertEquals(adminService.getAllCustomers(), customers);
	}

	@Test
	public void test_get_one_customer_valid() {
		Customer customer = new Customer();
		customer.setEmail("test@test.com");
		customer.setPassword("1234");
		customer.setFullName("test name");

		Coupon coupon = new Coupon();
		coupon.setAmount(100);
		coupon.setCategory(Category.Computer);
		coupon.setCompanyId(0);
		coupon.setCompanyName(customer.getFullName());
		coupon.setDescription("desc");

		customer.addCoupon(coupon);

		when(customerRepository.findById((long) 1)).thenReturn(Optional.of(customer));

		assertEquals(adminService.getOneCustomer(1).getBody(), customer);
	}

	@Test(expected = ResourceNotFoundException.class)
	public void test_get_one_customer_not_valid_id() {
		adminService.getOneCustomer(123);
	}
}
