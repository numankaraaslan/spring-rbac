package com.numankaraaslan.spring_rbac.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.numankaraaslan.spring_rbac.model.Material;
import com.numankaraaslan.spring_rbac.repo.MaterialRepository;

@Service
@Transactional
public class MaterialService
{
	private final MaterialRepository materialRepository;

	public MaterialService(MaterialRepository materialRepository)
	{
		this.materialRepository = materialRepository;
	}

	public List<Material> findAll()
	{
		return materialRepository.findAll();
	}

	public void save(Material material)
	{
		materialRepository.save(material);
	}

	public void deleteById(UUID id)
	{
		materialRepository.deleteById(id);
	}

	public Material findById(UUID id)
	{
		return materialRepository.findById(id).get();
	}
}
