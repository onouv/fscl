package fscl.function.service.adapters.db;

import fscl.core.domain.EntityId;
import fscl.function.service.domain.Function;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FunctionRepository extends CrudRepository<Function, EntityId> {
	
	@Query("select f from #{#entityName} f where f.code.project.code = ?1")
	public List<Function> findByProjectCode(String code);
	
	public Function findByCode(EntityId code);	
	
	@Query("select f from #{#entityName} f where f.code.project.code = ?1 and f.parent is null")
	public List<Function> findRootsByProjectCode(String projectCode);
	
	//
	// Find all Functions which hold a link to componentId
	//
	//@Query("{'components.$id': ?0}")
	//public List<Function> findByLinkedComponent(EntityId componentId);
	public List<Function> findByComponents(EntityId componentId);
	
}
