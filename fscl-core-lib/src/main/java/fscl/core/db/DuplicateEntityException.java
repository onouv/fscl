package fscl.core.db;

@SuppressWarnings("serial")
// https://stackoverflow.com/questions/16885318/unhandled-exception-type-in-java
public class DuplicateEntityException extends Exception {
	
	public DuplicateEntityException() {
		super();
	}
	
	public DuplicateEntityException(String code) {
		super("duplicate item with code " + code);		
	}

}
