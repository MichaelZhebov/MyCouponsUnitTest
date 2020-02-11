package com.main.repo;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.main.beans.Company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;

@DataJpaTest
@RunWith(SpringRunner.class)
//uses H2
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CompanyRepositoryUnitTest {

	@Autowired
	CompanyRepository repo;
	
	@Test
	public void testEmpty() {
		assertTrue(repo.count() == 0);
	}
	
	@Test
	public void testWithTwoCompanies() {
		
		Company c1 = new Company();
		c1.setActive(true);
		c1.setEmail("test@test.com");
		c1.setFullName("test1 name");
		c1.setPassword("1234");
		c1.setRole("ADMIN");
		
		Company c2 = new Company();
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
		Company c1 = new Company();
		c1.setActive(true);
		c1.setEmail("test@test.com");
		c1.setFullName("test1 name");
		c1.setPassword("1234");
		c1.setRole("ADMIN");
		
		repo.save(c1);
		
		Company testCompany;
		
		Optional<Company> companyFromRepo = repo.findByEmailIgnoreCase("test@test.com");
		if (companyFromRepo.isEmpty()) {
			testCompany = null;
		} else {
			testCompany = companyFromRepo.get();
		}
		
		assertNotNull(testCompany);
	}
	
	@Test
	public void testFindCompanyFalse() {
		Company c1 = new Company();
		c1.setActive(true);
		c1.setEmail("test@test.com");
		c1.setFullName("test1 name");
		c1.setPassword("1234");
		c1.setRole("ADMIN");
		
		repo.save(c1);
		
		Company testCompany;
		
		Optional<Company> companyFromRepo = repo.findByEmailIgnoreCase("false@test.com");
		
		if (companyFromRepo.isEmpty()) {
			testCompany = null;
		} else {
			testCompany = companyFromRepo.get();
		}
		
		assertNull(testCompany);
	}
}
