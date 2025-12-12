package com.numankaraaslan.spring_rbac.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.numankaraaslan.spring_rbac.model.RoleEndpoint;
import com.numankaraaslan.spring_rbac.model.RolePageObject;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public final class SpecificationUtils
{
	public static Specification<RoleEndpoint> roleEndpointHasEndpointName(final String endpointName)
	{
		return new Specification<RoleEndpoint>()
		{
			@Override
			public Predicate toPredicate(Root<RoleEndpoint> root, CriteriaQuery<?> query, CriteriaBuilder cb)
			{
				// root.get("endpoint") is an Endpoint entity
				Path<String> namePath = root.get("endpoint").get("name");
				return cb.equal(namePath, endpointName);
			}
		};
	}

	public static Specification<RoleEndpoint> roleEndpointHasRoleName(final String roleName)
	{
		return new Specification<RoleEndpoint>()
		{
			@Override
			public Predicate toPredicate(Root<RoleEndpoint> root, CriteriaQuery<?> query, CriteriaBuilder cb)
			{
				// root.get("role") is a Role entity
				Path<String> namePath = root.get("role").get("name");
				return cb.equal(namePath, roleName);
			}
		};
	}

	public static Specification<RoleEndpoint> roleEndpointEndpointNameIn(final Collection<String> endpointNames)
	{
		return new Specification<RoleEndpoint>()
		{
			@Override
			public Predicate toPredicate(Root<RoleEndpoint> root, CriteriaQuery<?> query, CriteriaBuilder cb)
			{
				if (endpointNames == null || endpointNames.isEmpty())
				{
					return cb.conjunction();
				}
				Path<String> namePath = root.get("endpoint").get("name");
				return namePath.in(endpointNames);
			}
		};
	}

	public static Specification<RoleEndpoint> roleEndpointRoleNameIn(final Collection<String> roleNames)
	{
		return new Specification<RoleEndpoint>()
		{
			@Override
			public Predicate toPredicate(Root<RoleEndpoint> root, CriteriaQuery<?> query, CriteriaBuilder cb)
			{
				if (roleNames == null || roleNames.isEmpty())
				{
					return cb.conjunction();
				}
				Path<String> namePath = root.get("role").get("name");
				return namePath.in(roleNames);
			}
		};
	}

	// -------------------------------------------------
	// RolePageObject specifications
	// RolePageObject has:
	//   Role role;
	//   PageObject pageObject;
	// and we match by role.name and pageObject.name
	// -------------------------------------------------

	public static Specification<RolePageObject> rolePageObjectHasPageObjectName(final String pageObjectName)
	{
		return new Specification<RolePageObject>()
		{
			@Override
			public Predicate toPredicate(Root<RolePageObject> root, CriteriaQuery<?> query, CriteriaBuilder cb)
			{
				Path<String> namePath = root.get("pageObject").get("name");
				return cb.equal(namePath, pageObjectName);
			}
		};
	}

	public static Specification<RolePageObject> rolePageObjectHasRoleName(final String roleName)
	{
		return new Specification<RolePageObject>()
		{
			@Override
			public Predicate toPredicate(Root<RolePageObject> root, CriteriaQuery<?> query, CriteriaBuilder cb)
			{
				Path<String> namePath = root.get("role").get("name");
				return cb.equal(namePath, roleName);
			}
		};
	}

	public static Specification<RolePageObject> rolePageObjectPageObjectNameIn(final Collection<String> pageObjectNames)
	{
		return new Specification<RolePageObject>()
		{
			@Override
			public Predicate toPredicate(Root<RolePageObject> root, CriteriaQuery<?> query, CriteriaBuilder cb)
			{
				if (pageObjectNames == null || pageObjectNames.isEmpty())
				{
					return cb.conjunction();
				}
				Path<String> namePath = root.get("pageObject").get("name");
				return namePath.in(pageObjectNames);
			}
		};
	}

	public static Specification<RolePageObject> rolePageObjectRoleNameIn(final Collection<String> roleNames)
	{
		return new Specification<RolePageObject>()
		{
			@Override
			public Predicate toPredicate(Root<RolePageObject> root, CriteriaQuery<?> query, CriteriaBuilder cb)
			{
				if (roleNames == null || roleNames.isEmpty())
				{
					return cb.conjunction();
				}
				Path<String> namePath = root.get("role").get("name");
				return namePath.in(roleNames);
			}
		};
	}

	// -------------------------------------------------
	// Generic helper: AND-combine specs ignoring nulls
	// -------------------------------------------------

	public static <T> Specification<T> whereAllOf(@SuppressWarnings("unchecked") final Specification<T>... specs)
	{
		return new Specification<T>()
		{
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb)
			{
				List<Predicate> predicates = new ArrayList<Predicate>();

				if (specs != null)
				{
					for (int i = 0; i < specs.length; i++)
					{
						Specification<T> s = specs[i];
						if (s == null)
						{
							continue;
						}

						Predicate p = s.toPredicate(root, query, cb);
						if (p != null)
						{
							predicates.add(p);
						}
					}
				}

				if (predicates.isEmpty())
				{
					return cb.conjunction();
				}

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
}
