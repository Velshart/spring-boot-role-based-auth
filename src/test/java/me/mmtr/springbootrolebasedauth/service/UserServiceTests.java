package me.mmtr.springbootrolebasedauth.service;

import me.mmtr.springbootrolebasedauth.enums.RoleName;
import me.mmtr.springbootrolebasedauth.model.User;
import me.mmtr.springbootrolebasedauth.model.dto.UserDTO;
import me.mmtr.springbootrolebasedauth.repository.RoleRepository;
import me.mmtr.springbootrolebasedauth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldRegisterNewUserIfUserWithGivenUsernameDoesNotExist() {
        String username = "user";
        String password = "very secure password";
        userService.registerUser(new UserDTO(username, password), RoleName.USER);

        User user = userRepository.findByUsername(username).orElse(null);

        assertNotNull(user);
        assertEquals(username, user.getUsername());
        assertTrue(passwordEncoder.matches(password, user.getPassword()));
    }

    @Test
    void shouldNotRegisterNewUserIfUserWithGivenUsernameExists() {
        String username = "user";
        String password = "very secure password";

        User existingUser = new User();
        existingUser.setUsername(username);
        existingUser.setPassword(passwordEncoder.encode("another very secure password"));
        existingUser.setRole(roleRepository.findByRoleName(RoleName.USER).orElse(null));

        userRepository.save(existingUser);

        userService.registerUser(new UserDTO(username, password), RoleName.USER);

        User user = userRepository.findByUsername(username).orElse(null);

        assertNotNull(user);
        assertEquals(username, user.getUsername());
        assertTrue(passwordEncoder.matches("another very secure password", user.getPassword()));
        assertFalse(passwordEncoder.matches(password, user.getPassword()));
    }
}