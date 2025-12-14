package com.numankaraaslan.spring_rbac.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.numankaraaslan.spring_rbac.model.Role;
import com.numankaraaslan.spring_rbac.model.User;
import com.numankaraaslan.spring_rbac.repo.UserRepository;

@Service
public class UserService implements UserDetailsService
{
	private final UserRepository userRepo;
	private final RoleService roleService;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepo, RoleService roleService, PasswordEncoder passwordEncoder)
	{
		this.userRepo = userRepo;
		this.roleService = roleService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username)
	{
		return userRepo.findByUsername(username).get();
	}

	public void deleteAllInBatch()
	{
		userRepo.deleteAllInBatch();
	}

	public void save(User u)
	{
		u.setPassword(this.passwordEncoder.encode(u.getPassword()));
		userRepo.save(u);
	}

	public List<User> findAll()
	{
		return userRepo.findAll();
	}

	public void assignRolesToUser(UUID userId, List<UUID> roleIds)
	{
		User user = userRepo.findById(userId).get();
		user.getRoles().clear();
		Set<UUID> uniqueIds = new HashSet<UUID>(roleIds);
		for (UUID id : uniqueIds)
		{
			Role role = roleService.findById(id).get();
			user.getRoles().add(role);
		}
		userRepo.save(user);
	}
}