package com.numankaraaslan.spring_rbac.config;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.numankaraaslan.spring_rbac.dto.AuthPrincipal;
import com.numankaraaslan.spring_rbac.model.Role;
import com.numankaraaslan.spring_rbac.model.RoleEndpoint;
import com.numankaraaslan.spring_rbac.model.RolePageObject;
import com.numankaraaslan.spring_rbac.model.User;
import com.numankaraaslan.spring_rbac.repo.RoleEndpointRepository;
import com.numankaraaslan.spring_rbac.repo.RolePageObjectRepository;
import com.numankaraaslan.spring_rbac.repo.UserRepository;
import com.numankaraaslan.spring_rbac.service.PermissionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RedirectSuccessHandler implements AuthenticationSuccessHandler
{
	private final PermissionService permissionService;
	private final UserRepository userRepo;
	private final RoleEndpointRepository roleEndpointRepo;
	private final RolePageObjectRepository rolePageObjectRepo;

	public RedirectSuccessHandler(PermissionService permissionService, UserRepository userRepo, RolePageObjectRepository rolePageObjectRepo, RoleEndpointRepository roleEndpointRepo)
	{
		this.permissionService = permissionService;
		this.userRepo = userRepo;
		this.roleEndpointRepo = roleEndpointRepo;
		this.rolePageObjectRepo = rolePageObjectRepo;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
	{
		String username = authentication.getName();
		User user = userRepo.findByUsername(username).orElseThrow();
		Set<String> roleNames = new HashSet<>();
		for (Role r : user.getRoles())
		{
			if (r != null && r.getName() != null)
			{
				roleNames.add(r.getName());
			}
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
		// instead of stuffing jpa entities into session, use a custom authentication principal
		AuthPrincipal principal = new AuthPrincipal(user.getId(), user.getUsername(), roleNames, endpoints, pageObjects);
		// I don't want spring to keep my custom roles in memory, so empty hashset..
		Authentication replaced = new UsernamePasswordAuthenticationToken(principal, null, new HashSet<GrantedAuthority>());
		SecurityContextHolder.getContext().setAuthentication(replaced);
		String ctx = request.getContextPath();
		if (permissionService.canAccessEndpoint(authentication, "/admin"))
		{
			response.sendRedirect(ctx + "/admin");
		}
		else
		{
			response.sendRedirect(ctx + "/");
		}
	}
}
