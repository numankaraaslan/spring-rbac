package com.numankaraaslan.spring_rbac.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import com.numankaraaslan.spring_rbac.model.Role;
import com.numankaraaslan.spring_rbac.model.RoleEndpoint;
import com.numankaraaslan.spring_rbac.model.RolePageObject;
import com.numankaraaslan.spring_rbac.repo.RoleEndpointRepository;
import com.numankaraaslan.spring_rbac.repo.RolePageObjectRepository;

@Component
public class DbAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext>
{
	private final RoleEndpointRepository endpointRepo;
	private final RolePageObjectRepository pageObjectRepo;

	public DbAuthorizationManager(RoleEndpointRepository endpointRepo, RolePageObjectRepository pageObjectRepo)
	{
		this.endpointRepo = endpointRepo;
		this.pageObjectRepo = pageObjectRepo;
	}

	@Override
	public @Nullable AuthorizationResult authorize(Supplier<? extends @Nullable Authentication> authentication, RequestAuthorizationContext request)
	{
		Authentication auth = authentication.get();
		String endpointKey = request.getRequest().getRequestURI();
		boolean granted = canAccessEndpoint(auth, endpointKey);
		if (!granted)
		{
		}
		return new AuthorizationDecision(granted);
	}

	//	@Cacheable(cacheNames = "endpointRoles", key = "#endpointKey")
	public boolean canAccessEndpoint(Authentication auth, String endpoint)
	{
		if (!isValidAuth(auth))
		{
			return false;
		}
		Collection<? extends GrantedAuthority> userRoles = auth.getAuthorities();
		List<RoleEndpoint> rows = endpointRepo.findAll(SpecificationUtils.roleEndpointHasEndpointName(endpoint));
		Set<Role> allowedRoles = new HashSet<Role>();
		for (RoleEndpoint re : rows)
		{
			allowedRoles.add(re.getRole());
		}
		return intersects(userRoles, allowedRoles);
	}

	//	@Cacheable(cacheNames = "pageObjectRoles", key = "#pageObjectKey")
	public boolean canAccessPageObject(Authentication auth, String pageObject)
	{
		if (!isValidAuth(auth))
		{
			return false;
		}
		Collection<? extends GrantedAuthority> userRoles = auth.getAuthorities();
		List<RolePageObject> rows = pageObjectRepo.findAll(SpecificationUtils.rolePageObjectHasPageObjectName(pageObject));
		Set<Role> allowedRoles = new HashSet<Role>();
		for (RolePageObject rpo : rows)
		{
			allowedRoles.add(rpo.getRole());
		}
		return intersects(userRoles, allowedRoles);
	}

	//	@CacheEvict(cacheNames = { "endpointRoles", "pageObjectRoles" }, allEntries = true)
	public void clearCaches()
	{
	}

	private boolean isValidAuth(Authentication auth)
	{
		return auth != null && auth.isAuthenticated();
	}

	private boolean intersects(Collection<? extends GrantedAuthority> userRoles, Set<Role> allowedRoles)
	{
		for (GrantedAuthority s : userRoles)
		{
			for (GrantedAuthority a : allowedRoles)
			{
				if (a.getAuthority().equals(s.getAuthority()))
				{
					return true;
				}
			}
		}
		return false;
	}
}