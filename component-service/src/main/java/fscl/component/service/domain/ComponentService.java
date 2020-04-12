package fscl.component.service.domain;

import fscl.project.foreignkeys.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fscl.component.api.messaging.ComponentDeletedEvent;
import fscl.component.api.messaging.ComponentRecodedEvent;
import fscl.component.service.adapters.db.ComponentRepository;
import fscl.component.service.adapters.db.ProjectRepository;
import fscl.component.service.adapters.messaging.DomainEventPublisher;
import fscl.core.api.CreateEntityContent;
import fscl.core.api.EntityApiId;
import fscl.core.api.UpdateEntityContent;
import fscl.core.db.DuplicateEntityException;
import fscl.core.db.NoSuchCodedItemException;
import fscl.core.domain.CodeFormat;
import fscl.core.domain.EntityId;
import fscl.core.domain.EntityService;
import fscl.core.domain.ProjectCode;
import fscl.core.domain.registration.CollidingClientForRegistrationException;
import fscl.core.domain.registration.NoSuchRegistrationException;



@Service
public class ComponentService extends EntityService<Component> {
	
	@Autowired
	private ComponentRepository componentRepo;	
	
	@Autowired
	private ProjectRepository projectRepo;
	
	@Autowired
	private ComponentIdManager idMgr;
	
	@Autowired
	private DomainEventPublisher publisher;
	
	
	@Override
	public EntityId createNewRegistration(
		ProjectCode projectCode,
		String parentCode,		
		UUID clientId,
		long timeOutSeconds) throws NoSuchCodedItemException {
		
		return this.idMgr.createRegisteredId(
				projectCode, 
				parentCode, 
				clientId, 
				timeOutSeconds);
	}
	
	/**
	 * CREATE new component with id and data content 
	 * 
	 */
	public Component createComponent(EntityId id, CreateEntityContent data) throws
		DuplicateEntityException,
		NoSuchRegistrationException, 
		NoSuchCodedItemException,
		CollidingClientForRegistrationException,
		IllegalArgumentException {
		
		if( ! data.isValid()) {
			throw new IllegalArgumentException(	String.format(
					"no client id provided for %s", 
					id.toString()));
		}
		
		Project project = this.projectRepo.findByCode(id.project.toString());
		if(project != null) {			
			
			CodeFormat format = project.getComponentConfig();
			id = id.validate(format);
			if(id.isValid()) {	
				
				Component parent = null;
				
				if(data.hasParent()) {
					
					EntityId parentId = new EntityId(data.getParent());
					parentId = parentId.validate(format);
					if(!parentId.isValid())
						throw new IllegalArgumentException(
								String.format(
									"invalid parent Id: %s", 
									parentId.toString()));
					
					parent = this.loadParent(data.getParent());
					
				} 
					
				if(!this.componentExists(id)) {
					
					this.idMgr.grantRegisteredId(id, data.getClientId());
					
					if(parent != null) {
						if(parent.isValidChildCode(id)) {
							
							Aggregate aggregate = Component.create(
									id, 
									parent, 
									data, 
									format);
							
							this.componentRepo.save(aggregate.instance);
							this.componentRepo.save(parent);
							this.publisher.publish(aggregate.created);
							return aggregate.instance;
							
						} else
							throw new IllegalArgumentException(	String.format(
								"EntityId %s not valid for parent %s", 
								id.toString(), parent.getEntityCode().toString()));
					} else {
						
						Aggregate aggregate = Component.create(id, null, data, format);
						
						this.componentRepo.save(aggregate.instance);						
						this.publisher.publish(aggregate.created);
						return aggregate.instance;
					}
									
				} else
					throw new DuplicateEntityException(id.toString());
				
			} else
				throw new IllegalArgumentException(
					String.format(
						"invalid EntityId: %s", 
						id.toString()));
		} else 
			throw new NoSuchCodedItemException(id.project.toString());
	
	
	}
	
	
	/**
	 * READ component with id
	 */
	public Component readComponent(EntityId id) 
			throws NoSuchCodedItemException, Exception {
		
		Project project = projectRepo.findByCode(id.project.toString());
		if(project != null) {
						 
			CodeFormat config = project.getComponentConfig();
			id = id.validate(config);
			if(id.isValid()) {
				
				Component component= this.componentRepo.findByCode(id);
				if(component != null ) {
					return component;
				} else {
					throw new NoSuchCodedItemException(String.format(
						"no component %s found.", 
						id.toString()));
				}	
				
			} else  
				throw new IllegalArgumentException(String.format(
						"invalid EntityId: %s", 
						id.toString()));
			
		} else {
			throw new NoSuchCodedItemException(
				String.format(
					"no project code %s found.", 
					id.project.toString()));
		}
	}
	
	/**
	 * READ Root Component entities saved in repository under the given project identifier
	 * 
	 */
	public List<Component> readComponents(String projectCode) 
			throws NoSuchCodedItemException, Exception {
		
		Project project = this.projectRepo.findByCode(projectCode);
		if(project != null) {
			
			List<Component> components = this.componentRepo.findRootsByProjectCode(projectCode);
			return components;
			
		} else 
			throw new NoSuchCodedItemException("no such project found: " + projectCode); 
	}
	
