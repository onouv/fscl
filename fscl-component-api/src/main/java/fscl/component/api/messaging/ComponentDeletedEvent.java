package fscl.component.api.messaging;

import fscl.core.api.EntityApiId;
import fscl.core.messaging.EntityEvent;

public class ComponentDeletedEvent extends EntityEvent {

	public ComponentDeletedEvent() {
		super();
	}
	
	public ComponentDeletedEvent(EntityApiId code) {
		super(code);
	}
	
	public EntityApiId getCode() {
		return this.code;
	}
	
	public String toString() {
		return "ComponentDeletedEvent=" + super.toString();
	}
}
