package fscl.core.api;


import org.springframework.data.annotation.PersistenceConstructor;

public class EntityApiId {
	
	public final String project;
	public final String entity;
	
	@PersistenceConstructor
	public EntityApiId(String project, String entity) {
		this.project = project;
		this.entity = entity;
	}
	
	public EntityApiId(EntityApiId id) {
		this.project = id.project;
		this.entity = id.entity;
	}
	
	public String toString() {
		return "{" + this.project + ":" + this.entity + "}";
	}

}
