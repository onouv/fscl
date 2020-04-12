package fscl.component.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fscl.core.db.NoSuchCodedItemException;
import fscl.core.domain.CodeFormat;
import fscl.core.domain.EntityId;
import fscl.component.service.adapters.db.ProjectRepository;
import fscl.project.foreignkeys.Project;

@Service
public class ComponentServiceHelper {

	@Autowired 
	private ProjectRepository projectRepo;

	public EntityId validateFunctionId(EntityId id) 
		throws IllegalArgumentException, NoSuchCodedItemException {
			
		Project project = projectRepo.findByCode(id.project.toString());
		if(project != null) {
						 
			CodeFormat config = project.getFunctionConfig();
			EntityId validated = id.validate(config);
			if(id.isValid()) {
				return validated;
			} else {
				throw new IllegalArgumentException(
					String.format(
						"invalid EntityId: %s", 
						id.toString()));
			}
		} else 
			throw new NoSuchCodedItemException(id.project.toString());
	}
	
	public EntityId validateComponentId(EntityId id) 
			throws IllegalArgumentException, NoSuchCodedItemException {
				
			Project project = projectRepo.findByCode(id.project.toString());
			if(project != null) {
							 
				CodeFormat config = project.getComponentConfig();
				EntityId validated = id.validate(config);
				if(id.isValid()) {
					return validated;
				} else {
					throw new IllegalArgumentException(
						String.format(
							"invalid EntityId: %s", 
							id.toString()));
				}
			} else 
				throw new NoSuchCodedItemException(id.project.toString());
		}
}
