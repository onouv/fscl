package fscl.core.api;

public class Response {

	public Response()  {
		this.error = null;	// assuming no error by default 
	}
	
	public Response(String errMsg) {
		this.error = errMsg;
	}
	
	public String getError() {
		return this.error;
	}
	
	public void setError(String errMsg) {
		this.error = errMsg;
	}
	
	private String error;
	
}
