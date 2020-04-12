package fscl.function.service.domain;

import fscl.function.api.messaging.FunctionCreatedEvent;
import fscl.function.api.messaging.FunctionDeletedEvent;

public class Aggregate {
	public final Function instance;
	public FunctionCreatedEvent created;
	public FunctionDeletedEvent deleted;
	
	Aggregate(Function instance, FunctionCreatedEvent event) {
		this.instance = instance;
		this.created = event;
		this.deleted = null;
	}
	
	Aggregate(Function instance, FunctionDeletedEvent event) {
		this.instance = instance;
		this.created = null;
		this.deleted = event;			
	}
}