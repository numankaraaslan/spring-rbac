package com.numankaraaslan.spring_rbac.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.numankaraaslan.spring_rbac.model.Employee;
import com.numankaraaslan.spring_rbac.repo.EmployeeRepository;

@Service
@Transactional
public class EmployeeService
{
	private final EmployeeRepository employeeRepository;

	public EmployeeService(EmployeeRepository employeeRepository)
	{
		this.employeeRepository = employeeRepository;
	}

	public List<Employee> findAll()
	{
		return employeeRepository.findAll();
	}

	public void save(Employee employee)
	{
		employeeRepository.save(employee);
	}

	public void deleteById(UUID id)
	{
		employeeRepository.deleteById(id);
	}

	public Employee findById(UUID id)
	{
		return employeeRepository.findById(id).get();
	}
}
