package com.main.client;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import com.main.beans.Company;

@RunWith(SpringRunner.class)
@JsonTest
public class AdminClientUnitTest {
	
	@Autowired
	private JacksonTester<Company> test;
	
	@Test
	public void test_company_json_info() {
		// perform both structural & content checks on JSON
		try {
			Company toCheckAgainst = new Company();
			toCheckAgainst.setId((long) 64);
			toCheckAgainst.setFullName("Shufersal test Israel");

			// fetch REAL person from DB as JSON through REST
			URL url = new URL("http://localhost:8080/admin/company/64");
			String json = "";
			try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
				json = in.readLine();
				System.out.println("Got from server: " + json);
			} catch (Exception e) {
				fail("Failed connecting to REAL server");
			}
			// check structure & content
			test.parse(json).assertThat().hasFieldOrPropertyWithValue("id", 64L).hasFieldOrPropertyWithValue("fullName", "Shufersal test Israel");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
