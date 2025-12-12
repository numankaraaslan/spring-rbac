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
@Table(name = "RolePageObject", uniqueConstraints = { @UniqueConstraint(columnNames = { "role_id", "page_object_id" }) })
public class RolePageObject
{
	@Id
	@UuidGenerator
	private UUID id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "role_id")
	private Role role;

	@ManyToOne(optional = false)
	@JoinColumn(name = "page_object_id")
	private PageObject pageObject;

	public RolePageObject()
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

	public PageObject getPageObject()
	{
		return pageObject;
	}

	public void setPageObject(PageObject pageObject)
	{
		this.pageObject = pageObject;
	}
}
