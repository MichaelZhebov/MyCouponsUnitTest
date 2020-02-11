package com.main.beans;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.main.repo.CompanyRepository;
import com.main.repo.CustomerRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Company> company = companyRepository.findByEmailIgnoreCase(email);
		Optional<Customer> customer = customerRepository.findByEmailIgnoreCase(email);
		if (company.isEmpty() && customer.isEmpty()) {
			throw new UsernameNotFoundException("User with " + email + " not found");
		}
		if (company.isEmpty()) {
			return customer.map(UserDetailsImpl::new).get();
		} else {
			return company.map(UserDetailsImpl::new).get();
		}
	}

}
