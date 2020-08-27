package fscl.core.api;

import java.util.ArrayList;
import java.util.List;

public class EntityIdListRequest {
	
	public EntityIdListRequest() {
		this.entities = new ArrayList<EntityApiId>();
	}
	
	public List<EntityApiId> getEntities() {
		return entities;
	}

	public void setEntities(List<EntityApiId> entities) {
		this.entities.addAll(entities);
	}

	public EntityIdListRequest(List<EntityApiId> entities) {
		super();
		this.entities = new ArrayList<EntityApiId>();
		this.entities.addAll(entities);
	}

	private ArrayList<EntityApiId> entities;
	
}
