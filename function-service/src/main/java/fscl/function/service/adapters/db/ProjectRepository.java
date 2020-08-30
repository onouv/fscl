package fscl.function.service.adapters.db;

import org.springframework.data.repository.CrudRepository;

import fscl.project.foreignkeys.Project;

public interface ProjectRepository extends CrudRepository<Project, String> {

	public Project findByCode(String code); 
}
