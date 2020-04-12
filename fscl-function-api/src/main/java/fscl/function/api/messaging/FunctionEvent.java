package fscl.function.api.messaging;

import fscl.core.domain.EntityId;
import fscl.core.api.EntityApiId;
import fscl.messaging.events.DomainEvent;

public final class FunctionEvent implements DomainEvent {
	
	public enum Type {
		CREATED,
		DELETED,
		RECODED
	}
	
	private final Type type;
	//private final String projectCode;
	//private final String entityCode;
	private final EntityApiId oldCode;
	public EntityApiId getOldCode() {
		return oldCode;
	}

	private final EntityApiId code;
	
	public FunctionEvent() {
		this.type = null;
		this.code = null;
		this.oldCode = null;
	}
	
	public FunctionEvent(Type t, EntityId oldCode, EntityId newCode) {
		this.type = t;
		this.oldCode = oldCode.toEntityApiId();
		this.code = newCode.toEntityApiId();	
		
	}
	
	public FunctionEvent(Type t, EntityId code) {
		this.type = t;
		this.code = code.toEntityApiId();
		this.oldCode = null;
		//this.projectCode = projectCode;
		//this.entityCode = entityCode;
	}
	
	public static FunctionEvent created(EntityId code) {
		return new FunctionEvent(Type.CREATED, code);
	}
	
	public static FunctionEvent deleted(
			String projectCode, 
			String entityCode) {
		return new FunctionEvent(Type.DELETED, new EntityId(projectCode, entityCode));
	}
	
	public static FunctionEvent recoded(
			EntityId oldCode,
			EntityId newCode) {
		
		return new FunctionEvent(Type.RECODED, oldCode, newCode);
	}
		
	public EntityApiId getCode() {
		return this.code;
	}
		
	public Type getType() {
		return this.type;
	}
	
}
