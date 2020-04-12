package fscl.component.api.messaging;

import fscl.core.domain.EntityId;
import fscl.core.api.EntityApiId;
import fscl.messaging.events.DomainEvent;

public final class ComponentEvent implements DomainEvent {
	
	public enum Type {
		CREATED,
		DELETED,
		RECODED
	}
	
	private final Type type;
	private final EntityApiId code;	
	private final EntityApiId oldCode;
	
	public ComponentEvent() {
		this.type = null;
		this.code = null;
		this.oldCode = null;
	}
	
	public ComponentEvent(Type t, EntityId oldCode, EntityId newCode) {
		this.type = t;
		this.oldCode = oldCode.toEntityApiId();
		this.code = newCode.toEntityApiId();	
		
	}
	
	public ComponentEvent(Type t, EntityId code) {
		this.type = t;
		this.code = code.toEntityApiId();
		this.oldCode = null;
		//this.projectCode = projectCode;
		//this.entityCode = entityCode;
	}
	
	public static ComponentEvent created(EntityId code) {
		return new ComponentEvent(Type.CREATED, code);
	}
	
	public static ComponentEvent deleted(
			String projectCode, 
			String entityCode) {
		return new ComponentEvent(Type.DELETED, new EntityId(projectCode, entityCode));
	}
	
	public static ComponentEvent recoded(
			EntityId oldCode,
			EntityId newCode) {
		
		return new ComponentEvent(Type.RECODED, oldCode, newCode);
	}
	
	public Type getType() {
		return this.type;
	}

	public EntityApiId getCode() {
		return this.code;
	}
	
	public EntityApiId getOldCode() {
		return oldCode;
	}


	
}
