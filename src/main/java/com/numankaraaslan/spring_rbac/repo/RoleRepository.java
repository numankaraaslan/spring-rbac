package com.numankaraaslan.spring_rbac.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.numankaraaslan.spring_rbac.model.Role;

public interface RoleRepository extends JpaRepository<Role, UUID>
{
}