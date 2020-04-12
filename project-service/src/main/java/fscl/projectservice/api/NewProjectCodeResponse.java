package fscl.projectservice.api;

import fscl.core.api.Response;

public class NewProjectCodeResponse extends Response {
	
	public NewProjectCodeResponse() {
		super();	
	}
	
	public NewProjectCodeResponse(String newCode) {
		super();
		this.code = newCode;
	}
	
	public void setCode(String code ) {		
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}
	
	private String code;
}
