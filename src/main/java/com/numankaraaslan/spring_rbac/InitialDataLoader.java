package com.numankaraaslan.spring_rbac;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.numankaraaslan.spring_rbac.model.Endpoint;
import com.numankaraaslan.spring_rbac.model.PageObject;
import com.numankaraaslan.spring_rbac.model.Role;
import com.numankaraaslan.spring_rbac.model.RoleEndpoint;
import com.numankaraaslan.spring_rbac.model.User;
import com.numankaraaslan.spring_rbac.repo.EndpointRepository;
import com.numankaraaslan.spring_rbac.repo.PageObjectRepository;
import com.numankaraaslan.spring_rbac.repo.RoleEndpointRepository;
import com.numankaraaslan.spring_rbac.repo.RolePageObjectRepository;
import com.numankaraaslan.spring_rbac.service.RoleService;
import com.numankaraaslan.spring_rbac.service.UserService;

@Component
public class InitialDataLoader implements CommandLineRunner
{
	private final UserService userService;
	private final RoleService roleService;
	private final RoleEndpointRepository roleEndpointRepository;
	private final RolePageObjectRepository rolePageObjectRepository;
	private final EndpointRepository endpointRepository;
	private final PageObjectRepository pageObjectRepository;

	public InitialDataLoader(UserService userService, PageObjectRepository pageObjectRepository, EndpointRepository endpointRepository, RoleService roleService, RolePageObjectRepository rolePageObjectRepository, RoleEndpointRepository roleEndpointRepository)
	{
		this.userService = userService;
		this.roleService = roleService;
		this.roleEndpointRepository = roleEndpointRepository;
		this.rolePageObjectRepository = rolePageObjectRepository;
		this.endpointRepository = endpointRepository;
		this.pageObjectRepository = pageObjectRepository;
	}

	@Override
	public void run(String... args) throws Exception
	{
		roleEndpointRepository.deleteAllInBatch();
		rolePageObjectRepository.deleteAllInBatch();
		endpointRepository.deleteAllInBatch();
		pageObjectRepository.deleteAllInBatch();
		userService.deleteAllInBatch();
		roleService.deleteAllInBatch();
		ensurePageObject("MATERIAL_CREATE", "Material create permission");
		ensurePageObject("MATERIAL_DELETE", "Material delete permission");
		ensurePageObject("EMPLOYEE_CREATE", "Employee create permission");
		ensurePageObject("EMPLOYEE_DELETE", "Employee delete permission");
		ensureEndpoint("/materials", "Materials page (GET)");
		ensureEndpoint("/materials/create", "Create material (POST)");
		ensureEndpoint("/materials/delete", "Delete material (POST)");
		ensureEndpoint("/employees", "Employees page (GET)");
		ensureEndpoint("/employees/create", "Create employee (POST)");
		ensureEndpoint("/employees/delete", "Delete employee (POST)");
		Role adminRole = ensureRole("ADMIN");
		//		ensureRole("WORKER");
		//		ensureRole("EMPLOYER");
		ensureUser("admin", "1234", adminRole);
		ensureUser("user1", "1234");
		ensureUser("user2", "1234");
		ensureUser("user3", "1234");
		Endpoint epHome = ensureEndpoint("/", "Home");
		Endpoint epRoot = ensureEndpoint("/admin", "Admin home");
		Endpoint epRoles = ensureEndpoint("/admin/roles", "Roles page");
		Endpoint epRolesToUser = ensureEndpoint("/admin/assign-roles-to-user", "Assign roles to user (POST)");
		Endpoint epRolesCreate = ensureEndpoint("/admin/roles/create", "Create role (POST)");
		Endpoint epRolesDelete = ensureEndpoint("/admin/roles/delete", "Delete role (POST)");
		Endpoint epRolePages = ensureEndpoint("/admin/role-pages", "Role ↔ Endpoint page");
		Endpoint epRolePagesCreate = ensureEndpoint("/admin/role-pages/create", "Add role-endpoint mapping (POST)");
		Endpoint epRolePagesDelete = ensureEndpoint("/admin/role-pages/delete", "Delete role-endpoint mapping (POST)");
		Endpoint epRolePageObjects = ensureEndpoint("/admin/role-pageobjects", "Role ↔ PageObject page");
		Endpoint epRolePageObjectsCreate = ensureEndpoint("/admin/role-pageobjects/create", "Add role-pageobject mapping (POST)");
		Endpoint epRolePageObjectsDelete = ensureEndpoint("/admin/role-pageobjects/delete", "Delete role-pageobject mapping (POST)");
		grant(roleService.getRole("ADMIN"), epHome);
		grant(roleService.getRole("ADMIN"), epRoot);
		grant(roleService.getRole("ADMIN"), epRoles);
		grant(roleService.getRole("ADMIN"), epRolesToUser);
		grant(roleService.getRole("ADMIN"), epRolesCreate);
		grant(roleService.getRole("ADMIN"), epRolesDelete);
		grant(roleService.getRole("ADMIN"), epRolePages);
		grant(roleService.getRole("ADMIN"), epRolePagesCreate);
		grant(roleService.getRole("ADMIN"), epRolePagesDelete);
		grant(roleService.getRole("ADMIN"), epRolePageObjects);
		grant(roleService.getRole("ADMIN"), epRolePageObjectsCreate);
		grant(roleService.getRole("ADMIN"), epRolePageObjectsDelete);
	}

	private PageObject ensurePageObject(String name, String description)
	{
		PageObject existing = pageObjectRepository.findByName(name).orElse(null);
		if (existing != null)
		{
			return existing;
		}

		PageObject po = new PageObject();
		po.setName(name);
		po.setDescription(description);
		return pageObjectRepository.save(po);
	}

	private User ensureUser(String username, String password, Role... roles)
	{
		try
		{
			return (User) userService.loadUserByUsername(username);
		}
		catch (Exception ignored)
		{
		}

		User u = new User();
		u.setUsername(username);
		u.setPassword(password);

		if (roles != null)
		{
			for (int i = 0; i < roles.length; i++)
			{
				Role r = roles[i];
				if (r != null)
				{
					u.getRoles().add(r);
				}
			}
		}

		userService.save(u);
		return u;
	}

	private Role ensureRole(String roleName)
	{
		Role role = roleService.findByName(roleName).orElse(null);
		if (role != null)
		{
			return role;
		}

		Role r = new Role();
		r.setName(roleName);
		roleService.save(r);
		return r;
	}

	private Endpoint ensureEndpoint(String name, String description)
	{
		Endpoint ep = endpointRepository.findByName(name).orElse(null);
		if (ep == null)
		{
			ep = new Endpoint();
			ep.setName(name);
			ep.setDescription(description);
			ep = endpointRepository.save(ep);
		}
		return ep;
	}

	private void grant(Role role, Endpoint endpoint)
	{
		RoleEndpoint re = new RoleEndpoint();
		re.setRole(role);
		re.setEndpoint(endpoint);
		roleEndpointRepository.save(re);
	}
}