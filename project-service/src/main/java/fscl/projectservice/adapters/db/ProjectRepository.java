package fscl.projectservice.adapters.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fscl.projectservice.domain.Project;

@Repository
public interface ProjectRepository extends CrudRepository<Project, String> {
	
	public Project findByCode(String code);
		
}
