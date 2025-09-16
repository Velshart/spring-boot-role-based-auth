package me.mmtr.springbootrolebasedauth.bootstrap;

import me.mmtr.springbootrolebasedauth.model.Role;
import me.mmtr.springbootrolebasedauth.enums.RoleName;
import me.mmtr.springbootrolebasedauth.repository.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class RoleSeeder extends Seeder implements ApplicationListener<ContextRefreshedEvent> {

    public RoleSeeder(RoleRepository roleRepository) {
        super(roleRepository);
    }

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        super.onApplicationEvent(event);
    }

    @Override
    void seed() {
        Arrays.stream(RoleName.values()).forEach(roleName -> {
            Optional<Role> roleOptional = roleRepository.findByRoleName(roleName);

            if (roleOptional.isEmpty()) {
                Role role = new Role();
                role.setRoleName(roleName);
                roleRepository.save(role);
            }
        });
    }
}
