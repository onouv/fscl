package fscl.projectservice.adapters.db;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import fscl.projectservice.domain.IdRegistration;


public interface ProjectCodeCacheRepository extends MongoRepository<IdRegistration, String> {
	
	public List<IdRegistration> findByClientId(UUID clientId);	
	public List<IdRegistration> findByCode(String code);	
	public List<IdRegistration> deleteByCode(String code);
}
