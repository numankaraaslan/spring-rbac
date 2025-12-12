package com.numankaraaslan.spring_rbac.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		User user = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
		return user;
	}

	public User createUser(String username, String rawPassword, Set<String> roleNames)
	{
		List<Role> roles = roleService.getRolesByName(roleNames);
		User user = new User();
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(rawPassword));
		user.setRoles(new HashSet<>(roles));

		return userRepo.save(user);
	}

	public void addRole(String username, String roleName)
	{
		User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found: " + username));
		Role role = roleService.getRole(roleName);
		user.getRoles().add(role);
		userRepo.save(user);
	}

	public void removeRole(String username, String roleName)
	{
		User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found: " + username));
		user.getRoles().removeIf(r -> r.getName().equals(roleName));
		userRepo.save(user);
	}

	public void setRoles(String username, Set<String> newRoleNames)
	{
		User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found: " + username));
		List<Role> roles = roleService.getRolesByName(newRoleNames);
		user.setRoles(new HashSet<>(roles));
		userRepo.save(user);
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

	public void assignRolesToUser(String username, List<UUID> roleIds)
	{
		User user = userRepo.findByUsername(username).orElse(null);
		if (user == null)
		{
			return;
		}

		// FULL REPLACE
		user.getRoles().clear();

		if (roleIds != null)
		{
			Set<UUID> uniqueIds = new HashSet<UUID>(roleIds);

			for (UUID id : uniqueIds)
			{
				if (id == null)
				{
					continue;
				}

				Role role = roleService.findById(id).orElse(null);
				if (role != null)
				{
					user.getRoles().add(role);
				}
			}
		}
		userRepo.save(user);
	}
}