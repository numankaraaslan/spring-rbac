package com.numankaraaslan.spring_rbac.repo;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.numankaraaslan.spring_rbac.model.Role;

public interface RoleRepository extends JpaRepository<Role, UUID>
{
	Optional<Role> findByName(String name);

	List<Role> findByNameIn(Collection<String> names);
}
