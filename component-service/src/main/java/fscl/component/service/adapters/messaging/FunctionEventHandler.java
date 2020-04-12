package fscl.component.service.adapters.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;

import fscl.function.api.foreignkeys.Function;
import fscl.function.api.messaging.FunctionCreatedEvent;
import fscl.function.api.messaging.FunctionDeletedEvent;
import fscl.function.api.messaging.FunctionRecodedEvent;
import fscl.function.api.messaging.ComponentLinkEvent;
import fscl.core.db.NoSuchCodedItemException;
import fscl.core.db.DuplicateEntityException;
import fscl.core.domain.EntityId;
import fscl.function.api.messaging.FunctionSubscriberChannels;
import fscl.component.service.ComponentServiceHelper;
import fscl.component.service.adapters.db.FunctionRepository;
import fscl.component.service.adapters.messaging.FunctionEventHandler;
import fscl.component.service.domain.ComponentLinkService;


@EnableBinding(FunctionSubscriberChannels.class)
public class FunctionEventHandler {

	//@Autowired
	//private ProjectRepository projectRepository;
	
	@Autowired
	private FunctionRepository functionRepository;
	
	@Autowired
	private ComponentLinkService service;
	
	@Autowired
	private ComponentServiceHelper helper;
	
	private static final Logger log = LoggerFactory.getLogger(
			FunctionEventHandler.class);
	
	@StreamListener(FunctionSubscriberChannels.CREATED)
	public void handleEvent(@Payload final FunctionCreatedEvent event) 
		throws IllegalArgumentException, NoSuchCodedItemException {
		
		log.info(
			"component service: processing event {}", 
			event.toString());
		
		EntityId code = new EntityId(event.getCode());		
		EntityId validatedCode = this.helper.validateFunctionId(code);
		
		Function preExisting = this.functionRepository.findByCode(validatedCode);
		if(preExisting == null) {
			Function newBie = new Function(validatedCode);
			this.functionRepository.save(newBie);
			log.info("component service : created function-ref={}", validatedCode);
			
		} else {
			log.info(
				"component service: ignoring pre-existing function-ref for code={}", 
				validatedCode);
		}	
	}
	
	@StreamListener(FunctionSubscriberChannels.DELETED)
	public void handleEvent(@Payload final FunctionDeletedEvent event) 
		throws IllegalArgumentException, NoSuchCodedItemException {
		
		log.info(
				"component service: processing event {}", 
				event.toString());
		
		EntityId code = new EntityId(event.getCode());		
		EntityId validatedCode = this.helper.validateFunctionId(code);
		
		try {
			EntityId unlinkedFunc = this.service.deleteFunction(validatedCode);
			if(unlinkedFunc != null) {
				log.info(
					"component service : unlinked function-ref={} from any components", 
					validatedCode);
			} else {
				log.info(
					"component service : no function-ref={} to unlink - do nothing", 
					validatedCode);
			}
			
			// delete foreign key			
			this.functionRepository.deleteById(validatedCode);
			
		} catch(Exception e) {
			log.error(
				"component service: could not delete function-ref with code={}. Message: {}", 
				validatedCode,
				e.getMessage());
		}
	}
	
	@StreamListener(FunctionSubscriberChannels.RECODED)
	public void handleEvent(@Payload final FunctionRecodedEvent event) 
		throws IllegalArgumentException, NoSuchCodedItemException {
		log.info(
				"function service: received event {}, but no implementation provided, yet.", 
				event.toString());
		
		EntityId newCode = new EntityId(event.getNewCode());		
		EntityId validatedNewCode = this.helper.validateFunctionId(newCode);
		EntityId oldCode = new EntityId(event.getNewCode());
		EntityId validatedOldCode = this.helper.validateFunctionId(oldCode);
	
	}
	
	/**
	 * We expect to receive a ComponentLinkEvent in case another service instance 
	 * has initially unlinked a Component from this function, usually due to 
	 * request from a client, and we are supposed to replicate this action. 
	 * 
	 * @param event
	 * @throws IllegalArgumentException
	 * @throws NoSuchCodedItemException
	 * @throws DuplicateEntityException
	 */
	@StreamListener(FunctionSubscriberChannels.COMPONENT_LINKING)
	public void handleEvent(@Payload final ComponentLinkEvent event) 
		throws 
			IllegalArgumentException, 
			NoSuchCodedItemException,
			DuplicateEntityException {
		log.info(
				"component service: processing event {}", 
				event.toString());
		
		EntityId componentId = new EntityId(event.getComponent());
		EntityId functionId = new EntityId(event.getFunction());
				
		ComponentLinkEvent.Type type = event.getType();
		try {
			switch(type) {
				case LINKED:
					this.service.linkFunctionToComponent(componentId, functionId);
				break;
				case UNLINKED:
					this.service.unlinkFunctionFromComponent(componentId, functionId);				
				break;
				default:
					throw new IllegalArgumentException("event.type not valid");
			}
		} catch (Exception ex) {
			log.error(
				"component-service: error while handling {}: {}",
				event.toString(),
				ex.getMessage());
		}
	}
}
