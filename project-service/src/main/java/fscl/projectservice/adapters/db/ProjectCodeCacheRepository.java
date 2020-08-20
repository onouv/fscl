package fscl.projectservice.adapters.db;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fscl.projectservice.domain.IdRegistration;

@Repository
public interface ProjectCodeCacheRepository extends CrudRepository<IdRegistration, String> {
	
	public List<IdRegistration> findByClientId(UUID clientId);	
	public List<IdRegistration> findByCode(String code);	
	public List<IdRegistration> deleteByCode(String code);
}
