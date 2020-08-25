package fscl.function.service.adapters.web;

import fscl.core.api.*;
import fscl.core.auxil.CommonConfig;
import fscl.core.db.DuplicateEntityException;
import fscl.core.db.NoSuchCodedItemException;
import fscl.core.domain.EntityId;
import fscl.core.domain.ProjectCode;
import fscl.function.service.api.Url;
import fscl.function.service.domain.Function;
import fscl.function.service.domain.FunctionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class FunctionController extends FunctionControllerBase {
	
	private static final Logger log=LoggerFactory.getLogger(FunctionController.class);
	
	@Autowired	
	private FunctionService service;
	
	//
	//CREATE new entity id registration
	//
	@CrossOrigin(origins=CommonConfig.Web.allowedCorsClient)
	@RequestMapping(
			value= {
				"/api/v4/functions/new/{project}",
				"/api/v4/functions/new/{project}/{parent}"
			},
			method=RequestMethod.POST)
	public ResponseEntity<EntityLifecycleResponse>
	createNewEntityRegistration(
		@PathVariable("project") String projectCode,
		@PathVariable("parent") Optional<String> parentCode,
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
			
			log.info("CREATED function id reservation: {}", id.toString());			
			return new ResponseEntity<>(
					new EntityLifecycleResponse(id),
					HttpStatus.OK);		
			
		} catch (IllegalArgumentException e) {
			String msg = this.buildIdMessage(projectCode, e);
			log.error(msg);
			return new ResponseEntity<>(
					new EntityLifecycleResponse(
							new EntityId(projectCode, "void"),
							msg),
					HttpStatus.BAD_REQUEST);
			
		} catch (NoSuchCodedItemException e) {
			String msg = this.buildIdMessage(projectCode, e);
			log.error(msg);
			return new ResponseEntity<>(
					new EntityLifecycleResponse(
							new EntityId(projectCode, "void"),
							msg),
					HttpStatus.NOT_FOUND);
			
		} catch(Exception e) {
			
			String msg = this.buildIdMessage(projectCode, e);
			log.error(msg);
			return new ResponseEntity<>(
					new EntityLifecycleResponse(
							new EntityId(projectCode, "void"),
							msg),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//
	//CREATE function resource
	//
	@CrossOrigin(origins=CommonConfig.Web.allowedCorsClient)
	@RequestMapping(value=Url.CREATE.Function.url, method=RequestMethod.POST)
	public ResponseEntity<EntityLifecycleResponse> 
	createFunction(
			@PathVariable(value=Url.CREATE.Function.pathVarProject) String project,
			@PathVariable(value=Url.UPDATE.Function.pathVarEntity) String entity,			
			@RequestBody CreateEntityContent body)  {
		
		EntityId id;
		
		try {
		
			id = new EntityId(project, entity);
			Function f = this.service.createFunction(id, body);
			log.info("CREATE function: function={}", 
					f.toString());
			return new ResponseEntity<>(
					new EntityLifecycleResponse(id),
					HttpStatus.OK);
			
			
		} catch(IllegalArgumentException e) {
			
			String msg = this.buildFunctionMessage(
					project, 
					entity, 
					"create component ", 
					e);
					
			log.error(msg);
			return new ResponseEntity<>(
					new EntityLifecycleResponse(
							project,
							entity,
							msg),
					HttpStatus.BAD_REQUEST);			
			
		} catch(DuplicateEntityException e) {
			
			String msg = this.buildFunctionMessage(
					project, 
					entity, 
					"create function ", 
					e);
			log.error(msg);
			return new ResponseEntity<>(
					new EntityLifecycleResponse(
							project,
							entity,
							msg),
					HttpStatus.CONFLICT);
		    
		} catch(NoSuchCodedItemException e) {
			
			String msg = this.buildFunctionMessage(
					project, 
					entity, 
					"create function", 
					e);
			log.error(msg);
			return new ResponseEntity<>(
					new EntityLifecycleResponse(
							project,
							entity,
							msg),
					HttpStatus.NOT_FOUND);    
			
		} catch(Exception e) {
			
			String msg = this.buildFunctionMessage(
					project, 
					entity, 
					"create function ", 
					e);
			log.error(msg);
			return new ResponseEntity<>(
					new EntityLifecycleResponse(
							project,
							entity,
							msg),
					HttpStatus.INTERNAL_SERVER_ERROR);				
		}
	}
	
	//
	// READ ONE Function entity if it exists
	//
	@CrossOrigin(origins=CommonConfig.Web.allowedCorsClient)
	@RequestMapping(value=Url.READ.Function.url, method=RequestMethod.GET)
	public ResponseEntity<EntityResponse<Function>> 
	readFunction(
			@PathVariable(value=Url.READ.Function.pathVarProject) String project,
			@PathVariable(value=Url.READ.Function.pathVarEntity) String function ) {
		
		EntityId id;
		List<Function> functions = new ArrayList<>();
		
		try {
			
			id = new EntityId(project, function);
			Function f = this.service.readFunction(id);
						
			if(f != null) {
				functions.add(f);
				log.info("READ function: function={}", 
					f.toString());				
			} else {
				log.info("READ function: no such function={}", 
						id.toString());
			}				
			
			return new ResponseEntity<>(
					new EntityResponse<>(functions),
					HttpStatus.OK);
			
			
		} catch(IllegalArgumentException e) {
			
			String msg = this.buildFunctionMessage(
					project, 
					function, 
					"read function ", 
					e);					
			log.error(msg);
			return new ResponseEntity<>(
					new EntityResponse<>(msg),
					HttpStatus.BAD_REQUEST);
			
		} catch(NoSuchCodedItemException e) {
			
			String msg = this.buildFunctionMessage(
					project, 
					function, 
					"read function ", 
					e);					
			log.error(msg);
			return new ResponseEntity<>(
					new EntityResponse<>(msg),
					HttpStatus.NOT_FOUND);
		    
		} catch(Exception e) {
			
			String msg = this.buildFunctionMessage(
					project, 
					function, 
					"read function ", 
					e);					
			log.error(msg);
			return new ResponseEntity<>(
					new EntityResponse<>(msg),
					HttpStatus.INTERNAL_SERVER_ERROR);	
		}
	}
	
	
	//
	// READ ALL root-layer Function entities of project
	//
	@CrossOrigin(origins=CommonConfig.Web.allowedCorsClient)
	@RequestMapping(value=Url.READ.Function.Project.url, method=RequestMethod.GET)
	public ResponseEntity<EntityResponse<Function>> 
	
	readFunctionsOfProject(
			
			@PathVariable(value=Url.READ.Function.Project.pathVarProject) String projectCode ) {
		
		List<Function> functions;
		
		if(projectCode == null) {
			
			String msg = this.buildREADOfProjectMessage(projectCode, "cannot read project with null code");
			log.error(msg);
		    return new ResponseEntity<>(
					new EntityResponse<>(msg),
					HttpStatus.BAD_REQUEST);
						
		}
		
		if(projectCode.isEmpty()) {
			String msg = this.buildREADOfProjectMessage(projectCode, "cannot read project with empty code");
			log.error(msg);
		    return new ResponseEntity<>(
					new EntityResponse<>(msg),
					HttpStatus.BAD_REQUEST);
		}
		
		try {
					
			functions = this.service.readFunctions(projectCode);
			log.info("READ list of functions for project: {}", projectCode);	
			EntityResponse<Function> r = new EntityResponse<>(functions);
			return new ResponseEntity<>(r, HttpStatus.OK);
			
		} catch(NoSuchCodedItemException e) {
			
			String msg = this.buildREADOfProjectMessage(projectCode, e.getMessage());
			log.error(msg);
			return new ResponseEntity<>(
					new EntityResponse<>(msg),
					HttpStatus.NOT_FOUND);	
			
		} catch(Exception e) {
			
			String msg = this.buildREADOfProjectMessage(projectCode, e.getMessage());
			log.error(msg);
			return new ResponseEntity<>(
					new EntityResponse<>(msg),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
		
	
	/**
	 * UPDATE function resource
	 * 
	 * URI : 	api/v4/functions/{projectCode}/{functionCode}
	 * VERB: 	PUT
	 * 
	 * No Messages triggered. Note that only { projectCode, functionCode } together 
	 * are valid as unique identifiers across all of the FSCL domain.
	 *  
	 * @param 	project		String 		
	 * @param 	function	String 		full function code, no leading delimiters 
	 * @param 	body			EntityData, containing update data
	 * @return 	ResponseEntity for HTTP return 
	 */
	@CrossOrigin(origins=CommonConfig.Web.allowedCorsClient)
	@RequestMapping(value=Url.UPDATE.Function.url, method=RequestMethod.PUT)
	public ResponseEntity<EntityLifecycleResponse> 
	
	updateFunction(
			
		@PathVariable(value=Url.UPDATE.Function.pathVarProject) String project,
		@PathVariable(value=Url.UPDATE.Function.pathVarEntity) String function,		
		@RequestBody UpdateEntityContent body) 
	{
		EntityId id;
		
		try {
			
			id = new EntityId(project, function);
			
			
			Function f = this.service.updateFunction(id, body);
			if(f == null) {
				throw new NoSuchCodedItemException();
			}
			log.info("updated function: function={}", 
					f.toString());
			
			return new ResponseEntity<>(
					new EntityLifecycleResponse(f.getCode()), HttpStatus.OK);
			
			
		} catch(IllegalArgumentException e) {
			
			String msg = this.buildFunctionMessage(
				project, 
				function, 
				"update component ", 
				e);
			log.error(msg);
			return new ResponseEntity<>(
					new EntityLifecycleResponse(project, function, msg),
					HttpStatus.BAD_REQUEST);
			
		} catch(NoSuchCodedItemException e) {
			
			String msg = this.buildFunctionMessage(
				project, 
				function, 
				"update component ", 
				e);
			log.error(msg);
			return new ResponseEntity<>(
					new EntityLifecycleResponse(project, function, msg),
					HttpStatus.NOT_FOUND);	
			
			
		} catch(Exception e) {
			
			String msg = this.buildFunctionMessage(
				project, 
				function, 
				"update component ", 
				e);
			log.error(msg);
			return new ResponseEntity<>(
					new EntityLifecycleResponse(project, function, msg),
					HttpStatus.INTERNAL_SERVER_ERROR);			
		}
	}
	
	//
	// DELETE function resource
	//
	@CrossOrigin(origins=CommonConfig.Web.allowedCorsClient)
	@RequestMapping(value=Url.DELETE.Function.url, method=RequestMethod.DELETE)
	public ResponseEntity<EntityIdListResponse> 
	
	deleteFunction(
	
		@PathVariable(value=Url.DELETE.Function.pathVarProject) String project,
		@PathVariable(value=Url.DELETE.Function.pathVarEntity) String function) {
		
		EntityId id;
		
		try {
			
			id = new EntityId(project, function);
			List<EntityApiId> deletedFunctions =  this.service.deleteFunction(id);
			
			log.info("deleted function: {}:{}",
					project, function);
			return new ResponseEntity<>(
					new EntityIdListResponse(deletedFunctions),
					HttpStatus.OK);
			
		} catch(IllegalArgumentException e) {
						
			String msg = this.buildFunctionMessage(
				project, 
				function, 
				"delete component ", 
				e);
			log.error(msg);
			return new ResponseEntity<>(
					new EntityIdListResponse(msg),
					HttpStatus.BAD_REQUEST);
			
			
		} catch(NoSuchCodedItemException e) {
			
			String msg = this.buildFunctionMessage(
				project, 
				function, 
				"delete component ", 
				e);		
			log.error(msg);
			return new ResponseEntity<>(
					new EntityIdListResponse(msg),
					HttpStatus.NOT_FOUND);
			
		} catch(Exception e) {
			
			String msg = this.buildFunctionMessage(
				project, 
				function, 
				"delete component ", 
				e);
			log.error(msg);
			return new ResponseEntity<>(
					new EntityIdListResponse(msg),
					HttpStatus.INTERNAL_SERVER_ERROR);
		    
		}		
	}
	
	@RequestMapping(value="/api/v4/functions/ping", method=RequestMethod.GET)
	public ResponseEntity<String> getLifePing() {
		return new ResponseEntity<>("fscl.function-service:alive.",
				HttpStatus.OK);
	}
	
	protected String buildIdMessage(String projectCode, Exception e) {
		return "could not create new function Id for project : " + projectCode + ": " + e.getMessage();
	}
	
	protected String buildREADOfProjectMessage(String projectCode, String detail) {
		return "could not read functions of project {" + projectCode + "}: " + detail;
	}
	

}
	
