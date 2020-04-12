package fscl.component.service.adapters.web;

import java.util.List;
import java.util.stream.Collectors;

import fscl.core.api.EntityApiId;
import fscl.core.domain.EntityId;

public abstract class ComponentControllerBase {

	protected String buildComponentMessage(
			String projectCode,
			String componentCode,
			String action, 
			Exception e) {
		return 
			"could not " + action + 
			" component { " + 
			projectCode + ": " + componentCode + "}: " 
			+ e.getMessage();
	}
	
	protected List<EntityId> translateCodes(List<EntityApiId> codes) {
		return codes.stream()
			.map(id -> {
				EntityId eid = new EntityId(id);
				return eid;
			})
			.collect(Collectors.toList());
	}

}
