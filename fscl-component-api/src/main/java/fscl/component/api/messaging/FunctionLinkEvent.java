package fscl.component.api.messaging;

import fscl.core.api.EntityApiId;
import fscl.core.domain.EntityId;

public class FunctionLinkEvent {
	
	public enum Type {
		LINKED,
		UNLINKED		
	}

	protected final Type type;
	protected final EntityApiId function;
	protected final EntityApiId component;
	
	FunctionLinkEvent() {		
		this.type = null;
		this.component = null;
		this.function = null;
	}
	
	FunctionLinkEvent(
			Type type, 
			EntityApiId component, 
			EntityApiId function) {
		
		this.type = type;
		this.component = component;
		this.function = function;
	}
	
	public static FunctionLinkEvent linked(EntityId component, EntityId function) {
		return new FunctionLinkEvent(
			Type.LINKED, 
			component.toEntityApiId(), 
			function.toEntityApiId());
	}
	
	public static FunctionLinkEvent unlinked(EntityId component, EntityId function) {
		return new FunctionLinkEvent(
			Type.UNLINKED, 
			component.toEntityApiId(), 
			function.toEntityApiId());
	}
	
	public EntityApiId getComponent() {
		return this.component;
	}
	
	public EntityApiId getFunction() {
		return this.function;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public String toString() {
		String action;
		switch(this.type) {
			case LINKED:
				action = " LINKED TO ";
				break;
			case UNLINKED:
				action = " UNLINKED FROM ";
				break;
			default: 
				action = " UNDEFINED ";
		}
		String msg = "{ " + this.function.toString() + 
			action + 
			this.component.toString() + 
			" }";
		return "FunctionLinkedEvent=" + msg;
	
	}
	
}
