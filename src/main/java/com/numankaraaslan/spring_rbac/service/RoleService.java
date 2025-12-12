package com.numankaraaslan.spring_rbac.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.numankaraaslan.spring_rbac.model.Role;
import com.numankaraaslan.spring_rbac.repo.RoleRepository;

@Service
public class RoleService
{
	private final RoleRepository roleRepo;

	public RoleService(RoleRepository roleRepo)
	{
		this.roleRepo = roleRepo;
	}

	public Role createRole(String roleName)
	{
		return roleRepo.findByName(roleName).orElseGet(() -> roleRepo.save(new Role(roleName)));
	}

	public Role getRole(String roleName)
	{
		return roleRepo.findByName(roleName).orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
	}

	public List<Role> getRolesByName(Set<String> names)
	{
		return roleRepo.findByNameIn(names);
	}

	public List<Role> findAll()
	{
		return roleRepo.findAll();
	}

	public void deleteRole(String roleName)
	{
		roleRepo.findByName(roleName).ifPresent(roleRepo::delete);
	}

	public void deleteById(UUID id)
	{
		roleRepo.deleteById(id);
	}

	public void save(Role r)
	{
		roleRepo.save(r);
	}

	public Optional<Role> findByName(String rn)
	{
		return roleRepo.findByName(rn);
	}

	public void deleteAllInBatch()
	{
		this.roleRepo.deleteAllInBatch();
	}

	public Optional<Role> findById(UUID id)
	{
		return roleRepo.findById(id);
	}
}