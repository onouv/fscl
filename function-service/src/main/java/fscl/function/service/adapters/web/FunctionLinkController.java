package fscl.function.service.adapters.web;


import fscl.core.domain.EntityId;
import fscl.core.auxil.CommonConfig;
import fscl.function.service.domain.FunctionLinkService;
import fscl.core.api.EntityIdListResponse;
import fscl.core.api.EntityLifecycleResponse;
import fscl.core.api.EntityIdListRequest;
import fscl.core.db.NoSuchCodedItemException;
import fscl.core.db.DuplicateEntityException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Iterator;

@RestController
public class FunctionLinkController extends FunctionControllerBase {
	
private static final Logger log=LoggerFactory.getLogger(FunctionController.class);
	
	@Autowired	
	private FunctionLinkService service;
	
	/**
	 * GET all components linked to specified function
	 */
	@CrossOrigin(origins=CommonConfig.Web.allowedCorsClient)
	@RequestMapping(
			value= {
				"/api/v4/functions/components/{project}/{entity}"				
			},
			method=RequestMethod.GET)
	public ResponseEntity<EntityIdListResponse> readComponentsOfFunction(
			@PathVariable("project") String project,
			@PathVariable("entity") String function) {
		
		EntityId id;
		List<EntityId> ids = null;		
		
		try {
			
			id = new EntityId(project, function);
			ids = this.service.readLinkedComponentsOfFunction(id);
			return new ResponseEntity<EntityIdListResponse>(
					EntityIdListResponse.createFrom(ids),						
					HttpStatus.OK);		
			
			
		} catch(IllegalArgumentException e) {
			String msg = this.buildFunctionMessage(project, function, "read linked components of", e);
			log.error(msg);
			return new ResponseEntity<EntityIdListResponse>(
					new EntityIdListResponse(msg),						
					HttpStatus.BAD_REQUEST);
			
		} catch(NoSuchCodedItemException e) {
			String msg = this.buildFunctionMessage(project, function, "read linked components of", e);
			log.error(msg);
			return new ResponseEntity<EntityIdListResponse>(
		    		new EntityIdListResponse(msg),
		    		HttpStatus.NOT_FOUND);
		}
	}
	
	
	/**
	 * 
	 * Link all components listed in body to function {project:entity}
	 */
	@CrossOrigin(origins=CommonConfig.Web.allowedCorsClient)
	@RequestMapping(
			value= {
				"/api/v4/functions/link/components/{project}/{entity}"				
			},
			method=RequestMethod.POST)
	public ResponseEntity<EntityLifecycleResponse>
	
	linkComponentstoFunction(
			@PathVariable("project") String project,
			@PathVariable("entity") String entity,
			@RequestBody EntityIdListRequest body) {
		
		
		EntityId functionId = new EntityId( project, entity);
					
		try {		
			
			List<EntityId> componentIds = this.translateCodes(body.getEntities());			
			Iterator<EntityId> iter = componentIds.iterator();
			
			EntityId successFunctionId;
			while(iter.hasNext()) {
				EntityId componentId = iter.next();
				successFunctionId = this.service.linkComponentToFunctionAndPublish(
						functionId, 
						componentId);
				
				if( ! successFunctionId.equals(functionId)) {
					throw new Exception(
						"unexpected function code returned within server");
				}
				
				log.info(
					"linked component " +
					componentId.toString() + 
					" to function " +
					functionId.toString() );
			}
			
							
			return new ResponseEntity<EntityLifecycleResponse>(
				new EntityLifecycleResponse(functionId), 
				HttpStatus.OK);
			
		} catch(IllegalArgumentException e) {
			String msg = this.buildFunctionMessage(project, entity, "link component to", e);
			log.error(msg);
			return new ResponseEntity<EntityLifecycleResponse>(
					new EntityLifecycleResponse(
						new EntityId(project, "void"),
						msg), 
					HttpStatus.BAD_REQUEST);
			
		} catch(NoSuchCodedItemException e) {
			String msg = this.buildFunctionMessage(project, entity, "link component to", e);
			log.error(msg);
			return new ResponseEntity<EntityLifecycleResponse>(
		    		new EntityLifecycleResponse(
	    				new EntityId(project, "void"),
						msg),
		    		HttpStatus.NOT_FOUND);
			
		} catch(DuplicateEntityException e) {
			String msg = this.buildFunctionMessage(project, entity, "link component to", e);
			log.error(msg);
			return new ResponseEntity<EntityLifecycleResponse>(
		    		new EntityLifecycleResponse(
	    				new EntityId(project, "void"),
						msg),
		    		HttpStatus.CONFLICT);
		} 
		catch(Exception e) {
			String msg = this.buildFunctionMessage(project, entity, "link component to", e);
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
	 * 
	 * Unlink all components listed in body from component {project:entity}
	 * 
	 */
	@CrossOrigin(origins=CommonConfig.Web.allowedCorsClient)
	@RequestMapping(
			value= {
				"/api/v4/functions/unlink/components/{project}/{entity}"				
			},
			method=RequestMethod.POST)
	public ResponseEntity<EntityLifecycleResponse>
	
	unlinkComponentsFromFunction(
			@PathVariable("project") String project,
			@PathVariable("entity") String entity,
			@RequestBody EntityIdListRequest body) {
		
		EntityId functionId = new EntityId( project, entity);
		
		try {
			
			List<EntityId> componentIds = this.translateCodes(body.getEntities());			
			Iterator<EntityId> iter = componentIds.iterator();
			
			EntityId successFunctionId;
			while(iter.hasNext()) {
				EntityId componentId = iter.next();
				successFunctionId = this.service.unlinkComponentFromFunctionAndPublish(
						functionId, 
						componentId);
				
				if( ! successFunctionId.equals(functionId)) {
					throw new Exception(
						"unexpected function code returned within server");
				}
				
				log.info(
					"unlinked component " +
					componentId.toString() + 
					" from function " +
					functionId.toString() );
			}			
							
			return new ResponseEntity<EntityLifecycleResponse>(
				new EntityLifecycleResponse(functionId), 
				HttpStatus.OK);
			
		} catch(IllegalArgumentException e) {
			String msg = this.buildFunctionMessage(project, entity, "unlink component from", e);
			log.error(msg);
			return new ResponseEntity<EntityLifecycleResponse>(
					new EntityLifecycleResponse(
						project, 
	    				entity,
						msg),
					HttpStatus.BAD_REQUEST);
			
		} catch(NoSuchCodedItemException e) {
			String msg = this.buildFunctionMessage(project, entity, "unlink component from", e);
			log.error(msg);
			return new ResponseEntity<EntityLifecycleResponse>(
		    		new EntityLifecycleResponse(
	    				project, 
	    				entity,
						msg),
		    		HttpStatus.NOT_FOUND);
			
		} catch(DuplicateEntityException e) {
			String msg = this.buildFunctionMessage(project, entity, "unlink component from", e);
			log.error(msg);
			return new ResponseEntity<EntityLifecycleResponse>(
		    		new EntityLifecycleResponse(
	    				project, 
	    				entity,
						msg),
		    		HttpStatus.CONFLICT);
		} 
		catch(Exception e) {
			String msg = this.buildFunctionMessage(project, entity, "unlink component from", e);
			log.error(msg);
			return new ResponseEntity<EntityLifecycleResponse>(
					new EntityLifecycleResponse(
						project, 
						entity, 
						msg), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
		
	
}	

