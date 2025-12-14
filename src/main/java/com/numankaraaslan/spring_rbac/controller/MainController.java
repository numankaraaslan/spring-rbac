package com.numankaraaslan.spring_rbac.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping(path = { "", "/", "wellcome" })
	public ModelAndView wellcomePage()
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
	public ModelAndView materialsPage()
	{
		ModelAndView mv = new ModelAndView("materials");
		List<Material> materialList = materialService.findAll();
		mv.addObject("materialList", materialList);
		mv.addObject(new Material());
		return mv;
	}

	@GetMapping("/materials/update/{id}")
	public ModelAndView materialsUpdatePage(@PathVariable("id") UUID id)
	{
		ModelAndView mv = new ModelAndView("materialsUpdate");
		Material empty = materialService.findById(id);
		mv.addObject("material", empty);
		return mv;
	}

	@PostMapping("/materials/update")
	public ModelAndView materialsUpdatePage(@ModelAttribute Material material)
	{
		materialService.save(material);
		return new ModelAndView("redirect:/materials");
	}

	@PostMapping("/materials/create")
	public ModelAndView createMaterial(@ModelAttribute Material material)
	{
		materialService.save(material);
		return new ModelAndView("redirect:/materials");
	}

	@PostMapping("/materials/delete")
	public ModelAndView deleteMaterial(@RequestParam("id") UUID id)
	{
		if (id != null)
		{
			materialService.deleteById(id);
		}

		return new ModelAndView("redirect:/materials");
	}

	@GetMapping("/employees")
	public ModelAndView employeesPage()
	{
		ModelAndView mv = new ModelAndView("employees");
		List<Employee> employeeList = employeeService.findAll();
		mv.addObject("employeeList", employeeList);
		mv.addObject(new Employee());
		return mv;
	}

	@GetMapping("/employees/update/{id}")
	public ModelAndView employeesUpdatePage(@PathVariable("id") UUID id)
	{
		ModelAndView mv = new ModelAndView("employeesUpdate");
		Employee empty = employeeService.findById(id);
		mv.addObject("employee", empty);
		return mv;
	}

	@PostMapping("/employees/update")
	public ModelAndView employeesUpdatePage(@ModelAttribute Employee employee)
	{
		employeeService.save(employee);
		return new ModelAndView("redirect:/employees");
	}

	@PostMapping("/employees/create")
	public ModelAndView createEmployee(@ModelAttribute Employee employee)
	{
		employeeService.save(employee);
		return new ModelAndView("redirect:/employees");
	}

	@PostMapping("/employees/delete")
	public ModelAndView deleteEmployee(@RequestParam("id") UUID id)
	{
		if (id != null)
		{
			employeeService.deleteById(id);
		}

		return new ModelAndView("redirect:/employees");
	}
}