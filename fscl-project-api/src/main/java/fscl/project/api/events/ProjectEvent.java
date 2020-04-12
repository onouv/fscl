package fscl.project.api.events;

import fscl.core.domain.CodeFormat;
import fscl.messaging.events.DomainEvent;

public final class ProjectEvent implements DomainEvent {	
	
	public final CodeFormat functionConfig;
	public final CodeFormat systemConfig;
	public final CodeFormat locationConfig;
	public final CodeFormat componentConfig;
	
	private final Type type;	 
	private final String projectCode;
		
	public enum Type { CREATED,	DELETED	}
	
	public static ProjectEvent created(
		String projectCode, 			
		CodeFormat functionCfg,
		CodeFormat systemCfg,		
		CodeFormat componentCfg,
		CodeFormat locationCfg
		) {
		
		return new ProjectEvent(
				Type.CREATED, 
				projectCode, 
				functionCfg,
				systemCfg,
				componentCfg,
				locationCfg);
	}
	
	public static ProjectEvent deleted(String projectCode) {
		return new ProjectEvent(Type.DELETED, projectCode, null, null, null, null);
	}
	
	protected ProjectEvent(
		Type t, 
		String projectCode,
		CodeFormat functionCfg,
		CodeFormat systemCfg,		
		CodeFormat componentCfg,
		CodeFormat locationCfg) {
		this.type = t;
		this.projectCode = projectCode;
		this.functionConfig = functionCfg;
		this.systemConfig = systemCfg;
		this.componentConfig = componentCfg;
		this.locationConfig = locationCfg;
	}
	
	public String getProjectCode( ) {
		return this.projectCode;
	}	
	
	public Type getType() {
		return this.type;
	}
	
}
