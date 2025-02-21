package me.mmtr.springbootrolebasedauth.bootstrap;

import me.mmtr.springbootrolebasedauth.data.Role;
import me.mmtr.springbootrolebasedauth.enums.RoleName;
import me.mmtr.springbootrolebasedauth.repository.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        loadRolesToDatabase();
    }

    private void loadRolesToDatabase() {
        Arrays.stream(RoleName.values())
                .forEach(roleName -> {
                    Optional<Role> roleOptional = roleRepository.findByRoleName(roleName);
                    if (roleOptional.isEmpty()) {
                        Role role = new Role();
                        role.setRoleName(roleName);

                        roleRepository.save(role);
                    }
                });
    }
}
