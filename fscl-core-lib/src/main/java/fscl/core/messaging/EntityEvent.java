package fscl.core.messaging;

import fscl.core.api.EntityApiId;

public abstract class EntityEvent {
	
	protected final EntityApiId code;
	
	public EntityEvent() {
		this.code = null;
	}
	
	public EntityEvent(EntityApiId code) {
		this.code = code;
	}
	
	public String toString() {
		return this.code.toString(); 
	}
	
}
