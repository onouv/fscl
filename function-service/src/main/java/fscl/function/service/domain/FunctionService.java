package fscl.function.service.domain;

import fscl.core.api.EntityApiId;
import fscl.core.api.CreateEntityContent;
import fscl.core.db.DuplicateEntityException;
import fscl.core.db.NoSuchCodedItemException;
import fscl.core.domain.EntityId;
import fscl.core.domain.EntityService;
import fscl.core.domain.CodeFormat;
import fscl.core.domain.ProjectCode;
import fscl.core.domain.registration.NoSuchRegistrationException;
import fscl.core.domain.registration.CollidingClientForRegistrationException;
import fscl.function.service.adapters.messaging.DomainEventPublisher;
import fscl.function.service.adapters.web.FunctionController;
import fscl.project.foreignkeys.Project;
import fscl.core.api.UpdateEntityContent;
import fscl.function.api.messaging.FunctionDeletedEvent;
import fscl.function.api.messaging.FunctionRecodedEvent;
import fscl.function.service.adapters.db.FunctionRepository;
import fscl.function.service.adapters.db.ProjectRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;


@Service
public class FunctionService extends EntityService<Function>  {

	private static final Logger log=LoggerFactory.getLogger(FunctionController.class);
	
	@Autowired
	private FunctionRepository functionRepo;
	
	@Autowired 
	private ProjectRepository projectRepo;
	
	@Autowired
	private DomainEventPublisher publisher;
	
	@Autowired
	private FunctionIdManager idMgr;
	
	@Autowired 
	private FunctionServiceHelper helper;
	
	/**
	 * 
	 * @param projectCode
	 * @param clientId
	 * @param timeOutSeconds
	 * @return
	 */
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
	 * CREATE a new function instance, persist it and publish the associated 
	 * domain events. 
	 *
	 * @return Function						a persisted function instance 
	 * @throws DuplicateEntityException		if function with code pre-existing
	 * @throws NoSuchCodedItemException		if no project found for projectCode
	 */
	public Function 
	createFunction(
			EntityId id, 
			CreateEntityContent data) 
		throws
			DuplicateEntityException,
			NoSuchRegistrationException, 
			NoSuchCodedItemException,
			CollidingClientForRegistrationException,
			IllegalArgumentException {
			
		FunctionServiceHelper.Validation validation = 
				this.helper.validateAsFunctionId(id);
		EntityId validatedFunctionId = validation.validId;
		CodeFormat format = validation.format;
		
		Function parent = null;
		
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
			
		if(!this.functionExists(validatedFunctionId)) {
			
			this.idMgr.grantRegisteredId(validatedFunctionId, data.getClientId());
			
			if(parent != null) {
				if(parent.isValidChildCode(validatedFunctionId)) {
					 
					Aggregate aggregate = Function.create(
							validatedFunctionId, 
							parent, 
							data, 
							format);
					
					this.functionRepo.save(aggregate.instance);
					this.functionRepo.save(parent);
					this.publisher.publish(aggregate.created);
					return aggregate.instance;
					
				} else
					throw new IllegalArgumentException(	String.format(
						"EntityId %s not valid for parent %s", 
						validatedFunctionId.toString(), 
						parent.getEntityCode().toString()));
			} else {
				
				Aggregate aggregate = Function.create(id, null, data, format);
				
				this.functionRepo.save(aggregate.instance);						
				this.publisher.publish(aggregate.created);
				return aggregate.instance;
			}
							
		} else
			throw new DuplicateEntityException(validatedFunctionId.toString());
	}
	
	
	protected Function loadParent(EntityApiId id) throws NoSuchCodedItemException {
		 
		EntityId parentId = new EntityId(id); 
		Function parent = this.functionRepo.findByCode(parentId);
		if(parent == null)
			throw new NoSuchCodedItemException(parentId);
		
		return parent;
	}
	
	protected boolean functionExists(EntityId id) {
		
		return(this.functionRepo.findByCode(id) != null);			
	}
	
	/**
	 * READ Root Function entities saved in repository under the given project identifier
	 * 
	 * @param projectCode				identifier of project
	 * @return							a list of all Function entities for project
	 * @throws NoSuchCodedItemException if no such project is found in repository
	 * @throws Exception				in case of database or other low-level fault
	 */
	public List<Function> readFunctions(String projectCode) 
			throws NoSuchCodedItemException, Exception {
		
		Project project = this.projectRepo.findByCode(projectCode);
		if(project != null) {
			
			List<Function> functions = this.functionRepo.findRootsByProjectCode(projectCode);
			return functions;
			
		} else 
			throw new NoSuchCodedItemException("no such project found: " + projectCode); 
	}
	
