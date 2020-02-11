package com.main.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.beans.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

	Optional<Company> findByEmailIgnoreCase(String email);
	
	Optional<Company> findByFullNameIgnoreCase(String fullName);
	
	long count();
}
