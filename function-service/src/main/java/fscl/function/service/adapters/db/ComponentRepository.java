package fscl.function.service.adapters.db;

import fscl.component.api.foreignkeys.Component;
import fscl.core.domain.EntityId;
import org.springframework.data.repository.CrudRepository;

public interface ComponentRepository extends CrudRepository<Component, EntityId> {
	
	public Component findByCode(EntityId code);	 
}
