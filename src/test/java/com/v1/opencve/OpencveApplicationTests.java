package com.v1.opencve;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.v1.opencve.domainobject.UserDO;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
class OpencveApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@Test
	void contextLoads() {
	}

	// For convert json to string.
	public static String asJsonString(final Object object){
		try{
			return new ObjectMapper().writeValueAsString(object);
		}catch (Exception e){
			throw new RuntimeException();
		}
	}

	@Test
	public void getAllUsers() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users").accept(MediaType.APPLICATION_JSON)) // get sonucunda json verisi döndü mü?
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].id").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].isActive").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].username").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].password").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].email").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].enable_notifications").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].isAdmin").exists());
	}

	@Test
	public void getUserByUsername() throws Exception{
		String username = "aysen";
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{username}", username).accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.isActive").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.username").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.password").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.email").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.enable_notifications").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.isAdmin").exists());
	}

	@Test
	public void createUser() throws Exception{
		UserDO newUser = new UserDO();

		newUser.setIsAdmin(true);
		newUser.setPassword("123456Aburak");
		newUser.setEmail("aysemmmburak@gmail.com");
		newUser.setLast_name("burak");
		newUser.setFirst_name("aysenur");
		newUser.setUsername("aysenurrrrburak");
		newUser.setEnable_notifications(true);


		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(newUser)))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.isActive").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.username").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.password").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.email").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.enable_notifications").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.isAdmin").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.username").value("aysenurrrrburak"));
	}

	@Test
	public void updateUserWithDTO() throws Exception{
		UserDO newUser = new UserDO();

		newUser.setId(11L);
		newUser.setUsername("aysenurrrrburak");
		newUser.setFirst_name("BURAK");
		newUser.setLast_name("aysenur");
		newUser.setEmail("imrulkaysnfk0000@gmail.com");

		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(newUser)))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.username").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.email").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.username").value("aysemnurb")); // DTO objesi ile kullanıcının görebileceği bilgiyi kısıtladığımız için id'ye erişemeyiz
	}

	@Test
	public void testDeleteCustomer() throws Exception{
		String userID = "10";
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{userID}", userID).accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
}
