package fscl.projectservice.adapters.web;

import fscl.core.api.Response;
import fscl.core.auxil.CommonConfig;
import fscl.core.db.DuplicateEntityException;
import fscl.core.db.NoSuchCodedItemException;
import fscl.projectservice.domain.ProjectService;
import fscl.projectservice.domain.Project;
import fscl.projectservice.api.RetrieveProjectsResponse;
import fscl.projectservice.api.Url;
import fscl.projectservice.api.CreateProjectRequest;
import fscl.projectservice.api.NewProjectCodeRequest;
import fscl.projectservice.api.NewProjectCodeResponse;
import fscl.projectservice.api.ProjectData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
public class ProjectController {	
	
	@Autowired
	@Qualifier("ProjectService")
	private ProjectService service;
	
	private static final Logger log=LoggerFactory.getLogger(ProjectController.class);
	
	/**
	 * CREATE a new project id to be assigned to a project created new on the client.
	 * The client may subsequently call CREATE on a project with that id. 
	 * @param request
	 * @return
	 */
	@CrossOrigin(origins=CommonConfig.Web.allowedCorsClient)	
	@RequestMapping(value = Url.CREATE.ProjectCode.url, method=RequestMethod.POST)
	ResponseEntity<NewProjectCodeResponse>	createNewProjectCode(
		@RequestBody NewProjectCodeRequest request) {
		
		try {
			
			String newCode = service.createNewProjectCode(request.getClientId(), request.getTimeoutSeconds());
			ResponseEntity<NewProjectCodeResponse> responseEntity = new ResponseEntity<NewProjectCodeResponse>(
					new NewProjectCodeResponse(newCode),
					HttpStatus.OK);
			
			log.info("new project code cached : \tcode={}\ttime out={} s\tclient={}", 
					newCode,
					request.getTimeoutSeconds(),
					request.getClientId());
			return responseEntity;
			
		} catch (IllegalArgumentException e) {			
			NewProjectCodeResponse response = new NewProjectCodeResponse();
			response.setError(e.getMessage());
			ResponseEntity<NewProjectCodeResponse> responseEntity = new ResponseEntity<NewProjectCodeResponse>(
					response,
					HttpStatus.BAD_REQUEST);
			log.error("exception while serving request for new project code \ttimeout={}\tclient={}; message={}",
					request.getTimeoutSeconds(),
					request.getClientId(),
					e.getMessage());
			return responseEntity;
			
		} catch (Exception e) {			
			NewProjectCodeResponse response = new NewProjectCodeResponse();
			response.setError(e.getMessage());
			
			ResponseEntity<NewProjectCodeResponse> responseEntity = new ResponseEntity<NewProjectCodeResponse>(
					response,
					HttpStatus.INTERNAL_SERVER_ERROR);
			log.error("general exception while serving request for new project code \ttimeout={}\tclient={}; message={}",
					request.getTimeoutSeconds(),
					request.getClientId(),
					e.getMessage());
			return responseEntity;
		}		
					
	}
	
	/**
	 * CREATE a new project
	 * @param req		ProjectData
	 * @return			ResponseEntity<Response>
	 */
	@CrossOrigin(origins=CommonConfig.Web.allowedCorsClient)
	@RequestMapping(value=Url.CREATE.Project.url, method=RequestMethod.POST)
	ResponseEntity<Response> createProject(
		@RequestBody CreateProjectRequest request) {
		
		try {
			Project project = service.createNewProject(
					request.getData(), 
					request.getClientId(),
					request.functionFormat,
					request.systemFormat,
					request.componentFormat,
					request.locationFormat);			
			log.info("new project saved: {}", project.toString());
			return new ResponseEntity<Response>(new Response(), HttpStatus.OK);
			
		} catch (IllegalArgumentException e) { 
			
			log.error("exception while serving request to create project: code={}\tmessage={}",
					request.getData().getCode(),
					e.getMessage());
			return new ResponseEntity<Response>(
					new Response(e.getMessage()), 
					HttpStatus.BAD_REQUEST);
			
		} catch (DuplicateEntityException e) {			
			
			log.error("exception while serving request to create project: code={}\tmessage={}",
					request.getCode(),
					e.getMessage());
			
			return new ResponseEntity<Response>(
					new Response(e.getMessage()), 
					HttpStatus.CONFLICT);
		}
	}
	
