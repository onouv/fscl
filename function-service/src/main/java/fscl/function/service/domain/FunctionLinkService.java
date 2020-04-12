package fscl.function.service.domain;

import fscl.function.api.messaging.ComponentLinkEvent;
import fscl.function.service.adapters.db.ComponentRepository;
import fscl.function.service.adapters.db.FunctionRepository;
import fscl.function.service.adapters.messaging.DomainEventPublisher;
import fscl.core.domain.EntityId;
import fscl.core.domain.linking.EntityLinkService;
import fscl.core.db.NoSuchCodedItemException;
import fscl.core.db.DuplicateEntityException;
import fscl.component.api.foreignkeys.Component;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


@Service
public class FunctionLinkService extends EntityLinkService {

	@Autowired
	private FunctionRepository functionRepo;
	
	@Autowired
	private ComponentRepository componentRepository;
	
	@Autowired 
	private FunctionServiceHelper helper;
	
	@Autowired
	private DomainEventPublisher publisher;
	
	
	public List<EntityId> readLinkedComponentsOfFunction(EntityId functionId) 
		throws IllegalArgumentException, NoSuchCodedItemException {
		
		EntityId validatedFunctionId = this.helper.validateFunctionId(functionId);
		
		Function function = this.functionRepo.findByCode(validatedFunctionId);
		if(function == null) {
			throw new NoSuchCodedItemException(validatedFunctionId);
		}
		
		List<Component> components = function.getComponents();
		ArrayList<EntityId> componentIds = new ArrayList<EntityId>();
		components.forEach(component -> {
			componentIds.add(component.getCode());
		});
		
		return componentIds;
	}
	
		
	/** 
	 * Link the specified componentId to the specified functionId and persist in database.
	 * 
	 * @param functionId
	 * @param componentId the component foreign key
	 * @return 	the EntityId of the function which was successfully linked with the 
	 * 			componentId foreign key 
	 * @throws NoSuchCodedItemException	if any of functionId entity or componentId 
	 * 			foreign key is not found 
	 * @throws IllegalArgumentException if an invalid EntityId format is given in any of 
	 * 	the componentId or functionId
	 */
	public EntityId linkComponentToFunction(EntityId functionId, EntityId componentId) 
		throws 
			NoSuchCodedItemException,
			DuplicateEntityException,
			IllegalArgumentException {
		
		EntityId validatedFunctionId = this.helper.validateFunctionId(functionId);
		EntityId validatedComponentId = this.helper.validateComponentId(componentId);
		
		Component foreignKey = this.componentRepository.findByCode(validatedComponentId);
		if(foreignKey == null) {
			throw new NoSuchCodedItemException(validatedComponentId);
		}
		
		Function function = this.functionRepo.findByCode(validatedFunctionId);
		if(function == null) {
			throw new NoSuchCodedItemException(validatedFunctionId);
		}
		
		function.linkComponent(foreignKey);
		this.functionRepo.save(function);
		
		return validatedFunctionId;		
		
	}
	
	public EntityId linkComponentToFunctionAndPublish(
			EntityId functionId, 
			EntityId componentId) 
		throws 
			NoSuchCodedItemException,
			DuplicateEntityException,
			IllegalArgumentException {
		
		EntityId successFunction = this.linkComponentToFunction(functionId, componentId);		
		ComponentLinkEvent event = ComponentLinkEvent.linked(functionId, componentId);
		this.publisher.publish(event);
		
		return successFunction;
	}	
	
	public EntityId unlinkComponentFromFunction(EntityId functionId, EntityId componentId) 
		throws 
			NoSuchCodedItemException,			
			IllegalArgumentException {
		
		EntityId validatedFunctionId = this.helper.validateFunctionId(functionId);
		EntityId validatedComponentId = this.helper.validateComponentId(componentId);
	
		Function function = this.functionRepo.findByCode(validatedFunctionId);
		if(function == null) {
			throw new NoSuchCodedItemException(validatedFunctionId);
		}
		
		Component deletee = new Component(validatedComponentId);
		
		function.unlinkComponent(deletee);
		this.functionRepo.save(function);
		
		return validatedFunctionId;		
	}
	
	public EntityId unlinkComponentFromFunctionAndPublish(
			EntityId functionId, 
			EntityId componentId) 
		throws 
			NoSuchCodedItemException,			
			IllegalArgumentException {
	
		EntityId function = this.unlinkComponentFromFunction(functionId, componentId);
		ComponentLinkEvent event = ComponentLinkEvent.unlinked(functionId, componentId);
		this.publisher.publish(event);
		return function;
	}
	
	/**
	 * 
	 * @param id
	 * @return the id of deleted Component, if any links were existing, otherwise null
	 */
	public EntityId deleteComponent(EntityId id) {
		
		List<Function> linkedFunctions = this.functionRepo.findByLinkedComponent(id);
		
		if(linkedFunctions.isEmpty()) {		
			return null;
		} else {
			linkedFunctions.forEach(function -> {
				function.unlinkComponent(id);
				this.functionRepo.save(function);
			});
			
			return id;
		}
	}
}
