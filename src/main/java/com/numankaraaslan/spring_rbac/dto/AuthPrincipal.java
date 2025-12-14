package com.numankaraaslan.spring_rbac.dto;

import java.security.Principal;
import java.util.Set;

import org.springframework.util.AntPathMatcher;

import com.numankaraaslan.spring_rbac.model.Endpoint;
import com.numankaraaslan.spring_rbac.model.PageObject;
import com.numankaraaslan.spring_rbac.model.Role;

// this is in session, so no need to go to db everytime
public class AuthPrincipal implements Principal
{
	private final String username;
	private final Set<Role> roles;

	public AuthPrincipal(String username, Set<Role> roles)
	{
		this.username = username;
		this.roles = roles;
	}

	public boolean canAccessEndpoint(String requestUri)
	{
		String path = normalize(requestUri);
		for (Role role : roles)
		{
			for (Endpoint endpoint : role.getEndpoints())
			{
				if (new AntPathMatcher().match(normalize(endpoint.getPath()), path))
				{
					return true;
				}
			}
		}
		return false;
	}

	private String normalize(String s)
	{
		if (s == null)
		{
			return "";
		}
		int q = s.indexOf('?');
		String out = (q >= 0) ? s.substring(0, q) : s;
		if (out.length() > 1 && out.endsWith("/"))
		{
			out = out.substring(0, out.length() - 1);
		}
		return out;
	}

	public boolean canAccessPageObject(String key)
	{
		for (Role role : roles)
		{
			for (PageObject pageObject : role.getPageObjects())
			{
				if (pageObject.getName().equals(key))
				{
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getName()
	{
		// so that you can use ${#authentication.principal.getName()} or ${#authentication.getName()} in thymeleaf
		return username;
	}
}
