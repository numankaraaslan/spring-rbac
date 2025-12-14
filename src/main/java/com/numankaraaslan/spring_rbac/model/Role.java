package com.numankaraaslan.spring_rbac.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Role")
public class Role implements GrantedAuthority
{
	private static final long serialVersionUID = -553248307592159012L;

	@Id
	@UuidGenerator
	private UUID id;

	@Column(name = "name", unique = true, nullable = false)
	private String name;

	@OneToMany(mappedBy = "role", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<RoleEndpoint> roleEndpoints = new HashSet<RoleEndpoint>();

	@OneToMany(mappedBy = "role", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<RolePageObject> rolePageObjects = new HashSet<RolePageObject>();

	public Set<RoleEndpoint> getRoleEndpoints()
	{
		return roleEndpoints;
	}

	public Set<RolePageObject> getRolePageObjects()
	{
		return rolePageObjects;
	}

	public Role()
	{
	}

	public Role(String name)
	{
		this.name = name;
	}

	public UUID getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String getAuthority()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return name;
	}
}
