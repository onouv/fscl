package fscl.projectservice.domain;

import fscl.core.auxil.CommonConfig;
import fscl.core.db.DuplicateEntityException;
import fscl.core.db.NoSuchCodedItemException;
import fscl.core.domain.CodeFormat;
import fscl.core.domain.ProjectCode;
import fscl.messaging.DomainEventPublisher;
import fscl.messaging.events.ResultDomainEventsAggregate;
import fscl.project.api.events.ProjectEvent;
import fscl.projectservice.domain.IdRegistration;
import fscl.projectservice.domain.Project;
import fscl.projectservice.api.ProjectData;
import fscl.projectservice.adapters.db.ProjectRepository;
import fscl.projectservice.adapters.db.ProjectCodeCacheRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component("ProjectService")
@EnableBinding(Source.class)
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepo;
	@Autowired
	private ProjectCodeCacheRepository codeCacheRepo;
	@Autowired
	private CodeFactory codeProvider;

	@Autowired
	private DomainEventPublisher<ProjectEvent> publisher;


	/**
	 * CREATE a new project code.
	 * This will generate a new code and cache it for the specified clientId.
	 * After the secondsExpiring specified, the cached code will be considered "timed-out" (invalid),
	 * i.e. other clients my "take" it when creating a new object. Before that time, this client may
	 * "take" it for this purpose.
	 *
	 * @param 	UUID 		clientId
	 * @param 	long 		secondsExpiring		num seconds into future until code will be considered
	 * 											timed out (invalid)
	 * @return	String		code				the generated and cached code
	 * @throws IllegalArgumentException
	 */
	public String createNewProjectCode(
			UUID clientId,
			long secondsExpiring)
			throws IllegalArgumentException {

		if(secondsExpiring < 0)
			throw new IllegalArgumentException(
					"A negative value for expiration time was provided, where non-negative expected.");

		if(secondsExpiring > CommonConfig.Web.CachedCodes.maxTimeoutSeconds)
			throw new IllegalArgumentException(
					String.format("A value for expiration time exceeding the limit of %d s was provided.",
					CommonConfig.Web.CachedCodes.maxTimeoutSeconds));


		// lookup all relevant project codes
		List<String> committed = codeProvider.listCommittedCodes();
		List<String> cached = codeProvider.listCachedCodesForClient(clientId);

		// from these, create a valid new code...
		String newCode = codeProvider.generateCode(committed, cached);
		IdRegistration codeToCache = new IdRegistration(newCode, clientId, secondsExpiring);

		// ...and persist it
		this.codeCacheRepo.save(codeToCache);
		return codeToCache.getCode();
	}

	/**
	 * CREATE a new project
	 *
	 * @param id			String 			domain identifier, required
	 * @param name			String 			optional
	 * @param description	String
	 * @param clientId		UUID			referring to any preregistered
	 * 										project codes
	 * @return
	 * @throws DuplicateEntityException		if clientId was preregistered
	 * 										by other client
	 * @throws IllegalArgumentException		if no code is provided
	 */
	public Project createNewProject(
		ProjectData data,
		UUID   clientId,
		CodeFormat functionConfig,
		CodeFormat systemConfig,
		CodeFormat locationConfig,
		CodeFormat componentConfig)

		throws DuplicateEntityException, IllegalArgumentException {

		ProjectCode code = data.asProjectCode();
		String name = data.getName();
		String description = data.getDescription();

		if(code.isEmpty())
			throw new IllegalArgumentException("project must have a code");

		this.handleCachedCodes(code.toString(), clientId);

		Project p = this.projectRepo.findByCode(code.toString());
		if (p == null ) {

			ResultDomainEventsAggregate<Project, ProjectEvent> res = Project.create(
				code,
				name,
				description,
				functionConfig,
				systemConfig,
				locationConfig,
				componentConfig);
			Project newProject = res.result;
			List<ProjectEvent> events = res.events;

			this.projectRepo.save(newProject);
			this.publisher.publish(events);

			return newProject;
		}
		else {
			throw new DuplicateEntityException(
					"project with code " + code +
					" already in database");
		}

	}


	private void handleCachedCodes(String code, UUID clientId) throws DuplicateEntityException {

		List<IdRegistration> cachedCodes = this.codeCacheRepo.findByCode(code);
		LocalDateTime now = LocalDateTime.now();
		for(IdRegistration cached : cachedCodes) {

			// ensure, cached is either expired or it was locked by ourselves
			if(!cached.hasExpired(now)) {
				if(!cached.getClientId().equals(clientId)) {
					// bail out screaming
					throw new DuplicateEntityException(
							"project code " + code +
							" has been locked for another client");
				}
			}

			// erase it- this will eventually purge our own used ones
			// and all expired ones from anybody else if we did use them
			this.codeCacheRepo.delete(cached);
		}

	}


	/**
	 * READ all projects
	 * @return	List<Project>	all projects found in database
	 * @throws 	Exception		in case of lower layer problems
	 */
	public List<Project> getProjects() throws Exception {

		return this.projectRepo.findAll();
	}

	/**
	 * UPDATE a given project
	 *
	 * @param 	ProjectData  	data
	 * @return	boolean 		true if project found and updated
	 * @throws Exception
	 */
	public void updateProject(ProjectData data) throws NoSuchCodedItemException, Exception {
		Project project = this.projectRepo.findByCode(data.getCode().toString());
		if(project != null) {
			project.setCode(data.asProjectCode());
			project.setName(data.getName());
			project.setDescription(data.getDescription());

			this.projectRepo.save(project);
		}
		else {
			throw new NoSuchCodedItemException(data.getCode().toString());
		}
	}

	/**
	 * DELETE project identified by given code
	 *
	 * @param 	String 		code		identifier of project to be deleted
	 * @return	Project		project		if found and deleted, any other case null
	 * @throws 	Exception				in case of lower layer problem
	 */
	public Project deleteProject( String code) throws Exception {
		Project project = this.projectRepo.findByCode(code);
		if( project != null ) {
			List<ProjectEvent> events = project.delete();
			this.projectRepo.delete(project);
			this.publisher.publish(events);
		}
		return project;
	}

}
