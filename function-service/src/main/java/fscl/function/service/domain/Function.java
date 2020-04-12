package fscl.function.service.domain;


import fscl.function.api.messaging.FunctionCreatedEvent;
import fscl.function.api.messaging.FunctionDeletedEvent;
import fscl.function.api.messaging.FunctionRecodedEvent;
import fscl.core.domain.CodeFormat;
import fscl.core.domain.EntityId;
import fscl.core.domain.EntityContent;
import fscl.core.domain.Entity;
import fscl.core.db.DuplicateEntityException;
import fscl.core.db.NoSuchCodedItemException;
import fscl.component.api.foreignkeys.Component;

import java.lang.IllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.core.mapping.DBRef;


import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="functions")
public class Function extends Entity<Function> {
		
	@DBRef
	protected List<Component> components;
	
	public Function() {
		super();
		this.components = new ArrayList<Component>();
		super.children = new ArrayList<Function>();
		super.parent = null;
	}
	
	public Function(Entity<Function> entity, CodeFormat cfg) {
		super(entity, cfg);
	}
	
	public Function(EntityId id, Function parent, EntityContent content)//, CodeConfig cfg) 
		throws IllegalArgumentException {
		
		//this(cfg);
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
	 * CREATE a Function instance and return associated domain event
	 *   
	 */
	public static Aggregate create(	
		EntityId id, 
		Function parent, 
		EntityContent data, 
		CodeFormat config) {
			
		Function newFunction = new Function(id, parent, data);
		FunctionCreatedEvent event = new FunctionCreatedEvent (
				newFunction.code.toEntityApiId());
		
		return new Aggregate(newFunction, event);
		 
	}
	
	/**
	 * Generate the domain event associated to DELETE of a Function
	 *  
	 */	
	public FunctionDeletedEvent delete() {		
		
		return new FunctionDeletedEvent(this.code.toEntityApiId());		
	}
	
	public FunctionRecodedEvent recode(EntityId oldId) 
		throws IllegalStateException {
		
		if(oldId.equals(this.code))
			throw new IllegalStateException("recode called with same id");
		FunctionRecodedEvent event = new FunctionRecodedEvent(
				this.code.toEntityApiId(), 
				oldId.toEntityApiId());
		
		return event;
		
	}
	
	public void linkComponent(Component component) throws DuplicateEntityException {		
		if(this.components.contains(component)) {
			throw new DuplicateEntityException(component.getCode().toString());
		}
		this.components.add(component);
	}
	
	public void unlinkComponent(Component component) throws NoSuchCodedItemException {		
		if(this.components.remove(component)) {
			return;
		} else {
			throw new NoSuchCodedItemException(component.getCode().toString());
		}
	}
	
	public boolean unlinkComponent(EntityId component) {		
		
		return this.components.removeIf(c -> c.getCode().equals(component));
	}
	
	public List<Component> getComponents() {
		return this.components;
	}
}
