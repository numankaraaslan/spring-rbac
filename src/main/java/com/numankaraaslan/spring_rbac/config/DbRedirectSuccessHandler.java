package com.numankaraaslan.spring_rbac.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.numankaraaslan.spring_rbac.util.DbAuthorizationManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DbRedirectSuccessHandler implements AuthenticationSuccessHandler
{
	private final DbAuthorizationManager dbAuthManager;

	public DbRedirectSuccessHandler(DbAuthorizationManager dbAuthManager)
	{
		this.dbAuthManager = dbAuthManager;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
	{
		if (dbAuthManager.canAccessEndpoint(authentication, "/admin"))
		{
			response.sendRedirect("/admin");
		}
		else
		{
			response.sendRedirect("/");
		}
	}
}