package fscl.function.api.messaging;

import fscl.core.api.EntityApiId;

public class FunctionRecodedEvent {
		
	protected EntityApiId newCode;
	protected EntityApiId oldCode;
	
	public FunctionRecodedEvent() {
		this.newCode = null;
		this.oldCode = null;	
	}		
		
	public FunctionRecodedEvent(EntityApiId newCode, EntityApiId oldCode) {
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
