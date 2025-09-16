package me.mmtr.springbootrolebasedauth.bootstrap;

import me.mmtr.springbootrolebasedauth.model.User;
import me.mmtr.springbootrolebasedauth.model.dto.UserDTO;
import me.mmtr.springbootrolebasedauth.enums.RoleName;
import me.mmtr.springbootrolebasedauth.repository.RoleRepository;
import me.mmtr.springbootrolebasedauth.repository.UserRepository;
import me.mmtr.springbootrolebasedauth.service.UserServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminAccountSeeder extends Seeder implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(AdminAccountSeeder.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceImplementation userService;

    @Autowired
    public AdminAccountSeeder(RoleRepository roleRepository,
                              UserRepository userRepository,
                              PasswordEncoder passwordEncoder,
                              UserServiceImplementation userService) {
        super(roleRepository);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    public AdminAccountSeeder(RoleRepository roleRepository,
                              UserRepository userRepository,
                              PasswordEncoder passwordEncoder,
                              UserServiceImplementation userService,
                              String adminUsername,
                              String adminPassword) {
        super(roleRepository);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    @Override
    void seed() {
        Optional<User> userOptional = userRepository.findByUsername(adminUsername);

        if (userOptional.isEmpty()) {
            userService.registerUser(new UserDTO(adminUsername, adminPassword), RoleName.ADMIN);

            logger.info("Admin account created: {}", adminUsername);
        } else {
            User existingAdministrator = userOptional.get();

            if (existingAdministrator.getRole().getRoleName().equals(RoleName.ADMIN)) {
                boolean usernameChanged = !existingAdministrator
                        .getUsername().equals(adminUsername);

                boolean passwordChanged = !passwordEncoder
                        .matches(adminPassword, existingAdministrator.getPassword());

                if (usernameChanged || passwordChanged) {
                    if (usernameChanged) existingAdministrator.setUsername(adminUsername);
                    if (passwordChanged) existingAdministrator.setPassword(passwordEncoder.encode(adminPassword));

                    logger.info("Admin account updated: {}", adminUsername);
                    userRepository.save(existingAdministrator);
                }
            } else {
                logger.warn("The account with the specified user name {} already exists," +
                        " select another user name.", adminUsername);
            }
        }
    }
}
