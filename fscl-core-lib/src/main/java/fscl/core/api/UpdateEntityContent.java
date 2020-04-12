package fscl.core.api;

import fscl.core.domain.EntityContent;

public class UpdateEntityContent extends EntityContent {
	
	protected EntityApiId self;
	protected EntityApiId parent;
	
	
	public UpdateEntityContent() {
		super();
	}
	
	public UpdateEntityContent(
		EntityApiId parent, 
		EntityApiId self) {
		
		super();
		this.parent = parent;
		this.self = self;
	}

	public EntityApiId getSelf() {
		return self;
	}
	
	public EntityApiId getParent() {
		return parent;
	}


}
