package com.numankaraaslan.spring_rbac.repo;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.numankaraaslan.spring_rbac.model.RoleEndpoint;

public interface RoleEndpointRepository extends JpaRepository<RoleEndpoint, UUID>, JpaSpecificationExecutor<RoleEndpoint>
{

	List<RoleEndpoint> findAllByRole_NameIn(Set<String> roleNames);
}