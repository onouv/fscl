package fscl.component.service.adapters.db;

import org.springframework.data.mongodb.repository.MongoRepository;

import fscl.core.domain.EntityId;
import fscl.function.api.foreignkeys.Function;

public interface FunctionRepository 
	extends MongoRepository<Function, EntityId> {
	
	public Function findByCode(EntityId code);
}
