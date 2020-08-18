package fscl.projectservice.api;

import java.util.List;
import java.util.ArrayList;

import fscl.projectservice.domain.Project;
import fscl.core.api.Response;

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
		List<ProjectData> data  = new ArrayList<>();
		for(Project p : projects ) {
			data.add(new ProjectData(p));
		}
		this.projects = data.toArray(ProjectData[]::new);
	}

	public ProjectData[] getProjects() {
		return this.projects;
	}

}
