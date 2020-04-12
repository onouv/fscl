package fscl.core.domain.registration;

import java.util.UUID;

@SuppressWarnings("serial")
public class CollidingClientForRegistrationException extends RegistrationException {

	public CollidingClientForRegistrationException(IdRegistration registration, UUID collidingClient) {
		super(registration, ("collides with client " + collidingClient.toString()));		
	}
	
	

}
