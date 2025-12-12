package com.numankaraaslan.spring_rbac.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.numankaraaslan.spring_rbac.model.Employee;
import com.numankaraaslan.spring_rbac.model.Material;
import com.numankaraaslan.spring_rbac.service.EmployeeService;
import com.numankaraaslan.spring_rbac.service.MaterialService;

@Controller
public class MainController
{
	private final MaterialService materialService;
	private final EmployeeService employeeService;

	public MainController(MaterialService materialService, EmployeeService employeeService)
	{
		this.materialService = materialService;
		this.employeeService = employeeService;
	}

	@GetMapping(path = { "", "/" })
	public ModelAndView wellcomePage(Authentication auth)
	{
		ModelAndView mv = new ModelAndView("wellcome");
		return mv;
	}

	@GetMapping(path = "authexception")
	public ModelAndView authexception()
	{
		return new ModelAndView("authexception");
	}

	@GetMapping("/materials")
	public ModelAndView materialsPage(Authentication auth)
	{
		ModelAndView mv = new ModelAndView("materials");
		List<Material> materialList = materialService.findAll();
		mv.addObject("materialList", materialList);
		return mv;
	}

	@PostMapping("/materials/create")
	public ModelAndView createMaterial(Authentication auth, @RequestParam("name") String name, @RequestParam("description") String description)
	{
		String n = name == null ? "" : name.trim();
		String d = description == null ? "" : description.trim();

		if (!n.isEmpty())
		{
			Material m = new Material();
			m.setName(n);
			m.setDescription(d);
			materialService.save(m);
		}

		return new ModelAndView("redirect:/materials");
	}

	@PostMapping("/materials/delete")
	public ModelAndView deleteMaterial(Authentication auth, @RequestParam("id") UUID id)
	{
		if (id != null)
		{
			materialService.deleteById(id);
		}

		return new ModelAndView("redirect:/materials");
	}

	@GetMapping("/employees")
	public ModelAndView employeesPage(Authentication auth)
	{
		ModelAndView mv = new ModelAndView("employees");
		List<Employee> employeeList = employeeService.findAll();
		mv.addObject("employeeList", employeeList);
		return mv;
	}

	@PostMapping("/employees/create")
	public ModelAndView createEmployee(Authentication auth, @RequestParam("socialSecurityNo") String socialSecurityNo, @RequestParam("name") String name)
	{
		String ssn = socialSecurityNo == null ? "" : socialSecurityNo.trim();
		String n = name == null ? "" : name.trim();

		if (!ssn.isEmpty() && !n.isEmpty())
		{
			Employee e = new Employee();
			e.setSocialSecurityNo(ssn);
			e.setName(n);
			employeeService.save(e);
		}

		return new ModelAndView("redirect:/employees");
	}

	@PostMapping("/employees/delete")
	public ModelAndView deleteEmployee(Authentication auth, @RequestParam("id") UUID id)
	{
		if (id != null)
		{
			employeeService.deleteById(id);
		}

		return new ModelAndView("redirect:/employees");
	}
}