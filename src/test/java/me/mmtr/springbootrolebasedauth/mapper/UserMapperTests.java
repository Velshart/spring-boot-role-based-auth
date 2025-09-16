package me.mmtr.springbootrolebasedauth.mapper;

import me.mmtr.springbootrolebasedauth.model.User;
import me.mmtr.springbootrolebasedauth.model.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserMapperTests {
    private static User user;
    private static UserDTO userDTO;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        String username = "User";
        String password = "very secret password";

        user = new User();
        user.setUsername(username);
        user.setPassword(password);

        userDTO = userMapper.toDTO(user);
    }

    @Test
    void shouldCorrectlyConvertEntityToDTO() {
        assertNotNull(userDTO);
        assertEquals(user.getUsername(), userDTO.getUsername());
        assertEquals(user.getPassword(), userDTO.getPassword());
    }

    @Test
    void shouldCorrectlyConvertDTOToEntity() {
        User converted = userMapper.toEntity(userDTO);
        assertNotNull(converted);
        assertEquals(user.getUsername(), converted.getUsername());
        assertTrue(passwordEncoder.matches(user.getPassword(), converted.getPassword()));
    }
}
