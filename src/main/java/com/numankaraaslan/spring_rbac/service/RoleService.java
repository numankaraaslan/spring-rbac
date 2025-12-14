package com.numankaraaslan.spring_rbac.service;

import java.util.List;
import java.util.Optional;
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

	public List<Role> findAll()
	{
		return roleRepo.findAll();
	}

	public void deleteById(UUID id)
	{
		roleRepo.deleteById(id);
	}

	public void save(Role r)
	{
		roleRepo.save(r);
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