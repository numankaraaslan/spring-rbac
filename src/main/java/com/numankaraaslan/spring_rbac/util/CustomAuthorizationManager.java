package com.numankaraaslan.spring_rbac.util;

import java.util.function.Supplier;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import com.numankaraaslan.spring_rbac.dto.AuthPrincipal;

@Component
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext>
{
	// this is authroziation, not authentication !!

	@Override
	public AuthorizationResult authorize(Supplier<? extends @Nullable Authentication> authentication, RequestAuthorizationContext request)
	{
		String endpointKey = request.getRequest().getRequestURI();
		boolean granted = ((AuthPrincipal) authentication.get().getPrincipal()).canAccessEndpoint(endpointKey);
		return new AuthorizationDecision(granted);
	}
}