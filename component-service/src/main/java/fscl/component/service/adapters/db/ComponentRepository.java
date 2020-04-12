package fscl.component.service.adapters.db;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import fscl.component.service.domain.Component;
import fscl.core.domain.EntityId;


public interface ComponentRepository extends MongoRepository<Component, EntityId> {

	@Query("{'_id.project.code': ?0}")
	public List<Component> findByProjectCode(String code);
	
	public Component findByCode(EntityId code);	
	
	@Query("{'_id.project.code': ?0, parent:{$exists: false}}")
	public List<Component> findRootsByProjectCode(String projectCode);
	
	@Query("{'functions.$id': ?0}")
	public List<Component> findByLinkedFunction(EntityId functionid);
}
