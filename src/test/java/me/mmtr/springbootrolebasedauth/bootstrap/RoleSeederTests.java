package me.mmtr.springbootrolebasedauth.bootstrap;

import me.mmtr.springbootrolebasedauth.enums.RoleName;
import me.mmtr.springbootrolebasedauth.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestEntityManager
public class RoleSeederTests {

    @Autowired
    RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        Seeder seeder = new RoleSeeder(roleRepository);
        seeder.seed();
    }

    @Test
    public void databaseShouldContainAllRequiredRoles() {
        assertTrue(roleRepository.findByRoleName(RoleName.USER).isPresent());
        assertTrue(roleRepository.findByRoleName(RoleName.ADMIN).isPresent());
    }
}
