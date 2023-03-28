package jtechlog.jtechlogbootsecurity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@Sql(statements = "delete from users where username not in ('user', 'admin')")
class UserControllerTest {

	@Autowired
	WebApplicationContext webApplicationContext;

	MockMvc mockMvc;

	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(webApplicationContext)
				.apply(springSecurity())
				.build();
	}

	@Test
	void testNotLogged() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	void testListUsers() throws Exception {
		mockMvc.perform(post("/")
				.param("username", "johndoe")
				.param("password", "johndoe")
				.param("role", "ROLE_USER")
				.with(user("admin").roles("ADMIN"))
				.with(csrf()))
				.andExpect(status().is3xxRedirection());

		mockMvc.perform(get("/").with(user("user").roles("USER")))
				.andExpect(status().isOk())
				.andExpect(view().name("index"))
				.andExpect(model().attribute("users",
						hasItem(hasProperty("username", equalTo("johndoe")))))
				.andExpect(content().string(containsString("johndoe")));

	}

}
