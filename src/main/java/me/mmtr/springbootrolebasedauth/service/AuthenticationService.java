package me.mmtr.springbootrolebasedauth.service;

import me.mmtr.springbootrolebasedauth.data.Role;
import me.mmtr.springbootrolebasedauth.data.User;
import me.mmtr.springbootrolebasedauth.data.dto.UserDTO;
import me.mmtr.springbootrolebasedauth.enums.RoleName;
import me.mmtr.springbootrolebasedauth.repository.RoleRepository;
import me.mmtr.springbootrolebasedauth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(RoleRepository roleRepository,
                                 UserRepository userRepository,
                                 PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(UserDTO userDTO, RoleName role) {
        Optional<Role> roleOptional = roleRepository.findByRoleName(role);

        if (roleOptional.isEmpty()) return;

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(roleOptional.get());

        userRepository.save(user);
    }
}
