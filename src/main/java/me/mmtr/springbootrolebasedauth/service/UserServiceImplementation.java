package me.mmtr.springbootrolebasedauth.service;

import me.mmtr.springbootrolebasedauth.model.Role;
import me.mmtr.springbootrolebasedauth.model.User;
import me.mmtr.springbootrolebasedauth.model.dto.UserDTO;
import me.mmtr.springbootrolebasedauth.enums.RoleName;
import me.mmtr.springbootrolebasedauth.mapper.UserMapper;
import me.mmtr.springbootrolebasedauth.repository.RoleRepository;
import me.mmtr.springbootrolebasedauth.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public UserServiceImplementation(UserRepository userRepository,
                                     RoleRepository roleRepository,
                                     UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public Long registerUser(UserDTO userDTO, RoleName role) {
        Optional<Role> roleOptional = roleRepository.findByRoleName(role);
        Optional<User> userOptional = userRepository.findByUsername(userDTO.getUsername());

        if (roleOptional.isPresent() && userOptional.isEmpty()) {
            User newUser = userMapper.toEntity(userDTO);
            newUser.setRole(roleOptional.get());

            return userRepository.save(newUser).getId();
        }
        return null;
    }
}