package fscl.projectservice.domain;

import fscl.core.domain.CodeFormat;
import fscl.core.domain.EntityContent;
import fscl.core.domain.ProjectCode;
import fscl.messaging.events.ResultDomainEventsAggregate;
import fscl.project.api.events.ProjectEvent;

import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import java.util.List;
import java.util.ArrayList;

@Entity
//@Table(name="PROJECT")
public class Project extends EntityContent {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected Long dbId;
	
	private String code;
	
	public Project() {
		
	}
	
	public Project(String code, String name, String description) {
		super(name, description);
		this.code = code;
	}
	
	public Project(ProjectCode code, String name, String description) {
		this(code.toString(), name, description);
	}
	
	public ProjectCode getCode() {
		return new ProjectCode(this.code);
	}	
	
	public void setCode(ProjectCode code) {
		this.code = code.toString();
	}
	
	public static ResultDomainEventsAggregate<Project, ProjectEvent> 
	create(
			ProjectCode code, 
			String name, 
			String description,
			CodeFormat functionCfg, 
			CodeFormat systemCfg,
			CodeFormat locationCfg,
			CodeFormat componentCfg) {
		
		Project newProject = new Project(code, name, description);
		
		List<ProjectEvent> events = new ArrayList<ProjectEvent>();
		events.add(ProjectEvent.created(
			newProject.code.toString(),			 
			functionCfg,
			systemCfg,
			componentCfg,
			locationCfg));
		return new ResultDomainEventsAggregate<Project, ProjectEvent>(newProject, events);
	}
	
	public List<ProjectEvent> delete() {
		List<ProjectEvent> events = new ArrayList<ProjectEvent>();
		events.add(ProjectEvent.deleted(this.code.toString()));
		return events;
	}
}
