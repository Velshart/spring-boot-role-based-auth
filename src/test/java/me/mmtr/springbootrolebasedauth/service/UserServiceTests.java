package me.mmtr.springbootrolebasedauth.service;

import me.mmtr.springbootrolebasedauth.data.Role;
import me.mmtr.springbootrolebasedauth.data.User;
import me.mmtr.springbootrolebasedauth.data.dto.UserDTO;
import me.mmtr.springbootrolebasedauth.enums.RoleName;
import me.mmtr.springbootrolebasedauth.repository.RoleRepository;
import me.mmtr.springbootrolebasedauth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserDTO userDTO;

    private Role role;

    @BeforeEach
    public void setUp() {
        userDTO = new UserDTO("username", "password");

        role = new Role();
        role.setRoleName(RoleName.USER);
    }

    @Test
    public void shouldRegisterNewUserWhenRoleExists() {
        when(roleRepository.findByRoleName(RoleName.USER)).thenReturn(Optional.of(role));

        userService.registerUser(userDTO, role.getRoleName());

        verify(roleRepository, times(1)).findByRoleName(RoleName.USER);
        verify(passwordEncoder, times(1)).encode(userDTO.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void shouldNotRegisterNewUserWhenRoleDoesNotExist() {
        when(roleRepository.findByRoleName(RoleName.USER)).thenReturn(Optional.empty());

        userService.registerUser(userDTO, role.getRoleName());

        verify(roleRepository, times(1)).findByRoleName(RoleName.USER);
        verify(passwordEncoder, never()).encode(userDTO.getPassword());
        verify(userRepository, never()).save(any(User.class));
    }
}
