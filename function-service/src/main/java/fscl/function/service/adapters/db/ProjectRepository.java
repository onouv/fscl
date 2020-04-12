package fscl.function.service.adapters.db;

import org.springframework.data.mongodb.repository.MongoRepository;

import fscl.project.foreignkeys.Project;

public interface ProjectRepository extends MongoRepository<Project, String> {

	public Project findByCode(String code); 
}
