package com.numankaraaslan.spring_rbac;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.numankaraaslan.spring_rbac.model.Endpoint;
import com.numankaraaslan.spring_rbac.model.PageObject;
import com.numankaraaslan.spring_rbac.model.Role;
import com.numankaraaslan.spring_rbac.model.User;
import com.numankaraaslan.spring_rbac.repo.EndpointRepository;
import com.numankaraaslan.spring_rbac.repo.PageObjectRepository;
import com.numankaraaslan.spring_rbac.service.RoleService;
import com.numankaraaslan.spring_rbac.service.UserService;

@Component
public class InitialDataLoader implements CommandLineRunner
{
	private final UserService userService;
	private final RoleService roleService;
	private final EndpointRepository endpointRepository;
	private final PageObjectRepository pageObjectRepository;

	public InitialDataLoader(UserService userService, PageObjectRepository pageObjectRepository, EndpointRepository endpointRepository, RoleService roleService)
	{
		this.userService = userService;
		this.roleService = roleService;
		this.endpointRepository = endpointRepository;
		this.pageObjectRepository = pageObjectRepository;
	}

	@Override
	public void run(String... args) throws Exception
	{
		endpointRepository.deleteAllInBatch();
		pageObjectRepository.deleteAllInBatch();
		userService.deleteAllInBatch();
		roleService.deleteAllInBatch();
		PageObject pomCreate = createPageObject("MATERIAL_CREATE", "Material create permission");
		PageObject pomDelete = createPageObject("MATERIAL_DELETE", "Material delete permission");
		PageObject pomUpdate = createPageObject("MATERIAL_UPDATE", "Material update permission");
		PageObject poeCreate = createPageObject("EMPLOYEE_CREATE", "Employee create permission");
		PageObject poeDelete = createPageObject("EMPLOYEE_DELETE", "Employee delete permission");
		PageObject poeUpdate = createPageObject("EMPLOYEE_UPDATE", "Employee update permission");
		Endpoint epMat = crateEndpoint("/materials", "Materials page");
		Endpoint epMatCreate = crateEndpoint("/materials/create", "Create material");
		Endpoint epMatDelete = crateEndpoint("/materials/delete", "Delete material");
		Endpoint epMatUpdate = crateEndpoint("/materials/update/**", "Delete material");
		Endpoint epEmp = crateEndpoint("/employees", "Employees page");
		Endpoint epEmpCreate = crateEndpoint("/employees/create", "Create employee");
		Endpoint epEmpDelete = crateEndpoint("/employees/delete", "Delete employee");
		Endpoint epEmpUpdate = crateEndpoint("/employees/update/**", "Update employee");
		Endpoint epRoot = crateEndpoint("/admin", "Admin home");
		Endpoint epRoles = crateEndpoint("/admin/roles", "Roles page");
		Endpoint epRolesToUser = crateEndpoint("/admin/assign-roles-to-user", "Assign roles to user (POST)");
		Endpoint epRolesCreate = crateEndpoint("/admin/roles/create", "Create role (POST)");
		Endpoint epRolesDelete = crateEndpoint("/admin/roles/delete", "Delete role (POST)");
		Endpoint epRolePages = crateEndpoint("/admin/role-pages", "Role ↔ Endpoint page");
		Endpoint epRolePagesCreate = crateEndpoint("/admin/role-pages/create", "Add role-endpoint mapping (POST)");
		Endpoint epRolePagesDelete = crateEndpoint("/admin/role-pages/delete", "Delete role-endpoint mapping (POST)");
		Endpoint epRolePageObjects = crateEndpoint("/admin/role-pageobjects", "Role ↔ PageObject page");
		Endpoint epRolePageObjectsCreate = crateEndpoint("/admin/role-pageobjects/create", "Add role-pageobject mapping (POST)");
		Endpoint epRolePageObjectsDelete = crateEndpoint("/admin/role-pageobjects/delete", "Delete role-pageobject mapping (POST)");
		// ---------------------
		Role adminRole = createRole("ADMIN");
		//		Role workerRole = createRole("WORKER");
		//		Role employerRole = createRole("EMPLOYER");
		createUser("admin", "1234", adminRole);
		createUser("user1", "1234");
		createUser("user2", "1234");
		createUser("user3", "1234");
		//-----------------------
		adminRole.getEndpoints().add(epRoot);
		adminRole.getEndpoints().add(epRoles);
		adminRole.getEndpoints().add(epRolesToUser);
		adminRole.getEndpoints().add(epRolesCreate);
		adminRole.getEndpoints().add(epRolesDelete);
		adminRole.getEndpoints().add(epRolePages);
		adminRole.getEndpoints().add(epRolePagesCreate);
		adminRole.getEndpoints().add(epRolePagesDelete);
		adminRole.getEndpoints().add(epRolePageObjects);
		adminRole.getEndpoints().add(epRolePageObjectsCreate);
		adminRole.getEndpoints().add(epRolePageObjectsDelete);
		roleService.save(adminRole);
		// -------------------------
		//		grant(workerRole, epMat);
		//		grant(workerRole, epMatCreate);
		//		grant(workerRole, epMatDelete);
		//		grant(workerRole, epMatUpdate);
		//		grant(workerRole, pomCreate);
		//		grant(workerRole, pomDelete);
		//		grant(workerRole, pomUpdate);
		//		grant(employerRole, epEmp);
		//		grant(employerRole, epEmpCreate);
		//		grant(employerRole, epEmpDelete);
		//		grant(employerRole, epEmpUpdate);
		//		grant(employerRole, poeCreate);
		//		grant(employerRole, poeDelete);
		//		grant(employerRole, poeUpdate);
	}

	private PageObject createPageObject(String name, String description)
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

	private User createUser(String username, String password, Role... roles)
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

	private Role createRole(String roleName)
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

	private Endpoint crateEndpoint(String name, String description)
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
}