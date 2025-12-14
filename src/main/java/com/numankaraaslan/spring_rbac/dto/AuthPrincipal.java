package com.numankaraaslan.spring_rbac.dto;

import java.util.Set;
import java.util.UUID;

import org.springframework.util.AntPathMatcher;

public class AuthPrincipal
{
	private final UUID userId;
	private final String username;

	// what they are
	private final Set<String> roleNames;

	// what they can do
	private final Set<String> allowedEndpoints;
	private final Set<String> allowedPageObjects;

	public AuthPrincipal(UUID userId, String username, Set<String> roleNames, Set<String> allowedEndpoints, Set<String> allowedPageObjects)
	{
		this.userId = userId;
		this.username = username;
		this.roleNames = Set.copyOf(roleNames);
		this.allowedEndpoints = Set.copyOf(allowedEndpoints);
		this.allowedPageObjects = Set.copyOf(allowedPageObjects);
	}

	public UUID getUserId()
	{
		return userId;
	}

	public String getUsername()
	{
		return username;
	}

	public Set<String> getRoleNames()
	{
		return roleNames;
	}

	public Set<String> getAllowedEndpoints()
	{
		return allowedEndpoints;
	}

	public Set<String> getAllowedPageObjects()
	{
		return allowedPageObjects;
	}

	public boolean canAccessEndpoint(String requestUri)
	{
		String path = normalize(requestUri);
		for (String pattern : allowedEndpoints)
		{
			if (new AntPathMatcher().match(normalize(pattern), path))
			{
				return true;
			}
		}
		return false;
	}

	private String normalize(String s)
	{
		if (s == null)
			return "";
		int q = s.indexOf('?');
		String out = (q >= 0) ? s.substring(0, q) : s;
		if (out.length() > 1 && out.endsWith("/"))
			out = out.substring(0, out.length() - 1);
		return out;
	}

	public boolean canAccessPageObject(String key)
	{
		return key != null && allowedPageObjects.contains(key);
	}

	@Override
	public String toString()
	{
		return username;
	}
}
