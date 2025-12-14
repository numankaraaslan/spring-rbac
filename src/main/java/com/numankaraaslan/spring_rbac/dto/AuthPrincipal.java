package com.numankaraaslan.spring_rbac.dto;

import java.util.Set;
import java.util.UUID;

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

	public boolean canAccessEndpoint(String endpoint)
	{
		return endpoint != null && allowedEndpoints.contains(endpoint);
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
