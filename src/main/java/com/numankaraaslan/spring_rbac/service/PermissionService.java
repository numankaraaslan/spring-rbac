package com.numankaraaslan.spring_rbac.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import com.numankaraaslan.spring_rbac.dto.AuthPrincipal;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class PermissionService
{
	private final SessionRegistry sessionRegistry;

	public PermissionService(SessionRegistry sessionRegistry)
	{
		this.sessionRegistry = sessionRegistry;
	}

	public boolean canAccessEndpoint(Authentication auth, String endpoint)
	{
		if (!isValid(auth))
		{
			return false;
		}
		Object p = auth.getPrincipal();
		if (p instanceof AuthPrincipal ap)
		{
			return ap.canAccessEndpoint(endpoint);
		}
		return false;
	}

	public boolean canAccessPageObject(Authentication auth, String pageObject)
	{
		if (!isValid(auth))
		{
			return false;
		}
		Object p = auth.getPrincipal();
		if (p instanceof AuthPrincipal ap)
		{
			return ap.canAccessPageObject(pageObject);
		}
		return false;
	}

	private boolean isValid(Authentication auth)
	{
		return auth != null && auth.isAuthenticated();
	}

	public void invalidateAll(HttpServletRequest request)
	{
		String currentSessionId = null;
		if (request != null)
		{
			var session = request.getSession(false);
			if (session != null)
				currentSessionId = session.getId();
		}

		for (Object principal : sessionRegistry.getAllPrincipals())
		{
			for (SessionInformation si : sessionRegistry.getAllSessions(principal, false))
			{
				if (currentSessionId != null && currentSessionId.equals(si.getSessionId()))
				{
					continue;
				}
				si.expireNow();
			}
		}
	}
}