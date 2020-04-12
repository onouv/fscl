package fscl.function.service.adapters.db;

import org.springframework.data.mongodb.repository.MongoRepository;

import fscl.core.domain.EntityId;
import fscl.component.api.foreignkeys.Component;

public interface ComponentRepository extends MongoRepository<Component, EntityId> {
	
	public Component findByCode(EntityId code);	 
}
