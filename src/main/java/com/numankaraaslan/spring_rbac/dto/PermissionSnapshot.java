package com.numankaraaslan.spring_rbac.dto;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PermissionSnapshot
{
	private final Set<String> allowedEndpoints;
	private final Set<String> allowedPageObjects;

	public PermissionSnapshot(Set<String> allowedEndpoints, Set<String> allowedPageObjects)
	{
		this.allowedEndpoints = allowedEndpoints != null ? Collections.unmodifiableSet(new HashSet<>(allowedEndpoints)) : Collections.emptySet();
		this.allowedPageObjects = allowedPageObjects != null ? Collections.unmodifiableSet(new HashSet<>(allowedPageObjects)) : Collections.emptySet();
	}

	public static PermissionSnapshot empty()
	{
		return new PermissionSnapshot(Collections.emptySet(), Collections.emptySet());
	}

	public boolean canAccessEndpoint(String endpoint)
	{
		return endpoint != null && allowedEndpoints.contains(endpoint);
	}

	public boolean canAccessPageObject(String pageObject)
	{
		return pageObject != null && allowedPageObjects.contains(pageObject);
	}

	public Set<String> getAllowedEndpoints()
	{
		return allowedEndpoints;
	}

	public Set<String> getAllowedPageObjects()
	{
		return allowedPageObjects;
	}
}
