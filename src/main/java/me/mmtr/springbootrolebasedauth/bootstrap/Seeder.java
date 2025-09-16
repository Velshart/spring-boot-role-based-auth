package me.mmtr.springbootrolebasedauth.bootstrap;

import me.mmtr.springbootrolebasedauth.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;

public abstract class Seeder implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${admin.account.username}")
    protected String adminUsername;

    @Value("${admin.account.password}")
    protected String adminPassword;

    protected final RoleRepository roleRepository;

    public Seeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        seed();
    }

    abstract void seed();

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }
}