	/**
	 * RETRIEVE a list of projects
	 * @return
	 */
	@CrossOrigin(origins=CommonConfig.Web.allowedCorsClient)
	@RequestMapping(value=Url.READ.Project.url, method=RequestMethod.GET)
	ResponseEntity<RetrieveProjectsResponse> retrieveProjects() {
		
		try {
			List<Project> projects = service.getProjects();
								
			ResponseEntity<RetrieveProjectsResponse> responseEntity = new ResponseEntity<RetrieveProjectsResponse>(
					new RetrieveProjectsResponse(projects),
					HttpStatus.OK);
			
			log.info("project list retrieved: serving {} projects", projects.size());
			return responseEntity; 
		}
		catch (Exception e) {		
			
			// mask unknown error, so user can get at least a message
			String msg;
			if (e.getMessage().isEmpty()) {
				msg = "unknown error";
			}
			else {
				msg = e.getMessage();
			}
			
			ResponseEntity<RetrieveProjectsResponse> responseEntity = new ResponseEntity<RetrieveProjectsResponse>(
					new RetrieveProjectsResponse(msg),
					HttpStatus.INTERNAL_SERVER_ERROR);
			
			log.error("Exception while serving GET project list request. Message={}", msg);
			return responseEntity;			
		}
	}
	
	/**
	 * UPDATE a project
	 * 
	 * @param projectCode
	 * @param request
	 * @return
	 */
	@CrossOrigin(origins=CommonConfig.Web.allowedCorsClient)
	@RequestMapping(value=Url.UPDATE.Project.url, method=RequestMethod.PUT)
	ResponseEntity<Response> updateProject(	
			@PathVariable(value=Url.UPDATE.Project.pathVariable) String projectCode,
			@RequestBody ProjectData request) {
		
		if(!projectCode.equals(request.getCode())) {
			return new ResponseEntity<Response>(
					new Response("path variable and request body indicate mismatching resource code"),
					HttpStatus.BAD_REQUEST);
		}
		
		try {
			this.service.updateProject(request);
			
			log.info(
					"project updated:\tcode={}, name={}, description={}", 
					request.getCode(), 
					request.getName(),
					request.getDescription()
			);
			
			return new ResponseEntity<Response>(new Response(), HttpStatus.OK);
			
		} 
		catch(NoSuchCodedItemException e) {
			String msg = "UPDATE : project " + projectCode + " not found.";
			log.error(msg);
			return new ResponseEntity<Response>(new Response(msg), HttpStatus.NOT_FOUND);
		}
		catch(Exception e) {			
			
			log.error(
					"exception while serving request to update project :\tcode={}, message={}", 
					projectCode, 
					e.getMessage());
			
			return new ResponseEntity<Response> ( 
					new Response(e.getMessage()), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * DELETE a project
	 * @param projectCode	String
	 * @return
	 */
	@CrossOrigin(origins=CommonConfig.Web.allowedCorsClient)
	@RequestMapping(value=Url.DELETE.Project.url, method=RequestMethod.DELETE)
	ResponseEntity<Response> deleteProject(
			@PathVariable(value=Url.DELETE.Project.pathVariable) String projectCode) {
		
		if(projectCode.isEmpty()) {
			
			String msg = String.format(
					"invalid argument while deleting project: %s}",
					projectCode);
				
				log.error(msg);
			return new ResponseEntity<Response>(
				new Response(msg), 
				HttpStatus.BAD_REQUEST);
		}
		try {
			Project deleted = this.service.deleteProject(projectCode);
			
			if(deleted != null) {
				log.info("Deleted project: {}", deleted.toString());
				return new ResponseEntity<Response>(new Response(), HttpStatus.OK);
			}
			else {
				String msg = String.format(	"project %s not found",	projectCode);  
				log.error("while serving delete request: " + msg);
				return new ResponseEntity<Response>(new Response(msg), HttpStatus.NOT_FOUND);
			}
		}
		catch(Exception e) {
			log.error("exception while serving request to save project: code={}\tmessage={}",
					projectCode,
					e.getMessage());
			
			return new ResponseEntity<Response>(
					new Response(e.getMessage()), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		 
		
	}

}