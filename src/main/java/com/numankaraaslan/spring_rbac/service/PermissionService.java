package com.numankaraaslan.spring_rbac.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.numankaraaslan.spring_rbac.dto.AuthPrincipal;
import com.numankaraaslan.spring_rbac.dto.PermissionSnapshot;

@Service
public class PermissionService
{
	public PermissionSnapshot snapshot(Authentication auth)
	{
		if (auth == null || !auth.isAuthenticated())
		{
			return PermissionSnapshot.empty();
		}
		Object p = auth.getPrincipal();
		if (p instanceof AuthPrincipal ap)
		{
			return new PermissionSnapshot(ap.getAllowedEndpoints(), ap.getAllowedPageObjects());
		}
		return PermissionSnapshot.empty();
	}

	public boolean canAccessEndpoint(Authentication auth, String endpoint)
	{
		if (!isValid(auth))
			return false;

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
			return false;

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

	public void invalidateAll()
	{
	}
}