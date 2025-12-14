package com.numankaraaslan.spring_rbac.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.numankaraaslan.spring_rbac.model.Endpoint;
import com.numankaraaslan.spring_rbac.model.PageObject;
import com.numankaraaslan.spring_rbac.model.Role;
import com.numankaraaslan.spring_rbac.model.RoleEndpoint;
import com.numankaraaslan.spring_rbac.model.RolePageObject;
import com.numankaraaslan.spring_rbac.model.User;
import com.numankaraaslan.spring_rbac.repo.EndpointRepository;
import com.numankaraaslan.spring_rbac.repo.PageObjectRepository;
import com.numankaraaslan.spring_rbac.repo.RoleEndpointRepository;
import com.numankaraaslan.spring_rbac.repo.RolePageObjectRepository;
import com.numankaraaslan.spring_rbac.service.PermissionService;
import com.numankaraaslan.spring_rbac.service.PermissionService;
import com.numankaraaslan.spring_rbac.service.RoleService;
import com.numankaraaslan.spring_rbac.service.UserService;

@Controller
public class AdminController
{
	private final RoleService roleService;
	private final EndpointRepository endpointRepo;
	private final PageObjectRepository pageObjectRepo;
	private final UserService userService;
	private final RolePageObjectRepository rolePageObjectRepo;
	private final RoleEndpointRepository roleEndpointRepo;
	private final PermissionService permissionService;

	public AdminController(RoleService roleService, UserService userService, RolePageObjectRepository rolePageObjectRepo, RoleEndpointRepository roleEndpointRepo, EndpointRepository endpointRepo, PageObjectRepository pageObjectRepo, PermissionService permissionService)
	{
		this.roleService = roleService;
		this.endpointRepo = endpointRepo;
		this.pageObjectRepo = pageObjectRepo;
		this.userService = userService;
		this.rolePageObjectRepo = rolePageObjectRepo;
		this.roleEndpointRepo = roleEndpointRepo;
		this.permissionService = permissionService;
	}

	@GetMapping(path = "/admin")
	public ModelAndView adminPage(Authentication auth)
	{
		ModelAndView mv = new ModelAndView("/admin/admin");
		List<User> userList = userService.findAll();
		mv.addObject("userList", userList);
		List<Role> roleList = roleService.findAll();
		mv.addObject("roleList", roleList);
		return mv;
	}

