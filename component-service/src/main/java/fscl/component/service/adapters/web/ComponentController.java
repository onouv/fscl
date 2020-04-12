package fscl.component.service.adapters.web;

import fscl.component.service.domain.ComponentService;
import fscl.core.api.CreateEntityContent;
import fscl.core.api.EntityApiId;
import fscl.core.api.EntityIdListResponse;
import fscl.core.api.EntityLifecycleResponse;
import fscl.core.api.EntityResponse;
import fscl.core.api.NewEntityIdRequest;
import fscl.core.api.UpdateEntityContent;
import fscl.core.auxil.CommonConfig;
import fscl.core.db.DuplicateEntityException;
import fscl.core.db.NoSuchCodedItemException;
import fscl.core.domain.EntityId;
import fscl.core.domain.ProjectCode;
import fscl.component.service.domain.Component;
import fscl.component.service.adapters.web.API;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ComponentController {
	
	private static final Logger log=LoggerFactory.getLogger(ComponentController.class);
	
	@Autowired
	public ComponentService service;
	
	
	/**
	 * CREATE a new component entity registration 
	 * 
	 */
	@CrossOrigin(origins=CommonConfig.Web.allowedCorsClient)
	@RequestMapping(
			value= {
				API.Component.NewId.url,
				API.Component.Parent.NewId.url				
			},
			method=RequestMethod.POST)
	public ResponseEntity<EntityLifecycleResponse>
	
	createNewEntityRegistration(
			
		@PathVariable(API.Component.NewId.pathVar) String projectCode,
		@PathVariable(API.Component.Parent.NewId.pathVar) Optional<String> parentCode,
		@RequestBody NewEntityIdRequest body ) {
		
		String parent = null;
		if(parentCode.isPresent()) {
			parent = parentCode.get();
		}
			
		
		try {
			
			EntityId id = this.service.createNewRegistration(
					new ProjectCode(projectCode),
					parent,
					body.getClientId(),
					body.getTimeoutSeconds());
			
			log.info("CREATED component id reservation: {}", id.toString());			
			return new ResponseEntity<EntityLifecycleResponse>(
					new EntityLifecycleResponse(id), 
					HttpStatus.OK);		
			
		} catch (IllegalArgumentException e) {
			String msg = this.buildIdMessage(projectCode, e);
			log.error(msg);
			return new ResponseEntity<EntityLifecycleResponse>(
					new EntityLifecycleResponse(
						new EntityId(projectCode, "void"),
						msg), 
					HttpStatus.BAD_REQUEST);
		   
		} catch (NoSuchCodedItemException e) {
			String msg = this.buildIdMessage(projectCode, e);
			log.error(msg);
			return new ResponseEntity<EntityLifecycleResponse>(
		    		new EntityLifecycleResponse(
	    				new EntityId(projectCode, "void"),
						msg),
		    		HttpStatus.NOT_FOUND);
		    
		} catch(Exception e) {
			
			String msg = this.buildIdMessage(projectCode, e);
			log.error(msg);
			return new ResponseEntity<EntityLifecycleResponse>(
		    		new EntityLifecycleResponse(
	    				new EntityId(projectCode, "void"),
						msg),
		    		HttpStatus.INTERNAL_SERVER_ERROR);						
		}		
	}
	
	/**
	 * CREATE a new component resource
	 * 
	 * POST /api/v4/components/{project}/{entity}
	 * 
	 */
	@CrossOrigin(origins=CommonConfig.Web.allowedCorsClient)
	@RequestMapping(
			value= {API.Component.Create.url},
			method=RequestMethod.POST)
	public ResponseEntity<EntityLifecycleResponse> 
	
	createComponent(
			
			@PathVariable(value=API.Component.Create.project) String project,
			@PathVariable(value=API.Component.Create.entity) String entity,			
			@RequestBody CreateEntityContent body)  {
	
		
		EntityId id;
	
		try {
			id = new EntityId(project, entity);
			
			Component comp = this.service.createComponent(id, body);
			
			log.info("CREATE component: {}", 
					comp.toString());
			return new ResponseEntity<EntityLifecycleResponse>(
					new EntityLifecycleResponse(id), 
					HttpStatus.OK);
			
		} catch(IllegalArgumentException e) {
			
			String msg = this.buildCREATEMessage(project, entity, e);
			log.error(msg);
			return new ResponseEntity<EntityLifecycleResponse>(
					new EntityLifecycleResponse(
						project, 
						entity, 
						msg), 
					HttpStatus.BAD_REQUEST);
				
			
		} catch(DuplicateEntityException e) {
			
			String msg = this.buildCREATEMessage(project, entity, e);
			log.error(msg);
			return new ResponseEntity<EntityLifecycleResponse>(
					new EntityLifecycleResponse(
						project, 
						entity, 
						msg), 
					HttpStatus.CONFLICT);			
		    
		} catch(NoSuchCodedItemException e) {
			
			String msg = this.buildCREATEMessage(project, entity, e);
			log.error(msg);
			return new ResponseEntity<EntityLifecycleResponse>(
					new EntityLifecycleResponse(
						project, 
						entity, 
						msg), 
					HttpStatus.NOT_FOUND);
					    
			
		} catch(Exception e) {
			
			String msg = this.buildCREATEMessage(project, entity, e);
			log.error(msg);
			return new ResponseEntity<EntityLifecycleResponse>(
					new EntityLifecycleResponse(
						project, 
						entity, 
						msg), 
					HttpStatus.INTERNAL_SERVER_ERROR);			
		}
	}
	
	/**
	 * READ ONE Component entity if it exists
	 * 
	 * @param projectCode
	 * @param componentCode
	 * @return a list of Component entities containing the single found one or empty list
	 */
	@CrossOrigin(origins=CommonConfig.Web.allowedCorsClient)
	@RequestMapping(value=API.Component.Read.url, method=RequestMethod.GET)
	public ResponseEntity<EntityResponse<Component>> 
	
	readComponent(
			
			@PathVariable(value=API.Component.Read.project) String projectCode,
			@PathVariable(value=API.Component.Read.entity) String componentCode ) {
		
		EntityId id;
		ArrayList<Component> returnList = new ArrayList<Component>();
		
		try {
			
			id = new EntityId(projectCode, componentCode);
			Component f = this.service.readComponent(id);
						
			if(f != null) {
				returnList.add(f);
				log.info("READ component: component={}", 
					f.toString());				
			} else {
				log.info("READ component: no such component={}", 
						id.toString());
			}				
			
			return new ResponseEntity<EntityResponse<Component>>(
				new EntityResponse<Component>(returnList),
				HttpStatus.OK);
			
			
		} catch(IllegalArgumentException e) {
			
			String msg = this.buildREADMessage(projectCode, componentCode, e.getMessage());
			log.error(msg);
			return new ResponseEntity<EntityResponse<Component>>(
					new EntityResponse<Component>(msg), 
					HttpStatus.BAD_REQUEST);			
			
			
		} catch(NoSuchCodedItemException e) {
			
			String msg = this.buildREADMessage(projectCode, componentCode, e.getMessage());
			log.error(msg);
			return new ResponseEntity<EntityResponse<Component>>(
					new EntityResponse<Component>(msg), 
					HttpStatus.NOT_FOUND);		
			
			
		} catch(Exception e) {
			
			String msg = this.buildREADMessage(projectCode, componentCode, e.getMessage());
			log.error(msg);
			return new ResponseEntity<EntityResponse<Component>>(
					new EntityResponse<Component>(msg), 
					HttpStatus.INTERNAL_SERVER_ERROR);			
		}
	}
	
	
	/**
	 * READ ALL component entities of given project if it exists
	 * 
	 * @param projectCode
	 * @return
	 */
	@CrossOrigin(origins=CommonConfig.Web.allowedCorsClient)
	@RequestMapping(value=API.Component.Project.ReadAll.url, method=RequestMethod.GET)
	public ResponseEntity<EntityResponse<Component>> 
	
	readComponentsOfProject(
			
			@PathVariable(value=API.Component.Project.ReadAll.project) String projectCode ) {
		
		List<Component> components = new ArrayList<Component>();
		
		if(projectCode == null) {
			String msg = this.buildREADOfProjectMessage(projectCode, "cannot read project with null code");
			log.error(msg);			
			return new ResponseEntity<EntityResponse<Component>>(
					new EntityResponse<Component>(msg), 
					HttpStatus.BAD_REQUEST);
		    			
		}
		
		if(projectCode.isEmpty()) {
			String msg = this.buildREADOfProjectMessage(projectCode, "cannot read project with empty code");
			log.error(msg);			
			return new ResponseEntity<EntityResponse<Component>>(
					new EntityResponse<Component>(msg), 
					HttpStatus.BAD_REQUEST);
		}
		
		try {
					
			components = this.service.readComponents(projectCode);
			log.info("READ list of components for project: {}", projectCode);	
			EntityResponse<Component> r = new EntityResponse<Component>(components);
			return new ResponseEntity<EntityResponse<Component>>(r, HttpStatus.OK);
			
		} catch(NoSuchCodedItemException e) {
			
			String msg = this.buildREADOfProjectMessage(projectCode, e.getMessage());
			log.error(msg);
			return new ResponseEntity<EntityResponse<Component>>(
					new EntityResponse<Component>(msg), 
					HttpStatus.NOT_FOUND);
			
		} catch(Exception e) {
			
			String msg = this.buildREADOfProjectMessage(projectCode, e.getMessage());
			log.error(msg);
			return new ResponseEntity<EntityResponse<Component>>(
					new EntityResponse<Component>(msg), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * UPDATE component resource if it exists
	 * 
	 * No Messages triggered. Note that only { projectCode, enitityCode } together 
	 * are valid as unique identifiers across all of the FSCL domain.
	 * 
	 */
	@CrossOrigin(origins=CommonConfig.Web.allowedCorsClient)
	@RequestMapping(value=API.Component.Update.url, method=RequestMethod.PUT)
	public ResponseEntity<EntityLifecycleResponse> 
	
	updateComponent(
			
		@PathVariable(value=API.Component.Update.project) String project,
		@PathVariable(value=API.Component.Update.entity) String entity,		
		@RequestBody UpdateEntityContent body) 
	{
		EntityId id;
		
		try {
			
			id = new EntityId(project, entity);
			
			
			Component comp = this.service.updateComponent(id, body);
			if(comp == null) {
				throw new NoSuchCodedItemException();
			}
			log.info("updated component: component={}", 
					comp.toString());
			
			return new ResponseEntity<EntityLifecycleResponse>(
				new EntityLifecycleResponse(comp.getCode()),HttpStatus.OK);
			
			
		} catch(IllegalArgumentException e) {
			
			String msg = this.buildUPDATEMessage(project, entity, e);
			log.error(msg);
			return new ResponseEntity<EntityLifecycleResponse>(
					new EntityLifecycleResponse(
						project, 
						entity, 
						msg), 
					HttpStatus.BAD_REQUEST);
			
		} catch(NoSuchCodedItemException e) {
			
			String msg = this.buildUPDATEMessage(project, entity, e);
			log.error(msg);
			return new ResponseEntity<EntityLifecycleResponse>(
					new EntityLifecycleResponse(
						project, 
						entity, 
						msg), 
					HttpStatus.NOT_FOUND);
						
		} catch(Exception e) {
			
			String msg = this.buildUPDATEMessage(project, entity, e);
			log.error(msg);
			return new ResponseEntity<EntityLifecycleResponse>(
					new EntityLifecycleResponse(
						project, 
						entity, 
						msg), 
					HttpStatus.INTERNAL_SERVER_ERROR);			
		}
	}
	
	/**
	 * DELETE component resource if it exists
	 * 
	 * @param projectCode
	 * @param code
	 * @return
	 */
	@CrossOrigin(origins=CommonConfig.Web.allowedCorsClient)
	@RequestMapping(value=API.Component.Delete.url, method=RequestMethod.DELETE)
	public ResponseEntity<EntityIdListResponse> 
		
	deleteComponent(
			
		@PathVariable(value=API.Component.Delete.project) String projectCode,
		@PathVariable(value=API.Component.Delete.entity) String entityCode) {
		
		EntityId id;
		
		try {
			
			id = new EntityId(projectCode, entityCode);
			List<EntityApiId> deletedComponents =  this.service.deleteComponent(id);
			
			log.info("deleted component: {}:{}",
					projectCode, entityCode);
			return new ResponseEntity<EntityIdListResponse>(
				new EntityIdListResponse(deletedComponents), 
				HttpStatus.OK);
			
		} catch(IllegalArgumentException e) {
			
			String msg = this.buildDELETEMessage(projectCode, entityCode, e);
			log.error(msg);
			return new ResponseEntity<EntityIdListResponse>(
					new EntityIdListResponse(msg), 
					HttpStatus.BAD_REQUEST);
			
		} catch(NoSuchCodedItemException e) {
			
			String msg = this.buildDELETEMessage(projectCode, entityCode, e);
			log.error(msg);
			return new ResponseEntity<EntityIdListResponse>(
					new EntityIdListResponse(msg), 
					HttpStatus.NOT_FOUND);
			
		} catch(Exception e) {
			
			String msg = this.buildDELETEMessage(projectCode, entityCode, e);
			log.error(msg);
			return new ResponseEntity<EntityIdListResponse>(
					new EntityIdListResponse(msg), 
					HttpStatus.INTERNAL_SERVER_ERROR);			
		}		
	}
	
	protected String buildIdMessage(String project, Exception e) {
		return String.format("could not register component id for project {%s} due to exception %s", 
				project, 
				e.getMessage());
	}
	
	protected String buildCREATEMessage(String project, String entity, Exception e) {
		return String.format(
				"could not create component {%s:%s} due to exception %s", 
				project, entity, 
				e.getMessage());	
	}
	
	protected String buildREADOfProjectMessage(String projectCode, String detail) {
		return "could not read component of project {" + projectCode + "}: " + detail;
	}
	
	protected String buildREADMessage(String projectCode, String componentCode, String detail) {
		return "could not read component {" + projectCode + ":" + componentCode +"}: " + detail;
	}
	
	protected String buildUPDATEMessage(String projectCode, String componentCode, Exception e) {
		return "could not update component {" + projectCode + ":" + componentCode + "}: " + e.getMessage();
	}
	
	
	protected String buildDELETEMessage(String projectCode, String componentCode, Exception e) {
		return "could note delete component {" + projectCode + ":" + componentCode + "}: " + e.getMessage();
	}
	

}
