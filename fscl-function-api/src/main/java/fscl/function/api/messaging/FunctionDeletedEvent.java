package fscl.function.api.messaging;

import fscl.core.api.EntityApiId;
import fscl.core.messaging.EntityEvent;

public class FunctionDeletedEvent extends EntityEvent {

	public FunctionDeletedEvent() {
		super();
	}
	
	public FunctionDeletedEvent(EntityApiId code) {
		super(code);
	}
	
	public EntityApiId getCode() {
		return this.code;
	}
	
	public String toString() {
		return "FunctionDeletedEvent=" + super.toString();
	}
}
