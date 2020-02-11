package com.main.repo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.main.beans.Customer;

@DataJpaTest
@RunWith(SpringRunner.class)
//uses H2
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CustomerRepositoryUnitTest {

	@Autowired
	CustomerRepository repo;
	
	@Test
	public void testEmpty() {
		assertTrue(repo.count() == 0);
	}
	
	@Test
	public void testWithTwoCustomer() {
		
		Customer c1 = new Customer();
		c1.setActive(true);
		c1.setEmail("test@test.com");
		c1.setFullName("test1 name");
		c1.setPassword("1234");
		c1.setRole("ADMIN");
		
		Customer c2 = new Customer();
		c2.setActive(true);
		c2.setEmail("test@test.com");
		c2.setFullName("test1 name");
		c2.setPassword("1234");
		c2.setRole("ADMIN");
		
		repo.save(c1);
		repo.save(c2);
		
		assertEquals(2,repo.count());
	}
	
	@Test
	public void testFindCompanyTrue() {
		Customer c1 = new Customer();
		c1.setActive(true);
		c1.setEmail("test@test.com");
		c1.setFullName("test1 name");
		c1.setPassword("1234");
		c1.setRole("ADMIN");
		
		repo.save(c1);
		
		Customer testCustomer;
		
		Optional<Customer> customerFromRepo = repo.findByEmailIgnoreCase("test@test.com");
		if (customerFromRepo.isEmpty()) {
			testCustomer = null;
		} else {
			testCustomer = customerFromRepo.get();
		}
		
		assertNotNull(testCustomer);
	}
	
	@Test
	public void testFindCompanyFalse() {
		Customer c1 = new Customer();
		c1.setActive(true);
		c1.setEmail("test@test.com");
		c1.setFullName("test1 name");
		c1.setPassword("1234");
		c1.setRole("ADMIN");
		
		repo.save(c1);
		
		Customer testCustomer;
		
		Optional<Customer> customerFromRepo = repo.findByEmailIgnoreCase("false@test.com");
		
		if (customerFromRepo.isEmpty()) {
			testCustomer = null;
		} else {
			testCustomer = customerFromRepo.get();
		}
		
		assertNull(testCustomer);
	}
}
