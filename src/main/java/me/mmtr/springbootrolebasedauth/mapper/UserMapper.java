package me.mmtr.springbootrolebasedauth.mapper;

import me.mmtr.springbootrolebasedauth.model.User;
import me.mmtr.springbootrolebasedauth.model.dto.UserDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<UserDTO, User> {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO toDTO(User user) {
        return new UserDTO(user.getUsername(), user.getPassword());
    }

    @Override
    public User toEntity(UserDTO dto) {
        return new User(
                dto.getUsername(),
                passwordEncoder.encode(dto.getPassword())
        );
    }
}
