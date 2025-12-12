package com.numankaraaslan.spring_rbac.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee")
public class Employee
{
	@Id
	@GeneratedValue
	private UUID id;

	@Column(name = "social_security_no", nullable = false, unique = true, length = 50)
	private String socialSecurityNo;

	@Column(nullable = false, length = 100)
	private String name;

	public Employee()
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

	public String getSocialSecurityNo()
	{
		return socialSecurityNo;
	}

	public void setSocialSecurityNo(String socialSecurityNo)
	{
		this.socialSecurityNo = socialSecurityNo;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
