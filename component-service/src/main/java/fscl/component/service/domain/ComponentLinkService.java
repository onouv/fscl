package fscl.component.service.domain;

import fscl.function.api.foreignkeys.Function;
import fscl.component.service.domain.Component;
import fscl.component.api.messaging.FunctionLinkEvent;
import fscl.component.service.ComponentServiceHelper;
import fscl.component.service.adapters.db.ComponentRepository;
import fscl.component.service.adapters.db.FunctionRepository;
import fscl.component.service.adapters.messaging.DomainEventPublisher;
import fscl.core.db.DuplicateEntityException;
import fscl.core.db.NoSuchCodedItemException;
import fscl.core.domain.EntityId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComponentLinkService {
	
	@Autowired
	private ComponentRepository componentRepo;

	@Autowired
	private FunctionRepository functionRepo;
	
	@Autowired
	private ComponentServiceHelper helper;
	
	@Autowired
	private DomainEventPublisher publisher;
	
	public List<EntityId> readLinkedFunctionsOfComponent(EntityId componentId) 
			throws IllegalArgumentException, NoSuchCodedItemException {
			
			EntityId validatedComponentId = 
					this.helper.validateComponentId(componentId);
			
			Component component = this.componentRepo.findByCode(validatedComponentId);
			if(component == null) {
				throw new NoSuchCodedItemException(validatedComponentId);
			}
			
			List<Function> functions = component.getFunctions();
			ArrayList<EntityId> functionIds = new ArrayList<EntityId>();
			functions.forEach(func -> {
				functionIds.add(func.getCode());
			});
			
			return functionIds;
		}
	
	public EntityId linkFunctionToComponent(
			EntityId componentId, 
			EntityId functionId) 
		throws 
			NoSuchCodedItemException,
			DuplicateEntityException,
			IllegalArgumentException {
		
		EntityId validatedFunctionId = this.helper.validateFunctionId(functionId);
		EntityId validatedComponentId = this.helper.validateComponentId(componentId);
		
		Function foreignKey = this.functionRepo.findByCode(validatedFunctionId);
		if(foreignKey == null) {
			throw new NoSuchCodedItemException(validatedComponentId);
		}
		
		Component component = this.componentRepo.findByCode(validatedComponentId);
		if(component == null) {
			throw new NoSuchCodedItemException(validatedComponentId);
		}
		
		component.linkFunction(foreignKey);		// may throw DuplicateEntityException
		
		this.componentRepo.save(component);
		
		return validatedComponentId;
	}
	

	public EntityId linkFunctionToComponentAndPublish(
			EntityId componentId, 
			EntityId functionId) 
		throws 
			NoSuchCodedItemException,
			DuplicateEntityException,
			IllegalArgumentException {
		
		
		EntityId successComponent = this.linkFunctionToComponent(
				componentId, 
				functionId);
				
		FunctionLinkEvent event = FunctionLinkEvent.linked(
				componentId, 
				functionId);
		
		this.publisher.publish(event);
		
		return successComponent;
		
	}
	
	public EntityId unlinkFunctionFromComponent(
			EntityId componentId, 
			EntityId functionId) 
		throws 
			NoSuchCodedItemException,			
			IllegalArgumentException {
		
		EntityId validatedFunctionId = this.helper.validateFunctionId(functionId);
		EntityId validatedComponentId = this.helper.validateComponentId(componentId);
	
		Component component = this.componentRepo.findByCode(validatedComponentId);
		if(component == null) {
			throw new NoSuchCodedItemException(validatedComponentId);
		}
		
		Function deletee = new Function(validatedFunctionId);
		
		component.unlinkFunction(deletee.getCode());
		this.componentRepo.save(component);
		
		return validatedComponentId;		
	}
		
	public EntityId unlinkFunctionFromComponentAndPublish(
			EntityId functionId, 
			EntityId componentId) 
		throws 
			NoSuchCodedItemException,			
			IllegalArgumentException {
	
		EntityId component = this.unlinkFunctionFromComponent(componentId, functionId);
		FunctionLinkEvent event = FunctionLinkEvent.unlinked(componentId, functionId);
		this.publisher.publish(event);
		return component;
	}

	/**
	 * 
	 * @param id
	 * @return	the id of deleted Function, if any links were existing, otherwise null
	 */
	public EntityId deleteFunction(EntityId id) {
		List<Component> linkedComponents = this.componentRepo.findByLinkedFunction(id);
		if(linkedComponents.isEmpty()) {
			return null;
		} else {
			linkedComponents.forEach(component -> {
				component.unlinkFunction(id);
				this.componentRepo.save(component);			
			});
			
			return id;
		}
		
	}
	
	
}
