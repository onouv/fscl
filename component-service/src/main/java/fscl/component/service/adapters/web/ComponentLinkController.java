package fscl.component.service.adapters.web;

import java.util.Iterator;
import java.util.List;

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

import fscl.component.service.domain.ComponentLinkService;
import fscl.core.api.EntityIdListRequest;
import fscl.core.api.EntityIdListResponse;
import fscl.core.api.EntityLifecycleResponse;
import fscl.core.auxil.CommonConfig;
import fscl.core.db.DuplicateEntityException;
import fscl.core.db.NoSuchCodedItemException;
import fscl.core.domain.EntityId;


@RestController
public class ComponentLinkController extends ComponentControllerBase {
	
	private static final Logger log=LoggerFactory
			.getLogger(ComponentLinkController.class);

	@Autowired
	private ComponentLinkService service;
	
	/**
	 * GET all components linked to specified function
	 */
	@CrossOrigin(origins=CommonConfig.Web.allowedCorsClient)
	@RequestMapping(
			value= {
				"/api/v4/components/functions/{project}/{entity}"				
			},
			method=RequestMethod.GET)
	public ResponseEntity<EntityIdListResponse> 
	readFunctionsOfComponent(
			@PathVariable("project") String project,
			@PathVariable("entity") String component) {
		
		EntityId id;
		List<EntityId> ids = null;		
		
		try {
			
			id = new EntityId(project, component);
			ids = this.service.readLinkedFunctionsOfComponent(id);
			return new ResponseEntity<EntityIdListResponse>(
					EntityIdListResponse.createFrom(ids),
					HttpStatus.OK);		
			
			
		} catch(IllegalArgumentException e) {
			String msg = this.buildComponentMessage(project, component, "read linked functions of", e);
			log.error(msg);
			return new ResponseEntity<EntityIdListResponse>(
					new EntityIdListResponse(msg),
					HttpStatus.BAD_REQUEST);
			
		} catch(NoSuchCodedItemException e) {
			String msg = this.buildComponentMessage(project, component, "read linked functions of", e);
			log.error(msg);
			return new ResponseEntity<EntityIdListResponse>(
		    		new EntityIdListResponse(msg),
		    		HttpStatus.NOT_FOUND);
		}
	}
	
	@CrossOrigin(origins=CommonConfig.Web.allowedCorsClient)
	@RequestMapping(
			value= {
				"/api/v4/components/link/functions/{project}/{entity}"				
			},
			method=RequestMethod.POST)
	public ResponseEntity<EntityLifecycleResponse>
	
	linkFunctionsToComponent(
			@PathVariable("project") String project,
			@PathVariable("entity") String entity,
			@RequestBody EntityIdListRequest body) {
		
		EntityId targetComponentId = new EntityId( project, entity);
		
		try {
			
			List<EntityId> functionIds = this.translateCodes(body.getEntities());
			Iterator<EntityId> iter = functionIds.iterator();
			
			EntityId successComponentId;
			while(iter.hasNext()) {
				
				EntityId functionId = iter.next();
				successComponentId = this.service.linkFunctionToComponentAndPublish(
					targetComponentId, functionId);
				
				if( ! successComponentId.equals(targetComponentId)) {
					throw new Exception(
						"unexpected function code returned within server");
				}
				
				log.info(
					"linked function " +
					functionId.toString() + 
					" to component " +
					successComponentId.toString() );				
			}
			
			return new ResponseEntity<EntityLifecycleResponse>(
				new EntityLifecycleResponse(targetComponentId), 
				HttpStatus.OK);

			
		} catch(IllegalArgumentException e) {
			String msg = this.buildComponentMessage(project, entity, "link component to", e);
			log.error(msg);
			return new ResponseEntity<EntityLifecycleResponse>(
					new EntityLifecycleResponse(
						new EntityId(project, "void"),
						msg), 
					HttpStatus.BAD_REQUEST);
			
		} catch(NoSuchCodedItemException e) {
			String msg = this.buildComponentMessage(project, entity, "link component to", e);
			log.error(msg);
			return new ResponseEntity<EntityLifecycleResponse>(
		    		new EntityLifecycleResponse(
	    				new EntityId(project, "void"),
						msg),
		    		HttpStatus.NOT_FOUND);
			
		} catch(DuplicateEntityException e) {
			String msg = this.buildComponentMessage(project, entity, "link component to", e);
			log.error(msg);
			return new ResponseEntity<EntityLifecycleResponse>(
		    		new EntityLifecycleResponse(
	    				new EntityId(project, "void"),
						msg),
		    		HttpStatus.CONFLICT);
		} 
		catch(Exception e) {
			String msg = this.buildComponentMessage(project, entity, "link component to", e);
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
				"/api/v4/components/unlink/functions/{project}/{entity}"				
			},
			method=RequestMethod.POST)
	public ResponseEntity<EntityLifecycleResponse>
	
	unlinkFunctionsFromComponent(
			@PathVariable("project") String project,
			@PathVariable("entity") String entity,
			@RequestBody EntityIdListRequest body) {
		
		EntityId componentId = new EntityId( project, entity);
		
		try {
			
			List<EntityId> functionIds = this.translateCodes(body.getEntities());			
			Iterator<EntityId> iter = functionIds.iterator();
			
			EntityId successFunctionId;
			while(iter.hasNext()) {
				EntityId functionId = iter.next();
				successFunctionId = this.service.unlinkFunctionFromComponentAndPublish(
						functionId,
						componentId);
				
				if( ! successFunctionId.equals(componentId)) {
					throw new Exception(
						"unexpected function code returned within server");
				}
				
				log.info(
					"unlinked function " +
					functionId.toString() + 
					" from component " +
					componentId.toString() );
			}			
							
			return new ResponseEntity<EntityLifecycleResponse>(
				new EntityLifecycleResponse(componentId), 
				HttpStatus.OK);
			
		} catch(IllegalArgumentException e) {
			String msg = this.buildComponentMessage(project, entity, "unlink function from", e);
			log.error(msg);
			return new ResponseEntity<EntityLifecycleResponse>(
					new EntityLifecycleResponse(
						new EntityId(project, "void"),
						msg), 
					HttpStatus.BAD_REQUEST);
			
		} catch(NoSuchCodedItemException e) {
			String msg = this.buildComponentMessage(project, entity, "unlink function from", e);
			log.error(msg);
			return new ResponseEntity<EntityLifecycleResponse>(
		    		new EntityLifecycleResponse(
	    				new EntityId(project, "void"),
						msg),
		    		HttpStatus.NOT_FOUND);
			
		} catch(DuplicateEntityException e) {
			String msg = this.buildComponentMessage(project, entity, "unlink function from", e);
			log.error(msg);
			return new ResponseEntity<EntityLifecycleResponse>(
		    		new EntityLifecycleResponse(
	    				new EntityId(project, "void"),
						msg),
		    		HttpStatus.CONFLICT);
		} 
		catch(Exception e) {
			String msg = this.buildComponentMessage(project, entity, "unlink function from", e);
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
