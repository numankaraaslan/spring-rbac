package com.numankaraaslan.spring_rbac.model;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "RoleEndpoint", uniqueConstraints = { @UniqueConstraint(columnNames = { "role_id", "endpoint_id" }) })
public class RoleEndpoint
{
	@Id
	@UuidGenerator
	private UUID id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "role_id", nullable = false)
	private Role role;

	@ManyToOne(optional = false)
	@JoinColumn(name = "endpoint_id")
	private Endpoint endpoint;

	public RoleEndpoint()
	{
	}

	public UUID getId()
	{
		return id;
	}

	public Role getRole()
	{
		return role;
	}

	public void setRole(Role role)
	{
		this.role = role;
	}

	public Endpoint getEndpoint()
	{
		return endpoint;
	}

	public void setEndpoint(Endpoint endpoint)
	{
		this.endpoint = endpoint;
	}
}
