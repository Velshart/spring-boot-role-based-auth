package me.mmtr.springbootrolebasedauth.bootstrap;

import me.mmtr.springbootrolebasedauth.data.User;
import me.mmtr.springbootrolebasedauth.data.dto.UserDTO;
import me.mmtr.springbootrolebasedauth.enums.RoleName;
import me.mmtr.springbootrolebasedauth.repository.UserRepository;
import me.mmtr.springbootrolebasedauth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(AdminSeeder.class);
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_ACCOUNT_USERNAME}")
    private String adminUsername;

    @Value("${ADMIN_ACCOUNT_PASSWORD}")
    private String adminPassword;

    public AdminSeeder(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        registerOrUpdateAdminAccount();
    }

    private void registerOrUpdateAdminAccount() {
        Optional<User> userOptional = userRepository.findByUsername(adminUsername);

        if (userOptional.isEmpty()) {
            userService.registerUser(new UserDTO(adminUsername, adminPassword), RoleName.ADMIN);
            logger.info("Admin account created: {}", adminUsername);

        } else {

            User user = userOptional.get();
            if (user.getRole().getRoleName().equals(RoleName.ADMIN)) {

                boolean usernameChanged = !user.getUsername().equals(adminUsername);
                boolean passwordChanged = !passwordEncoder.matches(adminPassword, user.getPassword());

                if (usernameChanged || passwordChanged) {
                    if (usernameChanged) user.setUsername(adminUsername);
                    if (passwordChanged) user.setPassword(passwordEncoder.encode(adminPassword));

                    logger.info("Admin account updated: {}", adminUsername);
                    userRepository.save(user);
                }

            } else {
                logger.warn("The account with the specified user name {} already exists," +
                        " select another user name.", adminUsername);
            }
        }
    }
}
