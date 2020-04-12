package fscl.core.db;

import fscl.core.domain.registration.IdRegistration;
import fscl.core.domain.EntityId;
import fscl.core.domain.ProjectCode;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;

@Repository
public interface IdRegistrationRepository 
	extends MongoRepository<IdRegistration, EntityId> {

	/*
	//public List<IdRegistration> findByClientId(UUID clientId);
	
	//@Query("{'entityId.project': ?0}")
	//public List<IdRegistration> findByProjectCodeAndClientId(			 
	//		ProjectCode code,
	//		UUID clientId);
	*/
	public IdRegistration findByEntityId(EntityId entityId);
	
	@Query("{'entityId.project': ?0}")
	public List<IdRegistration> findByProjectCode(ProjectCode code);
	
	public List<IdRegistration> deleteByEntityId(EntityId entityId);
}
