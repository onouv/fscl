package fscl.project.foreignkeys;

import fscl.core.domain.CodeFormat;

import javax.persistence.*;

@Entity
@Table(name = "project_fk")
public class Project {	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "db_id")
	private Long id;
	//private ObjectId id;

	@Column(name = "code")
	private String code;

	@AttributeOverrides({
		@AttributeOverride(name="prefix", column=@Column(name="func_prefix")),
		@AttributeOverride(name="separator", column=@Column(name="func_separator")),
	})
	@Embedded
	private CodeFormat functionConfig;

	@AttributeOverrides({
		@AttributeOverride(name="prefix", column=@Column(name="sys_prefix")),
		@AttributeOverride(name="separator", column=@Column(name="sys_separator")),
	})
	@Embedded
	private CodeFormat systemConfig;

	@AttributeOverrides({
		@AttributeOverride(name="prefix", column=@Column(name="comp_prefix")),
		@AttributeOverride(name="separator", column=@Column(name="comp_separator")),
	})
	@Embedded
	private CodeFormat componentConfig;

	@AttributeOverrides({
		@AttributeOverride(name="prefix", column=@Column(name="loc_prefix")),
		@AttributeOverride(name="separator", column=@Column(name="loc_separator")),
	})
	@Embedded
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
