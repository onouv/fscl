package fscl.core.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProjectCode {

	@Column(name = "project_code")
	private String code;

	public ProjectCode() {
		this.code = "";
	}

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