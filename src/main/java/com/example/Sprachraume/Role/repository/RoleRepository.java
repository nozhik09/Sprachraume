package com.example.Sprachraume.Role.repository;

import com.example.Sprachraume.Role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByTitle (String title);
}
