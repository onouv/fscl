package fscl.function.service.adapters.web;

import java.util.List;
import java.util.stream.Collectors;

import fscl.core.api.EntityApiId;
import fscl.core.domain.EntityId;

public abstract class FunctionControllerBase {
	
	protected String buildFunctionMessage(
			String projectCode,
			String functionCode,
			String action, 
			Exception e) {
		return 
			"could not " + action + 
			" function { " + 
			projectCode + ": " + functionCode + "}: " 
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
