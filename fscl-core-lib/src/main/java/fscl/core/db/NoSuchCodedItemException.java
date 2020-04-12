package fscl.core.db;

import fscl.core.domain.EntityId;

@SuppressWarnings("serial")
public class NoSuchCodedItemException extends Exception {
	
	private String code;
	
	public NoSuchCodedItemException() {
		super();
	}
	
	public NoSuchCodedItemException(EntityId code) {
		super(" no such item with code " + code);
		this.code = code.toString();
	}
	public NoSuchCodedItemException(String code) {
		super(" no such item with code " + code);
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
	
	public String toString() {
		return "{code=" + code + "}";
	}
}