	@PostMapping("/admin/assign-roles-to-user")
	public ModelAndView assignRolesToUser(@RequestParam("username") String username, @RequestParam(value = "roleIds", required = false) List<UUID> roleIds)
	{
		String u = username == null ? "" : username.trim();
		if (!u.isEmpty())
		{
			userService.assignRolesToUser(u, roleIds);
		}
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
	public ModelAndView createRole(@RequestParam("name") String name)
	{
		String roleName = name == null ? "" : name.trim();
		if (!roleName.isEmpty())
		{
			Role r = new Role();
			r.setName(roleName);
			roleService.save(r);
			RoleEndpoint re = new RoleEndpoint();
			re.setRole(roleService.getRole(roleName));
			re.setEndpoint(endpointRepo.findByName("/").get());
			roleEndpointRepo.save(re);
			permissionService.invalidateAll();
		}
		return new ModelAndView("redirect:/admin/roles");
	}

	@PostMapping("/admin/roles/delete")
	public ModelAndView deleteRole(@RequestParam("id") UUID id)
	{
		if (id != null)
		{
			roleService.deleteById(id);
			permissionService.invalidateAll();
		}
		return new ModelAndView("redirect:/admin/roles");
	}

	@GetMapping("/admin/role-pages")
	public ModelAndView rolePagesPage()
	{
		ModelAndView mv = new ModelAndView("admin/role_pages");
		List<Role> roleList = roleService.findAll();
		List<RoleEndpoint> roleEndpointList = roleEndpointRepo.findAll();
		mv.addObject("roleList", roleList);
		mv.addObject("roleEndpointList", roleEndpointList);
		mv.addObject("endpoints", endpointRepo.findAll());
		return mv;
	}

	@PostMapping("/admin/role-pages/create")
	public ModelAndView createRolePage(@RequestParam("roleName") String roleName, @RequestParam(value = "endpointNames", required = false) List<String> endpointNames)
	{
		String rn = roleName == null ? "" : roleName.trim();
		if (rn.isEmpty())
		{
			return new ModelAndView("redirect:/admin/role-pages");
		}
		Role role = roleService.findByName(rn).orElse(null);
		if (role == null)
		{
			return new ModelAndView("redirect:/admin/role-pages");
		}
		if (endpointNames != null)
		{
			for (int i = 0; i < endpointNames.size(); i++)
			{
				String raw = endpointNames.get(i);
				String ep = raw == null ? "" : raw.trim();
				if (ep.isEmpty())
				{
					continue;
				}
				Endpoint endpointEntity = endpointRepo.findByName(ep).orElse(null);
				if (endpointEntity == null)
				{
					continue;
				}
				RoleEndpoint re = new RoleEndpoint();
				re.setRole(role);
				re.setEndpoint(endpointEntity);
				roleEndpointRepo.save(re);
			}
			permissionService.invalidateAll();
		}
		return new ModelAndView("redirect:/admin/role-pages");
	}

	@PostMapping("/admin/role-pages/delete")
	public ModelAndView deleteRolePage(@RequestParam("id") UUID id)
	{
		if (id != null)
		{
			roleEndpointRepo.deleteById(id);
			permissionService.invalidateAll();
		}
		return new ModelAndView("redirect:/admin/role-pages");
	}

	@GetMapping("/admin/role-pageobjects")
	public ModelAndView rolePageObjectsPage()
	{
		ModelAndView mv = new ModelAndView("admin/role_pageobjects");
		List<Role> roleList = roleService.findAll();
		List<RolePageObject> rolePageObjectList = rolePageObjectRepo.findAll();
		mv.addObject("roleList", roleList);
		mv.addObject("rolePageObjectList", rolePageObjectList);
		mv.addObject("pageObjects", pageObjectRepo.findAll());
		return mv;
	}

	@PostMapping("/admin/role-pageobjects/create")
	public ModelAndView createRolePageObject(@RequestParam("roleName") String roleName, @RequestParam(value = "pageObjectNames", required = false) List<String> pageObjectNames)
	{
		String rn = roleName == null ? "" : roleName.trim();
		if (rn.isEmpty())
		{
			return new ModelAndView("redirect:/admin/role-pageobjects");
		}
		Role role = roleService.findByName(rn).orElse(null);
		if (role == null)
		{
			return new ModelAndView("redirect:/admin/role-pageobjects");
		}
		if (pageObjectNames != null)
		{
			for (int i = 0; i < pageObjectNames.size(); i++)
			{
				String raw = pageObjectNames.get(i);
				String po = raw == null ? "" : raw.trim();
				if (po.isEmpty())
				{
					continue;
				}

				PageObject pageObj = pageObjectRepo.findByName(po).orElse(null);
				if (pageObj == null)
				{
					continue;
				}

				RolePageObject rpo = new RolePageObject();
				rpo.setRole(role);
				rpo.setPageObject(pageObj);
				rolePageObjectRepo.save(rpo);
			}
			permissionService.invalidateAll();
		}
		return new ModelAndView("redirect:/admin/role-pageobjects");
	}

	@PostMapping("/admin/role-pageobjects/delete")
	public ModelAndView deleteRolePageObject(@RequestParam("id") UUID id)
	{
		if (id != null)
		{
			rolePageObjectRepo.deleteById(id);
			permissionService.invalidateAll();
		}
		return new ModelAndView("redirect:/admin/role-pageobjects");
	}
}