package com.main.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.main.beans.Company;
import com.main.service.AdminService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class AdminControllerUnitTest {

	@Autowired
    private MockMvc mvc;
	
	@MockBean
	private AdminService adminService;
	
	@Test
	@WithUserDetails("admin@admin.com")
	public void test_get_admin_details() {
		Company company = new Company();
		company.setEmail("admin@admin.com");
		company.setPassword("admin");
		company.setFullName("Admin");
		ResponseEntity<Company> r = new ResponseEntity<Company>(company, HttpStatus.OK);
		when(adminService.getOneCompany(83)).thenReturn(r);
		ResultActions ra = null;
		try {
			ra = mvc.perform(get("/admin/")).andDo(print())
					.andExpect(content().json("{\"fullName\":\"Admin\",\"email\":\"admin@admin.com\",\"password\":\"admin\"}", false));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
