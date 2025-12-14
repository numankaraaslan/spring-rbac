package com.numankaraaslan.spring_rbac.config;

import java.io.IOException;
import java.util.HashSet;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.numankaraaslan.spring_rbac.dto.AuthPrincipal;
import com.numankaraaslan.spring_rbac.model.User;
import com.numankaraaslan.spring_rbac.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AfterLoginHandler implements AuthenticationSuccessHandler
{
	private final UserService userService;

	public AfterLoginHandler(UserService userService)
	{
		this.userService = userService;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
	{
		String username = authentication.getName();
		User user = (User) userService.loadUserByUsername(username);
		// instead of stuffing jpa entities into session, use a custom authentication principal
		// also avoid using your user entity to check if its roles has access to endpoints or pageobjects
		AuthPrincipal principal = new AuthPrincipal(user.getUsername(), user.getRoles());
		// I don't want spring to keep my custom roles in memory, so empty hashset..
		Authentication replaced = new UsernamePasswordAuthenticationToken(principal, null, new HashSet<GrantedAuthority>());
		SecurityContextHolder.getContext().setAuthentication(replaced);
		String ctx = request.getContextPath();
		if (principal.canAccessEndpoint("/admin"))
		{
			response.sendRedirect(ctx + "/admin");
		}
		else
		{
			response.sendRedirect(ctx + "/");
		}
	}
}
