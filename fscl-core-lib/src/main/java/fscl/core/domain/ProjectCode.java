package fscl.core.domain;


public class ProjectCode {
	
	private String code;
	
	// NOTE : there must not be a default constructor (not even private)   
	// since mongodb driver cannot then deal with final members anymore. 
		
	public ProjectCode(String code) 
		throws IllegalArgumentException {
		
		if(code == null)
			throw new IllegalArgumentException("ProjectCode: cannot construct with null code");
		if(code.isEmpty())
			throw new IllegalArgumentException("ProjectCode: cannot construct with empty code");
		this.code = code;
	}
	
	@Override
	public String toString() {
		return this.code;
	}
	
	public String getCode() {
		return this.toString();
	}
	
	public boolean isEmpty() {
		return this.code.isEmpty();
	}
	
	// this should be elaborated later ...
	public boolean isValid() {
		return (!this.isEmpty());
	}
	
	@Override
	public boolean equals(Object o) {
		if(o==null)
			return false;
		
		if(!(o instanceof ProjectCode))
			return false;
		
		if(o == this)
			return true;
		
		ProjectCode c = (ProjectCode) o;
		
		return (
			(this.code == null ? c.code == null : this.code.equals(c.code))
		);
		
	}
		
}