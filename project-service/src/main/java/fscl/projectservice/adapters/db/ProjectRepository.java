package fscl.projectservice.adapters.db;

import org.springframework.data.mongodb.repository.MongoRepository;

import fscl.projectservice.domain.Project;

public interface ProjectRepository extends MongoRepository<Project, String> {
	
	public Project findByCode(String code);
		
}
