package fscl.core.domain.registration;

import fscl.core.domain.CodeFormat;
import fscl.core.domain.EntityCode;
import fscl.core.auxil.CommonConfig;
import fscl.core.domain.EntityId;
import fscl.core.domain.ProjectCode;
import fscl.core.db.NoSuchCodedItemException;
import fscl.core.db.IdRegistrationRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public abstract class IdRegistrationManager {
	
	@Autowired
	protected IdRegistrationRepository registrationRepo;
	
	protected CodeFormat codeFormat;
	
	/**
	 * Generate a new valid EntityId according to current CodeConfig of the given 
	 * project, register it for the given client and return it to client.
	 * 
	 * @param projectCode
	 * @param clientId
	 * @param timeOutSeconds
	 * @return
	 * @throws NoSuchCodedItemException
	 */
	public EntityId createRegisteredId(
		ProjectCode projectCode,
		String parentCode,
		UUID clientId, 
		long timeOutSeconds) 
		throws NoSuchCodedItemException, IllegalArgumentException {
		
		if(timeOutSeconds < 0) 
			throw new IllegalArgumentException(
					"A negative value for expiration time was provided, where non-negative expected.");

		if(timeOutSeconds > CommonConfig.Web.CachedCodes.maxTimeoutSeconds) 
			throw new IllegalArgumentException(
					String.format("A value for expiration time exceeding the limit of %d s was provided.", 
					CommonConfig.Web.CachedCodes.maxTimeoutSeconds));
			
		if(clientId == null)
			throw new IllegalArgumentException(
				String.format("client id must be a valid UUID"));
		
		this.codeFormat = this.loadCodeConfigFor(projectCode);
				
		List<EntityId> committed, registered;		
		if(parentCode == null) {
			
			committed = this.listCommittedRootsFor(projectCode, this.codeFormat);
			registered = this.listRegisteredRootsFor(projectCode);				
			
		} else {
						
			EntityId parentId = new EntityId(
					projectCode, 
					new EntityCode(parentCode, this.codeFormat));
			committed = this.listCommittedChildsFor(parentId, this.codeFormat);
			registered = this.listRegisteredChildsFor(parentId);
		}
				
		// from these, create a valid new code...
		EntityId newId = DefaultIdProvider.generateNewId(
			committed, 
			registered, 
			parentCode,
			projectCode,			
			this.codeFormat);
		IdRegistration newRegistration = new IdRegistration(
			newId, 
			clientId, 
			timeOutSeconds);
					
		// ...and persist it
		this.registrationRepo.save(newRegistration);		
		
		return newId;
	}
	
	
	/**
	 * Verify that id has been registered under clientId and purge it from the database
	 * 
	 * Semantics are as follows: 
	 * 
	 *  1) 	DENY to anyone if not registered at all
	 *  2)  DENY to anyone except original clientId during expiration period. 
	 *  	GRANT to anyone	afterwards. 
	 *  3) 	If a registered id is used, it's registration will be deleted 
	 *  	from the repository. This will eventually purge our own used ones 
	 *  	and all expired ones from anybody else if we did use them.
	 *     
	 * @param id
	 * @param clientId
	 * @throws NoSuchCodedItemException		if no id has been registered under given clientId
	 * @throws DuplicateEntityException		if id has been registered by some other clientId 
	 * 
	 */
	public void grantRegisteredId(
			EntityId id, 
			UUID clientId) 
			throws 
				NoSuchRegistrationException, 
				CollidingClientForRegistrationException	 {

		IdRegistration registered = this.registrationRepo.findByEntityId(id);
		
		// DENY to anyone if not registered at all
		if(registered == null)
			throw new NoSuchRegistrationException(id);
					
		// DENY to anyone except original clientId during expiration period...
		LocalDateTime now = LocalDateTime.now();
		if( ! registered.hasExpired(now)) {
			if(! registered.getClientId().equals(clientId))
			
				throw new CollidingClientForRegistrationException(registered, clientId);				
		}	
		
		// ... GRANT to anyone afterwards ... 
		
		
		// ... in which case we erase it- 
		this.registrationRepo.delete(registered);

	}
	
	
	protected List<EntityId> listRegisteredRootsFor(ProjectCode project) {
		
		List<IdRegistration> registeredForProject = this.registrationRepo
				.findByEntityIdIsContaining(project);
		
		List<EntityId> registeredRoots = new ArrayList<EntityId>();
		registeredForProject.forEach((reg) -> {		
			EntityId id = reg.getId();
			id.entity.init(this.codeFormat);
			if(id.entity.isRoot()) {				
				registeredRoots.add(id);
			}
		});
			
		return registeredRoots;
			
	}
	
	protected List<EntityId> listRegisteredChildsFor(EntityId parent) {
		
		ProjectCode project = parent.project;
		
		List<IdRegistration> registeredForProject = this.registrationRepo
				.findByEntityIdIsContaining(project);
		
		List<EntityId> registeredChilds = new ArrayList<EntityId>();
		registeredForProject.forEach((reg) -> {
			EntityCode childsECode = reg.getId().entity;
			childsECode.init(this.codeFormat);
			ProjectCode childsPCode = reg.getId().project;			
			if(childsPCode.equals(parent.project))	{				
				EntityCode p = childsECode.getParent();
				if(p != null) {
					if(p.equals(parent.entity)) {
						reg.getId().entity.init(this.codeFormat);
						registeredChilds.add(reg.getId());				
					}
				}
			}
		});		
		return registeredChilds;		
	}
	
	
		
	
	
	/**
	 * Retrieve a list of all EntityId already committed to the functions database.
	 * Note this may become a large object. 
	 * 
	 * @return
	 */
	protected abstract List<EntityId> listCommittedRootsFor(
			ProjectCode projectCode, 
			CodeFormat format);
	
	protected abstract List<EntityId> listCommittedChildsFor(
			EntityId parentCode,
			CodeFormat format)
			throws NoSuchCodedItemException;
	
	protected abstract CodeFormat loadCodeConfigFor(ProjectCode project)
		throws NoSuchCodedItemException;
		
}
