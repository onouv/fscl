package fscl.function.service.adapters.db;

import fscl.core.domain.EntityId;
import fscl.function.service.domain.Function;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FunctionRepository extends MongoRepository<Function, EntityId> {
	
	@Query("{'_id.project.code': ?0}")
	public List<Function> findByProjectCode(String code);
	
	public Function findByCode(EntityId code);	
	
	@Query("{'_id.project.code': ?0, parent:{$exists: false}}")
	public List<Function> findRootsByProjectCode(String projectCode);
	
	/**
	 * Find all Functions which hold a link to componentId
	 */
	@Query("{'components.$id': ?0}")
	public List<Function> findByLinkedComponent(EntityId componentId);
	
	
}
