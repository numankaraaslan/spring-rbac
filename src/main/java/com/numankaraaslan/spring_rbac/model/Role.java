package com.numankaraaslan.spring_rbac.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Endpoint> endpoints = new HashSet<Endpoint>();

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<PageObject> pageObjects = new HashSet<PageObject>();

	public Set<Endpoint> getEndpoints()
	{
		return endpoints;
	}

	public Set<PageObject> getPageObjects()
	{
		return pageObjects;
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
