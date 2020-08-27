package fscl.projectservice.api;

import fscl.core.api.Response;
import fscl.projectservice.domain.Project;

import java.util.List;

public class RetrieveProjectsResponse extends Response {

	private final ProjectData[] projects;

	public RetrieveProjectsResponse() {
		super();
		this.projects = new ProjectData[0];
	}
	
	public RetrieveProjectsResponse(String errMsg) {
		this();
		super.setError(errMsg);
	}
	
	public RetrieveProjectsResponse(List<Project> projects) {

		super();
		this.projects = new ProjectData[projects.size()];		
		int i = 0;
		for(Project p : projects ) {
			this.projects[i++] = new ProjectData(p);
		}
	}

	public ProjectData[] getProjects() {
		return this.projects;
	}

}
