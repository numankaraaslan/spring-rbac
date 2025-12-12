package com.numankaraaslan.spring_rbac.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.numankaraaslan.spring_rbac.model.RoleEndpoint;

public interface RoleEndpointRepository extends JpaRepository<RoleEndpoint, UUID>, JpaSpecificationExecutor<RoleEndpoint>
{
}