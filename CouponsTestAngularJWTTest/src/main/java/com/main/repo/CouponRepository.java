package com.main.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.beans.Coupon;
import com.main.beans.Coupon.Category;



public interface CouponRepository extends JpaRepository<Coupon, Long> {
	
	List<Coupon> findBycompanyId(long id);
	
	List<Coupon> findByCompanyIdAndCategory(long id, Category category);
	
	List<Coupon> findByCompanyIdAndPriceLessThanEqual(long id, double maxPrice);
	
	Optional<Coupon> findByTitle(String title);
	
	long count();
}
