package me.mmtr.springbootrolebasedauth.bootstrap;

import me.mmtr.springbootrolebasedauth.model.User;
import me.mmtr.springbootrolebasedauth.mapper.UserMapper;
import me.mmtr.springbootrolebasedauth.repository.RoleRepository;
import me.mmtr.springbootrolebasedauth.repository.UserRepository;
import me.mmtr.springbootrolebasedauth.service.UserServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestEntityManager
public class AdminAccountSeederTests {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Value("${admin.account.username}")
    private String adminAccountUsername;
    @Value("${admin.account.password}")
    private String adminAccountPassword;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;
    private Seeder adminSeeder;

    @BeforeEach
    void setUp() {
        UserServiceImplementation userService = new UserServiceImplementation(
                userRepository,
                roleRepository,
                userMapper
        );

        Seeder roleSeeder = new RoleSeeder(roleRepository);
        roleSeeder.seed();

        adminSeeder = new AdminAccountSeeder(
                roleRepository,
                userRepository,
                passwordEncoder,
                userService,
                adminAccountUsername,
                adminAccountPassword
        );

        adminSeeder.seed();
    }

    @Test
    public void databaseShouldContainAdminAccountWithUsernameAndPasswordSetInPropertiesFile() {
        User admin = userRepository.findByUsername(adminAccountUsername).orElse(null);

        assertNotNull(admin);
        assertEquals(admin.getUsername(), adminAccountUsername);
        assertTrue(new BCryptPasswordEncoder().matches(adminAccountPassword, admin.getPassword()));
    }

    @Test
    public void seederShouldUpdateExistingAdminAccountIfUsernameOrPasswordWasChangedInPropertiesFile() {
        String newAdminUsername = "newAdminUsername";
        String newAdminPassword = "newAdminPassword";

        adminSeeder.setAdminUsername(newAdminUsername);
        adminSeeder.setAdminPassword(newAdminPassword);

        adminSeeder.seed();

        User admin = userRepository.findByUsername(newAdminUsername).orElse(null);
        assertNotNull(admin);
        assertEquals(newAdminUsername, admin.getUsername());
        assertTrue(new BCryptPasswordEncoder().matches(newAdminPassword, admin.getPassword()));
    }
}

