package fscl.project.foreignkeys;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import fscl.core.domain.CodeFormat;

@Document(collection="projects")
public class Project {	
	
	@Id
	private ObjectId id;	
	private String code;
	private CodeFormat functionConfig;
	private CodeFormat systemConfig;
	private CodeFormat componentConfig;
	private CodeFormat locationConfig;
	
	public Project(
			String code, 
			CodeFormat functionConfig, 
			CodeFormat systemConfig,
			CodeFormat componentConfig, 
			CodeFormat locationConfig) {
		
		super();		
		this.code = code;
		this.functionConfig = functionConfig;
		this.systemConfig = systemConfig;
		this.componentConfig = componentConfig;
		this.locationConfig = locationConfig;
	}
	
	public Project() {
		this.code = "";		
	}
	
	public Project(String projectCode) {
		this.code = projectCode;
	}

	public String getCode() {
		return this.code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public CodeFormat getFunctionConfig() {
		return functionConfig;
	}

	public void setFunctionConfig(CodeFormat functionConfig) {
		this.functionConfig = functionConfig;
	}


	public CodeFormat getSystemConfig() {
		return systemConfig;
	}

	public void setSystemConfig(CodeFormat systemConfig) {
		this.systemConfig = systemConfig;
	}

	public CodeFormat getComponentConfig() {
		return componentConfig;
	}

	public void setComponentConfig(CodeFormat componentConfig) {
		this.componentConfig = componentConfig;
	}

	public CodeFormat getLocationConfig() {
		return locationConfig;
	}

	public void setLocationConfig(CodeFormat locationConfig) {
		this.locationConfig = locationConfig;
	}

}
