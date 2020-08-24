package fscl.core.db;

import fscl.core.domain.EntityId;
import fscl.core.domain.ProjectCode;
import fscl.core.domain.registration.IdRegistration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdRegistrationRepository 
	extends CrudRepository<IdRegistration, EntityId> {

	/*
	//public List<IdRegistration> findByClientId(UUID clientId);
	
	//@Query("{'entityId.project': ?0}")
	//public List<IdRegistration> findByProjectCodeAndClientId(			 
	//		ProjectCode code,
	//		UUID clientId);
	*/
	public IdRegistration findByEntityId(EntityId entityId);
	
	//@Query("{'entityId.project': ?0}")
	public List<IdRegistration> findByEntityIdIsContaining(ProjectCode code);
	//findByProjectCode(ProjectCode code);
	
	public List<IdRegistration> deleteByEntityId(EntityId entityId);
}
