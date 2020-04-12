package fscl.projectservice.api;


import java.util.UUID;

import fscl.core.domain.CodeFormat;

public class CreateProjectRequest extends ProjectData {
	
	private UUID   clientId;
	
	public final CodeFormat functionFormat;
	public final CodeFormat systemFormat;
	public final CodeFormat locationFormat;
	public final CodeFormat componentFormat;
	
	public CreateProjectRequest(
			String code, 
			String name, 
			String description,
			UUID clientId,
			CodeFormat functionFormat, 
			CodeFormat systemFormat,
			CodeFormat locationFormat,
			CodeFormat componentFormat) {
		
		super(code, name, description);
		this.clientId = clientId;
		this.functionFormat = functionFormat;
		this.systemFormat = systemFormat;
		this.locationFormat = locationFormat;
		this.componentFormat = componentFormat;
		
	}

		
	public ProjectData getData() {
		return new ProjectData(this.code, this.name, this.description);
	}
	
	public void setClientId(UUID id) {
		this.clientId = id;
	}
	
	public UUID getClientId() {
		return this.clientId;
	} 
	
}
