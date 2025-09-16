package me.mmtr.springbootrolebasedauth.service;

import me.mmtr.springbootrolebasedauth.model.User;
import me.mmtr.springbootrolebasedauth.model.dto.UserDTO;
import me.mmtr.springbootrolebasedauth.enums.RoleName;

public interface UserService {
    User findUserById(Long id);
    User findUserByUsername(String username);
    Long registerUser(UserDTO userDTO, RoleName role);
}
