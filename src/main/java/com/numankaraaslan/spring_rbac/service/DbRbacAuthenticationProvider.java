package com.numankaraaslan.spring_rbac.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.numankaraaslan.spring_rbac.dto.AuthPrincipal;
import com.numankaraaslan.spring_rbac.dto.PermissionSnapshot;
import com.numankaraaslan.spring_rbac.model.Role;
import com.numankaraaslan.spring_rbac.model.RoleEndpoint;
import com.numankaraaslan.spring_rbac.model.RolePageObject;
import com.numankaraaslan.spring_rbac.model.User;
import com.numankaraaslan.spring_rbac.repo.RoleEndpointRepository;
import com.numankaraaslan.spring_rbac.repo.RolePageObjectRepository;
import com.numankaraaslan.spring_rbac.repo.UserRepository;

@Component
public class DbRbacAuthenticationProvider implements AuthenticationProvider
{
	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	private final RoleEndpointRepository roleEndpointRepo;
	private final RolePageObjectRepository rolePageObjectRepo;

	public DbRbacAuthenticationProvider(UserRepository userRepo, PasswordEncoder passwordEncoder, RolePageObjectRepository rolePageObjectRepo, RoleEndpointRepository roleEndpointRepo)
	{
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.roleEndpointRepo = roleEndpointRepo;
		this.rolePageObjectRepo = rolePageObjectRepo;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException
	{
		String username = authentication.getName();
		String rawPassword = authentication.getCredentials() == null ? "" : authentication.getCredentials().toString();
		User user = userRepo.findByUsername(username).orElseThrow(() -> new BadCredentialsException("Bad credentials"));
		if (!passwordEncoder.matches(rawPassword, user.getPassword()))
		{
			throw new BadCredentialsException("Bad credentials");
		}
		Set<String> roleNames = new HashSet<>();
		for (Role r : user.getRoles())
		{
			if (r != null && r.getName() != null)
			{
				roleNames.add(r.getName());
			}
		}
		PermissionSnapshot pd = loadForRoles(roleNames);
		AuthPrincipal principal = new AuthPrincipal(user.getId(), user.getUsername(), roleNames, pd.getAllowedEndpoints(), pd.getAllowedPageObjects());
		List<SimpleGrantedAuthority> authorities = roleNames.stream().map(SimpleGrantedAuthority::new).toList();
		return new UsernamePasswordAuthenticationToken(principal, null, authorities);
	}

	private PermissionSnapshot loadForRoles(Set<String> roleNames)
	{
		if (roleNames == null || roleNames.isEmpty())
		{
			return new PermissionSnapshot(Set.of(), Set.of());
		}
		List<RoleEndpoint> res = roleEndpointRepo.findAllByRole_NameIn(roleNames);
		List<RolePageObject> rpos = rolePageObjectRepo.findAllByRole_NameIn(roleNames);
		Set<String> endpoints = new HashSet<>();
		for (RoleEndpoint re : res)
		{
			if (re.getEndpoint() != null && re.getEndpoint().getName() != null)
			{
				endpoints.add(re.getEndpoint().getName());
			}
		}
		Set<String> pageObjects = new HashSet<>();
		for (RolePageObject rpo : rpos)
		{
			if (rpo.getPageObject() != null && rpo.getPageObject().getName() != null)
			{
				pageObjects.add(rpo.getPageObject().getName());
			}
		}
		return new PermissionSnapshot(Set.copyOf(endpoints), Set.copyOf(pageObjects));
	}

	@Override
	public boolean supports(Class<?> authentication)
	{
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}