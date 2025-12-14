package com.numankaraaslan.spring_rbac.util;

import java.util.function.Supplier;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import com.numankaraaslan.spring_rbac.service.PermissionService;

@Component
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext>
{
	private final PermissionService permissionService;

	public CustomAuthorizationManager(PermissionService permissionService)
	{
		this.permissionService = permissionService;
	}

	@Override
	public AuthorizationResult authorize(Supplier<? extends @Nullable Authentication> authentication, RequestAuthorizationContext request)
	{
		Authentication auth = authentication.get();
		String endpointKey = request.getRequest().getRequestURI();
		boolean granted = permissionService.canAccessEndpoint(auth, endpointKey);
		return new AuthorizationDecision(granted);
	}
}