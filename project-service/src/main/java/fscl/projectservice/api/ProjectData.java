package fscl.projectservice.api;

import fscl.core.domain.EntityContent;
import fscl.core.domain.ProjectCode;
import fscl.projectservice.domain.Project;


public class ProjectData extends EntityContent {
	
	protected String code; 
	
	// Dummy constructor for Jackson JSON mapper
	public ProjectData() {
		this.code = "";
	}
	
	public ProjectData(Project p) {
		super(p.getName(), p.getDescription());
		this.code = p.getCode().toString();
	}
	
	public ProjectData(String code, String name, String description) {
		super(name, description);
		this.code = code;
	}
	
	public ProjectData(ProjectCode code, String name, String description) {
		super(name, description);
		this.code = code.toString();
	}
	
	
	
	// Standard java constructors and setters are not needed - 
	// Jackson would take care of it. We provide them for
	// ease of debugging
	
	public String getCode() {
		return this.code;
	}
	
	public ProjectCode asProjectCode() {
		return new ProjectCode(this.code);
	}
	
	public Project asProject() {
		return new Project(this.code, this.name, this.description);
	}	
	
	

}
