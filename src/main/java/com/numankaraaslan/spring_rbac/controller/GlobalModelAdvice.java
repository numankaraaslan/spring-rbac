package com.numankaraaslan.spring_rbac.controller;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.numankaraaslan.spring_rbac.util.DbAuthorizationManager;

@ControllerAdvice
public class GlobalModelAdvice
{
	private final DbAuthorizationManager authz;

	public GlobalModelAdvice(DbAuthorizationManager authz)
	{
		this.authz = authz;
	}

	@ModelAttribute
	public void addStuff(Model model, Authentication auth)
	{
		model.addAttribute("canSee_AdminHome", authz.canAccessEndpoint(auth, "/admin"));
		model.addAttribute("canSee_Roles", authz.canAccessEndpoint(auth, "/admin/roles"));
		model.addAttribute("canSee_RolePages", authz.canAccessEndpoint(auth, "/admin/role-pages"));
		model.addAttribute("canSee_RolePageObjects", authz.canAccessEndpoint(auth, "/admin/role-pageobjects"));
		model.addAttribute("canSee_MaterialsPage", authz.canAccessEndpoint(auth, "/materials"));
		model.addAttribute("canSee_EmployeesPage", authz.canAccessEndpoint(auth, "/employees"));
		model.addAttribute("MATERIAL_CREATE", authz.canAccessPageObject(auth, "MATERIAL_CREATE"));
		model.addAttribute("MATERIAL_DELETE", authz.canAccessPageObject(auth, "MATERIAL_DELETE"));
		model.addAttribute("EMPLOYEE_CREATE", authz.canAccessPageObject(auth, "EMPLOYEE_CREATE"));
		model.addAttribute("EMPLOYEE_DELETE", authz.canAccessPageObject(auth, "EMPLOYEE_DELETE"));
	}
}