	/**
	 * READ single Function entity with given code within project with given code.
	 * 
	 * @param id						identifier of function entity
	 * @return Function 				the Function object found or null
	 * @throws NoSuchCodedItemException	if given project not in repository
	 * @throws Exception				in case of database or low-level fault
	 */
	public Function readFunction(final EntityId id) 
		throws 
			IllegalArgumentException, 
			NoSuchCodedItemException, 
			Exception {
		
		EntityId validatedId = this.helper.validateFunctionId(id);
		Function function =	this.functionRepo.findByCode(validatedId);
		
		return function;		
	}
		
	/**
	 * UPDATE the specified Function with new content and persist it.
	 * 
	 * Finds the identified Function if it exists and updates it, 
	 * otherwise an exception is thrown. 
	 * 
	 * @param id						unique identifier of function
	 * @param requestedData				data set for content update
	 * @throws NoSuchCodedItemException	if no Function exists for the id
	 * @throws Exception				in case of database / connection
	 * 									fault
	 * @throws IllegalArgumentException if id does not validate against
	 * 									configurations of id.project 
	 */
	public Function updateFunction(
		final EntityId id, 
		final UpdateEntityContent requestedData)
	
		throws 	IllegalArgumentException, 
				NoSuchCodedItemException, 
				Exception {
		
		FunctionServiceHelper.Validation validation = 
				this.helper.validateAsFunctionId(id);		
				
		EntityId requestedCode = new EntityId(
				requestedData.getSelf(),
				validation.format);
		
		requestedCode.validate(validation.project.getFunctionConfig());
		if(requestedCode.isValid()) {
		
			Function legacy = this.functionRepo.findByCode(validation.validId);
			legacy.getCode().entity.init(validation.format);
			Function updated = super.updateEntity(legacy, requestedData);
			return updated;
		} else 
			throw new IllegalArgumentException(String.format(
					"requested entity not valid for project settings: %s", 
					requestedCode.toString()));
	}
	
	
	/**
	 * DELETE a specified Function from repository.
	 * 
	 * If Function for given project code - function code duplet is found, this 
	 * Function will be deleted from repository and a FunctionDeletedEvent 
	 * is published to the functionTopic.
	 * 
	 * @param projectCode
	 * @param id
	 * @throws NoSuchCodedItemException
	 */
	public List<EntityApiId> deleteFunction(final EntityId id) 
			throws NoSuchCodedItemException, IllegalArgumentException {
		
		EntityId validatedId = this.helper.validateFunctionId(id);
		
		Function function = this.functionRepo.findByCode(validatedId);
		
		if(function != null) {			
			List<EntityApiId> deleted = super.deleteEntity(function, true);
			return deleted;
			
		}
		else {
			throw new NoSuchCodedItemException(validatedId);
		}
	}
	
	@Override
	protected void deleteFromRepository(final Function f) {
		
		this.functionRepo.delete(f);
		
	}
	
	@Override
	protected void publishRecoding(final Function f, final EntityId oldCode) {		
		
		if((oldCode != null) && !(oldCode.equals(f.getCode()))) {
			FunctionRecodedEvent event = f.recode(oldCode);
			this.publisher.publish(event);			
		}
		
	}
	
	@Override
	protected void publishDeletion(Function f) {
		FunctionDeletedEvent event = f.delete();
		this.publisher.publish(event);
	}
	
	@Override
	protected void saveToRepository(Function f) {
		
		this.functionRepo.save(f);	
		
	}
	
	
	/**
	 * DELETE Project and all associated Functions
	 * 
	 * If given project code is found, all functions associated to this 
	 * project will be deleted, triggering FunctionDeletedEvents to be 
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
			
			List<Function> functionsOfProject = 
					this.functionRepo.findByProjectCode(code);
			this.deleteFunctions(functionsOfProject);
			this.projectRepo.delete(project);
			
		} else {
			throw new NoSuchCodedItemException(
					String.format("no project code %s found.", code));
		}	
	}
	
	/**
	 * DELETE all specified functions from repository and publish the 
	 * associated FunctionEvents. No consideration of child functions.
	 * This is a brute force approach shooting all function entities 
	 * in the head and kicking them out of the database. It's a jungle 
	 * out there... 
	 * 
	 * @param functions		the functions doomed for extermination
	 */
	protected void deleteFunctions(List<Function> functions) {
		
		ArrayList<FunctionDeletedEvent> events = 
			new ArrayList<FunctionDeletedEvent>();
		
		functions.forEach(function -> {			
			events.add(function.delete());
			
		});
		this.functionRepo.deleteAll(functions);
		if(!events.isEmpty()) {
			this.publisher.publish(events);
		}
	}
	
	@Override
	protected Function find(EntityId id) {
		return this.functionRepo.findByCode(id);
	}
	
	@Override
	protected CodeFormat loadCodeConfig(ProjectCode project) {
		Project p = this.projectRepo.findByCode(project.toString());
		CodeFormat format = p.getFunctionConfig();
		return format;
	}
}
