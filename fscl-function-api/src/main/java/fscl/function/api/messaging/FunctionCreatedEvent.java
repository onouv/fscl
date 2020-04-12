package fscl.function.api.messaging;

import fscl.core.messaging.EntityEvent;
import fscl.core.api.EntityApiId;

public class FunctionCreatedEvent extends EntityEvent {
	
	public FunctionCreatedEvent() {
		super();
	}
	
	public FunctionCreatedEvent(EntityApiId code) {
		super(code);
	}
	
	public EntityApiId getCode() {
		return this.code;
	}
	
	public String toString() {
		return "FunctionCreatedEvent=" + super.toString();
	}
}
