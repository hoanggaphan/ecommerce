package com.springboot.ecommerce;

import com.springboot.ecommerce.model.Role;
import com.springboot.ecommerce.model.User;
import com.springboot.ecommerce.service.UserService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class EcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

	private final UserService userService;

	@Bean
	CommandLineRunner run() {
		return args -> {
			// userService.createRole(new Role(null, "ROLE_USER"));
			// userService.createRole(new Role(null, "ROLE_MANAGER"));
			// userService.createRole(new Role(null, "ROLE_ADMIN"));
			// userService.createRole(new Role(null, "ROLE_SUPER_ADMIN"));

			// User user1 = new User();
			// user1.setUsername("hoang");
			// user1.setPassword("111");
			// userService.createUser(user1);

			// User user2 = new User();
			// user2.setUsername("nghia");
			// user2.setPassword("111");
			// userService.createUser(user2);

			// User user3 = new User();
			// user3.setUsername("tam");
			// user3.setPassword("111");
			// userService.createUser(user3);

			// User user4 = new User();
			// user4.setUsername("phuc");
			// user4.setPassword("111");
			// userService.createUser(user4);

			// userService.addRoleToUser("hoang", "ROLE_USER");
			// userService.addRoleToUser("hoang", "ROLE_SUPER_ADMIN");
			// userService.addRoleToUser("nghia", "ROLE_MANAGER");
			// userService.addRoleToUser("tam", "ROLE_ADMIN");
			// userService.addRoleToUser("phuc", "ROLE_SUPER_ADMIN");

		};
	}
}
