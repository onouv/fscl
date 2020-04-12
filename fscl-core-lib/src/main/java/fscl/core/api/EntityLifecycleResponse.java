package fscl.core.api;

import fscl.core.domain.EntityId;

public class EntityLifecycleResponse extends Response {
	
	private String project;
	private String entity;
	
	public EntityLifecycleResponse( EntityId id) {
		super();
		this.entity = id.entity.toString();
		this.project = id.project.toString();		
	}
	
	public EntityLifecycleResponse( EntityId id, String error) {
		super(error);
		this.entity = id.entity.toString();
		this.project = id.project.toString();		
	}
	
	public EntityLifecycleResponse( String project, String entity, String error) {
		super(error);
		this.entity = entity;
		this.project = project;		
	}
		
		
	/*
	 *	Note : getter and setter methods are required for JSON mapping 
	 *  by the Jackson package  
	 *
	 */
	
	public String getProject() {
		return this.project;
	}
	
	public void setProject(String projectCode) {
		this.project = projectCode;
	}
	
	public String getEntity() {
		return this.entity;
	}
	
	public void setEntity(String code) {
		this.entity = code;
	}
}
