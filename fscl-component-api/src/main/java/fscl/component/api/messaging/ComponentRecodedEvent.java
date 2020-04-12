package fscl.component.api.messaging;

import fscl.core.api.EntityApiId;


public class ComponentRecodedEvent {
		
	protected EntityApiId newCode;
	protected EntityApiId oldCode;
	
	public ComponentRecodedEvent() {
		this.newCode = null;
		this.oldCode = null;	
	}
	
	public ComponentRecodedEvent(EntityApiId newCode, EntityApiId oldCode) {
		this.newCode = newCode;
		this.oldCode = oldCode;	
	}
	
	public EntityApiId getNewCode() {
		return this.newCode;
	}

	public EntityApiId getOldCode() {
		return this.oldCode;
	}
	
	public String toString() {
		return "ComponentRecodedEvent= {" +
			"old: " + oldCode.toString() +
			"new: " + newCode.toString() +
			"}";
	}
	
}
