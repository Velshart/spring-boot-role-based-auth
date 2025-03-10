package me.mmtr.springbootrolebasedauth.repository;

import me.mmtr.springbootrolebasedauth.data.Role;
import me.mmtr.springbootrolebasedauth.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(RoleName roleName);
}
