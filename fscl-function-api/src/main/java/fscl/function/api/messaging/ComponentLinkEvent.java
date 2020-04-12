package fscl.function.api.messaging;

import fscl.core.api.EntityApiId;
import fscl.core.domain.EntityId;

public class ComponentLinkEvent  {
	
	public enum Type {
		LINKED,
		UNLINKED		
	}

	protected final Type type;
	protected final EntityApiId function;
	protected final EntityApiId component;
	
	ComponentLinkEvent() {		
		this.type = null;
		this.component = null;
		this.function = null;
	}
	
	ComponentLinkEvent(
			Type type, 
			EntityApiId component, 
			EntityApiId function) {
		this.component = component;
		this.function = function;
		this.type = type;
	}
	
	public static ComponentLinkEvent linked(EntityId function, EntityId component) {
		return new ComponentLinkEvent(
			Type.LINKED, 
			component.toEntityApiId(), 
			function.toEntityApiId());
	}
	
	public static ComponentLinkEvent unlinked(EntityId function, EntityId component) {
		return new ComponentLinkEvent(
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
		String msg = "{ " + this.component.toString() + 
			action + 
			this.function.toString() + 
			" }";
		return "ComponentLinkedEvent=" + msg;	
	}
	
}
