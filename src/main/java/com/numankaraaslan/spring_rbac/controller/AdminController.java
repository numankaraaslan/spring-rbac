package com.numankaraaslan.spring_rbac.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.numankaraaslan.spring_rbac.model.Endpoint;
import com.numankaraaslan.spring_rbac.model.PageObject;
import com.numankaraaslan.spring_rbac.model.Role;
import com.numankaraaslan.spring_rbac.model.User;
import com.numankaraaslan.spring_rbac.repo.EndpointRepository;
import com.numankaraaslan.spring_rbac.repo.PageObjectRepository;
import com.numankaraaslan.spring_rbac.service.RoleService;
import com.numankaraaslan.spring_rbac.service.SessionService;
import com.numankaraaslan.spring_rbac.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AdminController
{
	private final RoleService roleService;
	private final EndpointRepository endpointRepo;
	private final PageObjectRepository pageObjectRepo;
	private final UserService userService;
	private final SessionService sessionService;

	public AdminController(RoleService roleService, UserService userService, EndpointRepository endpointRepo, PageObjectRepository pageObjectRepo, SessionService sessionService)
	{
		this.roleService = roleService;
		this.endpointRepo = endpointRepo;
		this.pageObjectRepo = pageObjectRepo;
		this.userService = userService;
		this.sessionService = sessionService;
	}

	@GetMapping(path = "/admin")
	public ModelAndView adminPage()
	{
		ModelAndView mv = new ModelAndView("/admin/admin");
		List<User> userList = userService.findAll();
		mv.addObject("userList", userList);
		List<Role> roleList = roleService.findAll();
		mv.addObject("roleList", roleList);
		return mv;
	}

	@PostMapping("/admin/assign-roles-to-user")
	public ModelAndView assignRolesToUser(@RequestParam("username") UUID userId, @RequestParam(value = "roleIds") List<UUID> roleIds)
	{
		userService.assignRolesToUser(userId, roleIds);
		return new ModelAndView("redirect:/admin");
	}

	@GetMapping("/admin/roles")
	public ModelAndView rolesPage()
	{
		ModelAndView mv = new ModelAndView("admin/roles");
		List<Role> roleList = roleService.findAll();
		mv.addObject("roleList", roleList);
		return mv;
	}

	@PostMapping("/admin/roles/create")
	public ModelAndView createRole(HttpServletRequest request, @RequestParam("name") String name)
	{
		String roleName = name == null ? "" : name.trim();
		Role r = new Role();
		r.setName(roleName);
		roleService.save(r);
		sessionService.invalidateAll(request);
		return new ModelAndView("redirect:/admin/roles");
	}

	@PostMapping("/admin/roles/delete")
	public ModelAndView deleteRole(HttpServletRequest request, @RequestParam("id") UUID id)
	{
		roleService.deleteById(id);
		sessionService.invalidateAll(request);
		return new ModelAndView("redirect:/admin/roles");
	}

	@GetMapping("/admin/role-pages")
	public ModelAndView rolePagesPage()
	{
		ModelAndView mv = new ModelAndView("admin/role_pages");
		List<Role> roleList = roleService.findAll();
		mv.addObject("roleList", roleList);
		mv.addObject("endpoints", endpointRepo.findAll());
		return mv;
	}

	@PostMapping("/admin/role-pages/create")
	public ModelAndView createRolePage(HttpServletRequest request, @RequestParam("roleID") UUID roleID, @RequestParam(value = "endpointIds") List<UUID> endpointIds)
	{
		Role role = roleService.findById(roleID).get();
		role.getEndpoints().clear();
		for (int i = 0; i < endpointIds.size(); i++)
		{
			Endpoint endpointEntity = endpointRepo.findById(endpointIds.get(i)).get();
			role.getEndpoints().add(endpointEntity);
			roleService.save(role);
		}
		sessionService.invalidateAll(request);
		return new ModelAndView("redirect:/admin/role-pages");
	}

	@PostMapping("/admin/role-pages/delete")
	public ModelAndView deleteRolePage(HttpServletRequest request, @RequestParam("roleId") UUID roleId, @RequestParam("endpointId") UUID endpointId)
	{
		Role role = roleService.findById(roleId).get();
		role.getEndpoints().removeIf(item -> item.getId().equals(endpointId));
		roleService.save(role);
		sessionService.invalidateAll(request);
		return new ModelAndView("redirect:/admin/role-pages");
	}

	@GetMapping("/admin/role-pageobjects")
	public ModelAndView rolePageObjectsPage()
	{
		ModelAndView mv = new ModelAndView("admin/role_pageobjects");
		List<Role> roleList = roleService.findAll();
		mv.addObject("roleList", roleList);
		mv.addObject("pageObjects", pageObjectRepo.findAll());
		return mv;
	}

	@PostMapping("/admin/role-pageobjects/create")
	public ModelAndView createRolePageObject(HttpServletRequest request, @RequestParam("roleID") UUID roleID, @RequestParam(value = "pageObjectIds") List<UUID> pageObjectIds)
	{
		Role role = roleService.findById(roleID).get();
		role.getPageObjects().clear();
		for (int i = 0; i < pageObjectIds.size(); i++)
		{
			PageObject pageObj = pageObjectRepo.findById(pageObjectIds.get(i)).get();
			role.getPageObjects().add(pageObj);
			roleService.save(role);
		}
		sessionService.invalidateAll(request);
		return new ModelAndView("redirect:/admin/role-pageobjects");
	}

	@PostMapping("/admin/role-pageobjects/delete")
	public ModelAndView deleteRolePageObject(HttpServletRequest request, @RequestParam("roleId") UUID roleId, @RequestParam("pageobjectId") UUID pageobjectId)
	{
		Role role = roleService.findById(roleId).get();
		role.getPageObjects().removeIf(item -> item.getId().equals(pageobjectId));
		roleService.save(role);
		sessionService.invalidateAll(request);
		return new ModelAndView("redirect:/admin/role-pageobjects");
	}
}