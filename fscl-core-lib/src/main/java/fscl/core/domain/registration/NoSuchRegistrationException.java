package fscl.core.domain.registration;

import fscl.core.domain.EntityId;

@SuppressWarnings("serial")
public class NoSuchRegistrationException extends Exception {

	private EntityId id;
	
	public NoSuchRegistrationException(EntityId id) {
		super("No IdRegistration exists for " + id.toString());
		this.id = id;
	}
	
	public EntityId getId() {
		return id;
	}


}
