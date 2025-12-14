package com.numankaraaslan.spring_rbac.model;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Endpoint")
public class Endpoint
{
	@Id
	@UuidGenerator
	private UUID id;

	@Column(name = "path", nullable = false, unique = true)
	private String path;

	@Column(name = "description")
	private String description;

	public Endpoint()
	{
	}

	public UUID getId()
	{
		return id;
	}

	public void setId(UUID id)
	{
		this.id = id;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	@Override
	public String toString()
	{
		return path;
	}
}
