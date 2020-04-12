package fscl.component.service.domain;

import fscl.component.api.messaging.ComponentCreatedEvent;
import fscl.component.api.messaging.ComponentDeletedEvent;
import fscl.component.api.messaging.ComponentRecodedEvent;
import fscl.core.db.DuplicateEntityException;
import fscl.core.db.NoSuchCodedItemException;
import fscl.core.domain.CodeFormat;
import fscl.core.domain.Entity;
import fscl.core.domain.EntityId;
import fscl.function.api.foreignkeys.Function;
import fscl.core.domain.EntityContent;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.ArrayList;

@Document(collection="components")
public class Component extends Entity<Component>{
	
	@DBRef
	protected List<Function> functions; 
	
	public Component() {
		super();
		super.children = new ArrayList<Component>();
		super.parent = null;
		this.functions = new ArrayList<Function>();
	}
	
	public Component(EntityId id, Component parent, EntityContent content) {
		
		this();
		
		if(id ==null) {			
			throw new IllegalArgumentException("id must not be null");
		}
		this.code = id;
		
		this.parent = parent;
		if(parent != null)
			parent.addChild(this);
		
		if (content == null) {			
			throw new IllegalArgumentException(
					"parameter 'content' must not be null");
		}
		this.name = content.getName();
		this.description = content.getDescription();		
	}
	
	/**
	 * CREATE a Function instance and associated domain event
	 *
	 */
	public static Aggregate 
	create(	
		EntityId id, 
		Component parent, 
		EntityContent data, 
		CodeFormat config) {
		
		Component newComponent= new Component(id, parent, data);//, config); 
		ComponentCreatedEvent event = new ComponentCreatedEvent(
				newComponent.code.toEntityApiId());
						
		return new Aggregate( newComponent,	event);
	}
	
	/**
	 * Generate the domain event associated to DELETE a Component
	 *  
	 */	
	public ComponentDeletedEvent delete() {
		
		return new ComponentDeletedEvent(this.code.toEntityApiId());		
	}
	
	public ComponentRecodedEvent recode(EntityId oldId) 
		throws IllegalStateException {
		
		if(oldId.equals(this.code))
			throw new IllegalStateException("recode called with same id");
		
		ComponentRecodedEvent event = new ComponentRecodedEvent(
			this.code.toEntityApiId(),
			oldId.toEntityApiId());
			
		return event;		
	}
	
	public void linkFunction(Function function) throws DuplicateEntityException {		
		if(this.functions.contains(function)) {
			throw new DuplicateEntityException(function.getCode().toString());
		}
		this.functions.add(function);
	}
	
	public void unlinkFunction(Function function) throws NoSuchCodedItemException {		
		if(this.functions.remove(function)) {
			return;
		} else {
			throw new NoSuchCodedItemException(function.getCode().toString());
		}
	}
	
	public boolean unlinkFunction(EntityId function) {		
		return this.functions.removeIf(c -> c.getCode().equals(function));
	}
	
	public List<Function> getFunctions() {
		return this.functions;
	}

}
