package fscl.core.api;

import fscl.core.domain.EntityId;

import java.util.ArrayList;
import java.util.List;


public class EntityIdListResponse extends Response {

	public EntityIdListResponse(List<EntityApiId> entities) {
		
		this.entities = new ArrayList<EntityApiId>();
		for(EntityApiId id : entities) {
			EntityApiId responseId = new EntityApiId(id);
			this.entities.add(responseId);
		}
	}
	
	// standard workaround to the type Erasure problem with constructors 
	// taking List<> of different types
	public static EntityIdListResponse createFrom(List<EntityId> ids) {
		ArrayList<EntityApiId> apiIds = new ArrayList<EntityApiId>();
		ids.forEach(id -> {
			apiIds.add(id.toEntityApiId());
		});
		
		return new EntityIdListResponse(apiIds);
	}

	public EntityIdListResponse(String error) {
		super(error);
		this.entities = new ArrayList<EntityApiId>();
		
	}

	private ArrayList<EntityApiId> entities;

	public ArrayList<EntityApiId> getEntities() {
		return entities;
	}
}
