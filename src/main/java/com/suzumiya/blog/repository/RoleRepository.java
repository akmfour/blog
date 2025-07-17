package com.suzumiya.blog.repository;

import com.suzumiya.blog.entity.Role;
import com.suzumiya.blog.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByRoleName(RoleName roleName);
}