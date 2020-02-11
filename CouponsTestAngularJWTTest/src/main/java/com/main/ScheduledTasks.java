package com.main;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.main.beans.Coupon;
import com.main.repo.CouponRepository;
import com.main.service.CompanyService;

@Component
public class ScheduledTasks {
	
	@Autowired
	private CouponRepository couponRepository;
	
	@Autowired
	private CompanyService companyService;
	
	@Scheduled(fixedRate = 1000 * 60 * 60 * 12) // 12 hours
    @Transactional(propagation=Propagation.REQUIRES_NEW)
	public void checkCoupons() {
		List<Coupon> coupons = couponRepository.findAll();
		coupons.stream().filter(c -> c.getEndDate().before(new Date()))
		.map(c -> companyService.deleteCoupon(c.getId())).collect(Collectors.toList());
	}
}
