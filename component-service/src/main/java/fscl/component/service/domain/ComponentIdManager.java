package fscl.component.service.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fscl.core.db.NoSuchCodedItemException;
import fscl.core.domain.CodeFormat;
import fscl.core.domain.EntityId;
import fscl.core.domain.ProjectCode;
import fscl.core.domain.registration.IdRegistrationManager;
import fscl.component.service.adapters.db.ComponentRepository;
import fscl.component.service.adapters.db.ProjectRepository;
import fscl.project.foreignkeys.Project;

@Service
public class ComponentIdManager extends IdRegistrationManager {
	
	@Autowired 
	private ProjectRepository projectRepo;
	
	@Autowired
	private ComponentRepository componentRepo;
	
		
	@Override
	protected CodeFormat loadCodeConfigFor(ProjectCode projectCode) 
		throws NoSuchCodedItemException {
		Project project = this.projectRepo.findByCode(projectCode.toString());
		if(project != null) {			
			
			CodeFormat config = project.getComponentConfig();
			return config;
			
		} else {
			throw new NoSuchCodedItemException(projectCode.toString());
		}
	}
	
	@Override
	protected List<EntityId> listCommittedRootsFor(
			ProjectCode projectCode,
			CodeFormat format) {
		List<Component> functions= this.componentRepo.findRootsByProjectCode(
				projectCode.toString());
		List<EntityId> committed = new ArrayList<EntityId>();
		functions.forEach(function -> {
			EntityId id = function.getCode();
			id.entity.init(format);
			committed.add(id);
		});		
		return committed;
	}
	
	@Override
	protected List<EntityId> listCommittedChildsFor(
			EntityId parentCode, 
			CodeFormat format) 
			throws NoSuchCodedItemException {
		
		Component parent = this.componentRepo.findByCode(parentCode);
		if(parent != null ) {
			List<Component> childs = parent.getChildren();
			List<EntityId> childCodes = new ArrayList<EntityId>();
			childs.forEach((child) -> {
				EntityId id = child.getCode();
				id.entity.init(format);
				childCodes.add(id);
			});
			
			return childCodes;
			
		} else
			throw new NoSuchCodedItemException(parentCode);
	}
}
