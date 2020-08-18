package fscl.projectservice.domain;

import fscl.projectservice.domain.IdRegistration;
import fscl.projectservice.adapters.db.ProjectRepository;
import fscl.projectservice.adapters.db.ProjectCodeCacheRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;

@Component()
class CodeFactory {
	
	@Autowired
	private ProjectRepository projectRepo;
	@Autowired
	private ProjectCodeCacheRepository codeCacheRepo;	
	@Autowired
	@Qualifier("ProjectCodeProvider")
	private CodeProvider codeProvider;

	public List<String> listCommittedCodes() {
		Iterable<Project> projects = this.projectRepo.findAll();
		List<String> committed = new ArrayList<String>();
		projects.forEach(project -> {
			committed.add(project.getCode().toString());
		});		
		return committed;
	}
	
	public List<String> listCachedCodesForClient(UUID clientId) {
		List<IdRegistration> cachedForClient = this.codeCacheRepo.findByClientId(clientId);
		List<String> cached = new ArrayList<String>();
		cachedForClient.forEach(code -> {
			cached.add(code.getCode());
		});		
		return cached;
	}
	
	public String generateCode(List<String> committedCodes, List<String> cachedCodes) {
		return codeProvider.generateCode(committedCodes, cachedCodes);
	}
}
