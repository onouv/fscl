package fscl.component.service.domain;

import fscl.component.api.messaging.ComponentCreatedEvent;
import fscl.component.api.messaging.ComponentDeletedEvent;


public class Aggregate {

	public final Component instance;
	public ComponentCreatedEvent created;
	public ComponentDeletedEvent deleted;
	
	Aggregate(Component instance, ComponentCreatedEvent event) {
		this.instance = instance;
		this.created = event;
		this.deleted = null;
	}
	
	Aggregate(Component instance, ComponentDeletedEvent event) {
		this.instance = instance;
		this.created = null;
		this.deleted = event;			
	}
}