	/**
	 * UPDATE the specified Component with new content and persist it.
	 * 
	 * Finds the identified Component if it exists and updates it, 
	 * otherwise an exception is thrown. 
	 * 
	 * @param id						unique identifier of function
	 * @param requestedData				data set for content update
	 * @throws NoSuchCodedItemException	if no Component exists for the id
	 * @throws Exception				in case of database / connection
	 * 									fault
	 * @throws IllegalArgumentException if id does not validate against
	 * 									configurations of id.project 
	 */
	public Component updateComponent(
		EntityId id, 
		UpdateEntityContent requestedData)
	
		throws 	IllegalArgumentException, 
				NoSuchCodedItemException, 
				Exception {
			
		Project project = this.projectRepo.findByCode(id.project.toString());
		if(project != null) {
			
			CodeFormat config = project.getComponentConfig();
			id = id.validate(config);
			if(id.isValid()) {
				
				EntityId requestedCode = new EntityId(
						requestedData.getSelf(),
						config);
				requestedCode.validate(config);
				if(requestedCode.isValid()) {
				
					Component legacy = this.componentRepo.findByCode(id);
					legacy.getCode().entity.init(config);
					Component updated = super.updateEntity(legacy, requestedData);
					return updated;
				} else 
					throw new IllegalArgumentException(String.format(
							"requested entity not valid for project settings: %s", 
							requestedCode.toString()));
			} else  
				throw new IllegalArgumentException(String.format(
					"invalid EntityId: %s", 
					id.toString()));
		} else 
			throw new NoSuchCodedItemException(String.format(
					"no such project: %s", id.project));	
	}
	
	
	/**
	 * DELETE a specified Component from repository.
	 * 
	 * If Component for given project code - entity code duplet is found, this 
	 * Component will be deleted from repository and a ComponentDeletedEvent 
	 * is published to the components topic.
	 * 
	 * @param projectCode
	 * @param id
	 * @throws NoSuchCodedItemException
	 */
	public List<EntityApiId> deleteComponent(EntityId id) 
			throws NoSuchCodedItemException, IllegalArgumentException {
		
		Project project = projectRepo.findByCode(id.project.toString());
		if(project != null) {
			
			CodeFormat config = project.getComponentConfig();
			id = id.validate(config);
			if(id.isValid()) {			
			
				Component component = this.componentRepo.findByCode(id);
							
				if(component != null) {
					return super.deleteEntity(component, true);
					
				}
				else {
					throw new NoSuchCodedItemException(id);
				}
			} else 
				throw new IllegalArgumentException(String.format(
						"invalid EntityId: %s",
						id.toString()));
			
		} else {
			throw new NoSuchCodedItemException(id.project.toString());
		}	
	}
	
	/**
	 * DELETE Project and all associated Components
	 * 
	 * If given project code is found, all components associated to this 
	 * project will be deleted, triggering ComponentEvents { DELETED } to be 
	 * published. Afterwards, the project code will be deleted from the 
	 * repository.
	 * 
	 * @param code							of the project to be deleted.
	 * @throws NoSuchCodedItemException		if no such project code found
	 */
	public void deleteProject(String code)  throws 
	NoSuchCodedItemException, Exception {
		
		Project project = projectRepo.findByCode(code);
		if(project != null) {
			
			List<Component> componentsOfProject = 
					this.componentRepo.findByProjectCode(code);
			this.deleteComponents(componentsOfProject);
			this.projectRepo.delete(project);
			
		} else {
			throw new NoSuchCodedItemException(
					String.format("no project code %s found.", code));
		}	
	}
	
	/**
	 * DELETE all specified components from repository and publish the 
	 * associated ComponentEvents. No consideration of child components.
	 * This is a brute force approach shooting all entities 
	 * in the head and kicking them out of the database. It's a jungle 
	 * out there... 
	 * 
	 */
	protected void deleteComponents(List<Component> components) {
		
		ArrayList<ComponentDeletedEvent> events = new ArrayList<ComponentDeletedEvent>();
		components.forEach(entity-> {			
			events.add(entity.delete());
			
		});
		this.componentRepo.deleteAll(components);
		if(!events.isEmpty()) {
			this.publisher.publish(events);
		}
	}
	
	
	@Override
	protected void deleteFromRepository(Component c) {
		
		this.componentRepo.delete(c);
		
	}
	
	@Override
	protected void publishRecoding(Component c, EntityId oldCode) {		
		
		if((oldCode != null) && !(oldCode.equals(c.getCode()))) {
			ComponentRecodedEvent event = c.recode(oldCode);
			this.publisher.publish(event);			
		}
		
	}
	
	@Override
	protected void publishDeletion(Component c) {
		ComponentDeletedEvent event = c.delete();
		this.publisher.publish(event);		
	}
	
	@Override
	protected void saveToRepository(Component c) {
		
		this.componentRepo.save(c);	
		
	}
	
	@Override
	protected Component find(EntityId id) {
		return this.componentRepo.findByCode(id);
	}
	
	@Override
	protected CodeFormat loadCodeConfig(ProjectCode project) {
		Project p = this.projectRepo.findByCode(project.toString());
		CodeFormat format = p.getComponentConfig();
		return format;
	}
	
	protected Component loadParent(EntityApiId id) throws NoSuchCodedItemException {
		 
		EntityId parentId = new EntityId(id); 
		Component parent = this.componentRepo.findByCode(parentId);
		if(parent == null)
			throw new NoSuchCodedItemException(parentId);
		
		return parent;
	}
	
	protected boolean componentExists(EntityId id) {
		
		return(this.componentRepo.findByCode(id) != null);			
	}
	
}
