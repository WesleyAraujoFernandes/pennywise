package br.com.knowledge.pennywise;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.knowledge.pennywise.domain.user.User;
import br.com.knowledge.pennywise.repository.UserRepository;

@SpringBootTest
class PennywiseApplicationTests {

	@Autowired
	UserRepository userRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void shouldFindUserByEmail() {
		Optional<User> user = userRepository.findByEmail("admin@pennywise.com");
		assertTrue(user.isPresent());
	}

}
