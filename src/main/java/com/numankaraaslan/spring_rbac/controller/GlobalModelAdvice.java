package com.numankaraaslan.spring_rbac.controller;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.numankaraaslan.spring_rbac.service.PermissionService;

@ControllerAdvice
public class GlobalModelAdvice
{
	private final PermissionService permissionService;

	public GlobalModelAdvice(PermissionService permissionService)
	{
		this.permissionService = permissionService;
	}

	@ModelAttribute
	public void addStuff(Model model, Authentication auth)
	{
		// I don't want to use "permissionservice" in thymeleaf, too much coupling
		model.addAttribute("canSee_AdminHome", permissionService.canAccessEndpoint(auth, "/admin"));
		model.addAttribute("canSee_Roles", permissionService.canAccessEndpoint(auth, "/admin/roles"));
		model.addAttribute("canSee_RolePages", permissionService.canAccessEndpoint(auth, "/admin/role-pages"));
		model.addAttribute("canSee_RolePageObjects", permissionService.canAccessEndpoint(auth, "/admin/role-pageobjects"));
		model.addAttribute("canSee_MaterialsPage", permissionService.canAccessEndpoint(auth, "/materials"));
		model.addAttribute("canSee_EmployeesPage", permissionService.canAccessEndpoint(auth, "/employees"));
		model.addAttribute("MATERIAL_CREATE", permissionService.canAccessPageObject(auth, "MATERIAL_CREATE"));
		model.addAttribute("MATERIAL_DELETE", permissionService.canAccessPageObject(auth, "MATERIAL_DELETE"));
		model.addAttribute("EMPLOYEE_CREATE", permissionService.canAccessPageObject(auth, "EMPLOYEE_CREATE"));
		model.addAttribute("EMPLOYEE_DELETE", permissionService.canAccessPageObject(auth, "EMPLOYEE_DELETE"));
	}
}