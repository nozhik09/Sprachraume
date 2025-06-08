package com.example.Sprachraume.Role.repository;

import com.example.Sprachraume.Role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByTitle (String title);
}
