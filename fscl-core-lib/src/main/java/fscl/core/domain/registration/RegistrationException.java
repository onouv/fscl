package fscl.core.domain.registration;

@SuppressWarnings("serial")
public class RegistrationException extends Exception {

	protected IdRegistration registration;

	public RegistrationException(IdRegistration registration, String message) {
		super(registration.toString() + message);
		this.registration = registration;
	}

	public IdRegistration getRegistration() {
		return registration;
	}
	
	
}
