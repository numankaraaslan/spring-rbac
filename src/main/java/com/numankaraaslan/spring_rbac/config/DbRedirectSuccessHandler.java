package com.numankaraaslan.spring_rbac.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.numankaraaslan.spring_rbac.service.PermissionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DbRedirectSuccessHandler implements AuthenticationSuccessHandler
{
	private final PermissionService permissionService;

	public DbRedirectSuccessHandler(PermissionService permissionService)
	{
		this.permissionService = permissionService;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
	{
		// Optional: contextPath-safe redirect
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
