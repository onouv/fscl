package fscl.core.domain;

import fscl.core.api.EntityApiId;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Transient;

@Embeddable
public class EntityId {

	@Embedded
	public final EntityCode entity;

	@Embedded
	public final ProjectCode project;
	
	@Transient
	private boolean validated = false;

	public EntityId() {
		this.entity = null;
		this.project = null;
	}
	
	public EntityId(ProjectCode project, EntityCode entity) {
		this.project = project;
		this.entity = entity;
	}
		
	public EntityId(EntityApiId id) {
		this.project = new ProjectCode(id.project);
		this.entity = new EntityCode(id.entity);
	}
	
	public EntityId(EntityApiId id, CodeFormat format) {
		this.project = new ProjectCode(id.project);
		this.entity = new EntityCode(id.entity, format);
	}
	
	public EntityId(String project, String entity)//, CodeFormat format) 
		throws IllegalArgumentException {
		
		this.project = new ProjectCode(project);
		this.entity = new EntityCode(entity);//, format);
	}
	
	public EntityId fromProjectCode(ProjectCode projectCode) {
		return new EntityId(projectCode, this.entity);
	}
	
	public EntityId fromEntityCode(EntityCode entityCode) {
		return new EntityId(this.project, entityCode);
	}
	
	public EntityApiId toEntityApiId() {
		return new EntityApiId(
				this.project.toString(), 
				this.entity.toString());
	}
		
	@Override
	public String toString() {
		return "{ project=" + this.project + ", entity=" + this.entity + "}";
	}
	
	/**
	 * Return validation status.
	 * 
	 * @return true if compliant to config, otherwise false
	 */
	public boolean isValid() {
		return this.validated;
	}
	
	/**
	 * Validate against CodeConfig provided.
	 * 
	 * @param 	config CodeConfig
	 * @return 	a validated instance. May or may not be valid. 
	 * 			Make sure to check this new instance with 
	 * 			isValid() and to use that one henceforth.
	 */
	public EntityId validate(CodeFormat config) {
		this.validated =this.entity.isValid(config) &&
						 this.project.isValid();
		EntityId ret=  new EntityId(
				this.project, 
				new EntityCode(this.entity, config));
		ret.validated = this.validated;
		return ret;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o==null)
			return false;
		
		if(!(o instanceof EntityId))
			return false;
		
		if(o == this)
			return true;
		
		EntityId id = (EntityId) o;
		
		return (
			(this.entity == null ? 
					id.entity == null : 
					this.entity.equals(id.entity)) &&
			(this.project == null ? 
					id.project == null : 
					this.project.equals(id.project ))
		);
		
	}
}