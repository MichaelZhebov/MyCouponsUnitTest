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

import com.main.beans.Coupon;

@DataJpaTest
@RunWith(SpringRunner.class)
//uses H2
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CouponRepositoryUnitTest {

	@Autowired
	CouponRepository repo;
	
	@Test
	public void testEmpty() {
		assertTrue(repo.count() == 0);
	}
	
	@Test
	public void testWithTwoCustomer() {
		
		Coupon c1 = new Coupon();
		c1.setTitle("test1");
		c1.setDescription("desc1");
		c1.setCompanyName("company1");
		
		
		Coupon c2 = new Coupon();
		c2.setTitle("test2");
		c2.setDescription("desc2");
		c2.setCompanyName("company2");
		
		repo.save(c1);
		repo.save(c2);
		
		assertEquals(2,repo.count());
	}
	
	@Test
	public void testFindCouponTrue() {
		Coupon c1 = new Coupon();
		c1.setTitle("test1");
		c1.setDescription("desc1");
		c1.setCompanyName("company1");
		
		
		repo.save(c1);
		
		Coupon testCoupon;
		
		Optional<Coupon> couponFromRepo = repo.findByTitle("test1");
		System.out.println(couponFromRepo);
		if (couponFromRepo.isEmpty()) {
			testCoupon = null;
		} else {
			testCoupon = couponFromRepo.get();
		}
	
		
		assertNotNull(testCoupon);
	}
	
	@Test
	public void testFindCouponFalse() {
		Coupon c1 = new Coupon();
		c1.setTitle("test1");
		c1.setDescription("desc1");
		c1.setCompanyName("company1");
		
		repo.save(c1);
		
		Coupon testCoupon;
		
		Optional<Coupon> couponFromRepo = repo.findByTitle("test1False");
		
		if (couponFromRepo.isEmpty()) {
			testCoupon = null;
		} else {
			testCoupon = couponFromRepo.get();
		}
	
		
		assertNull(testCoupon);
	}
}
