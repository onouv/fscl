package fscl.function.service.domain;

import fscl.core.domain.EntityId;
import fscl.core.domain.ProjectCode;
import fscl.core.domain.CodeFormat;
import fscl.core.domain.registration.IdRegistrationManager;
import fscl.function.service.adapters.db.FunctionRepository;
import fscl.function.service.adapters.db.ProjectRepository;
import fscl.project.foreignkeys.Project;
import fscl.core.db.NoSuchCodedItemException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class FunctionIdManager extends IdRegistrationManager {
	
	@Autowired 
	private ProjectRepository projectRepo;
	
	@Autowired
	private FunctionRepository functionRepo;
	
		
	@Override
	protected CodeFormat loadCodeConfigFor(ProjectCode projectCode) 
		throws NoSuchCodedItemException {
		Project project = this.projectRepo.findByCode(projectCode.toString());
		if(project != null) {			
			
			CodeFormat config = project.getFunctionConfig();
			return config;
			
		} else {
			throw new NoSuchCodedItemException(projectCode.toString());
		}
	}
	
	@Override
	protected List<EntityId> listCommittedRootsFor(
			ProjectCode projectCode,
			CodeFormat format) {
		List<Function> functions= this.functionRepo.findRootsByProjectCode(
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
		
		Function parent = this.functionRepo.findByCode(parentCode);
		if(parent != null ) {
			List<Function> childs = parent.getChildren();
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
