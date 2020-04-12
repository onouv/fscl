package fscl.core.api;

import fscl.core.domain.Entity;

import java.util.List;
import java.util.ArrayList;

public class ReadEntityContent<T extends Entity<T>> extends UpdateEntityContent {
			
	protected List<EntityApiId> children;
	
	public ReadEntityContent() {
		super();
		this.children = new ArrayList<EntityApiId>();
	}
	
	public ReadEntityContent(T entity) {		
		this.name = entity.getName();
		this.description = entity.getDescription();		
				
		if(entity.hasParent()) {			
			this.parent = entity.getParentId();			
		} else {
			this.parent = null;
		}
		
		this.self = new EntityApiId(
				entity.getCode().project.toString(),
				entity.getCode().entity.toString());
		
		this.children = entity.getChildrenId();
	}
	
	public ReadEntityContent(
			EntityApiId parent, 
			EntityApiId self, 
			List<EntityApiId> children,
			String name, 
			String description) {
		
		super(parent, self);
		super.name = name;
		super.description = description;
		this.self = self;
		this.children = children;
		
	}
	
	public List<EntityApiId> getChildren() {
		return children;
	}

}
