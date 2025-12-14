package com.numankaraaslan.spring_rbac.controller;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.numankaraaslan.spring_rbac.dto.AuthPrincipal;

@ControllerAdvice
public class GlobalModelAdvice
{
	@ModelAttribute
	public void addStuff(Model model, Authentication auth)
	{
		// I don't want to use "myPrincipal" in thymeleaf with sec:authorize, too much coupling
		// but it caused all permissions to be loaded like this, easy to forget!
		// these for links in the nav on the left
		if (auth != null)
		{
			AuthPrincipal myPrincipal = ((AuthPrincipal) auth.getPrincipal());
			model.addAttribute("canSee_AdminHome", myPrincipal.canAccessEndpoint("/admin"));
			model.addAttribute("canSee_Roles", myPrincipal.canAccessEndpoint("/admin/roles"));
			model.addAttribute("canSee_RolePages", myPrincipal.canAccessEndpoint("/admin/role-pages"));
			model.addAttribute("canSee_RolePageObjects", myPrincipal.canAccessEndpoint("/admin/role-pageobjects"));
			model.addAttribute("canSee_MaterialsPage", myPrincipal.canAccessEndpoint("/materials"));
			model.addAttribute("canSee_EmployeesPage", myPrincipal.canAccessEndpoint("/employees"));
			// these will be populated huge in the future
			// only alternative is using some spel in thymeleaf and use authentication.principal bean
			model.addAttribute("MATERIAL_CREATE", myPrincipal.canAccessPageObject("MATERIAL_CREATE"));
			model.addAttribute("MATERIAL_DELETE", myPrincipal.canAccessPageObject("MATERIAL_DELETE"));
			model.addAttribute("MATERIAL_UPDATE", myPrincipal.canAccessPageObject("MATERIAL_UPDATE"));
			model.addAttribute("EMPLOYEE_CREATE", myPrincipal.canAccessPageObject("EMPLOYEE_CREATE"));
			model.addAttribute("EMPLOYEE_DELETE", myPrincipal.canAccessPageObject("EMPLOYEE_DELETE"));
			model.addAttribute("EMPLOYEE_UPDATE", myPrincipal.canAccessPageObject("EMPLOYEE_UPDATE"));
		}
	}
}