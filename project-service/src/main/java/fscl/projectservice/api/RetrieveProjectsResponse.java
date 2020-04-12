package fscl.projectservice.api;

import java.util.List;

import fscl.projectservice.domain.Project;
import fscl.core.api.Response;

public class RetrieveProjectsResponse extends Response {

	public RetrieveProjectsResponse() {
		super();
		this.projects = new ProjectData[0];
	}
	
	public RetrieveProjectsResponse(String errMsg) {
		this();
		super.setError(errMsg);
	}
	
	public RetrieveProjectsResponse(List<Project> pList) {
		super();
		this.projects = new ProjectData[pList.size()];
		int i = 0;
		for(Project p : pList) {
			ProjectData responseData = new ProjectData(p);
			this.projects[i++]  = responseData;
		}
	}
	
	public ProjectData[] getProjects() {
		return this.projects;
	}
	
	private ProjectData[] projects;

	public int numProjects() {
		return this.projects.length;
	}
}
