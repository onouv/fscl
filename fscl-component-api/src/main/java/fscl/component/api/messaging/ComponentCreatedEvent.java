package fscl.component.api.messaging;

import fscl.core.messaging.EntityEvent;
import fscl.core.api.EntityApiId;

public class ComponentCreatedEvent extends EntityEvent {
	
	public ComponentCreatedEvent() {
		super();
	}
	
	public ComponentCreatedEvent(EntityApiId code) {
		super(code);
	}
	
	public EntityApiId getCode() {
		return this.code;
	}
	
	public String toString() {
		return "ComponentCreatedEvent=" + super.toString();
	}
}
