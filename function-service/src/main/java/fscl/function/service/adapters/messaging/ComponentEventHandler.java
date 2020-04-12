package fscl.function.service.adapters.messaging;

import fscl.core.domain.EntityId;
import fscl.core.db.NoSuchCodedItemException;
import fscl.component.api.foreignkeys.Component;

import fscl.component.api.messaging.ComponentCreatedEvent;
import fscl.component.api.messaging.ComponentDeletedEvent;
import fscl.component.api.messaging.ComponentRecodedEvent;
import fscl.component.api.messaging.FunctionLinkEvent;
import fscl.component.api.messaging.ComponentSubscriberChannels;
import fscl.function.service.adapters.db.ComponentRepository;
import fscl.function.service.domain.FunctionLinkService;
import fscl.function.service.domain.FunctionServiceHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;


@EnableBinding(ComponentSubscriberChannels.class)
@Service
public class ComponentEventHandler {
	
	@Autowired
	private ComponentRepository componentRepository;
	
	@Autowired
	private FunctionLinkService service;
	
	@Autowired
	private FunctionServiceHelper helper;
	
	private static final Logger logger = LoggerFactory.getLogger(
			ComponentEventHandler.class);

	
	@StreamListener(ComponentSubscriberChannels.CREATED)
	public void handleEvent(@Payload ComponentCreatedEvent event) 
		throws IllegalArgumentException, NoSuchCodedItemException, Exception {
		
		logger.info(
			"function service: processing event {}", 
			event.toString());
		
		EntityId code = new EntityId(event.getCode());		
		EntityId validatedCode = this.helper.validateComponentId(code);
		
		Component preExisting = this.componentRepository.findByCode(validatedCode);
		if(preExisting == null) {
			Component newBie = new Component(validatedCode);
			this.componentRepository.save(newBie);
			logger.info("function service : created component-ref={}", validatedCode);
			
		} else {
			logger.info(
				"function service: ignoring pre-existing component-ref for code={}", 
				validatedCode);
		}
	}
	
	@StreamListener(ComponentSubscriberChannels.DELETED)
	public void handleEvent(@Payload ComponentDeletedEvent event) 
		throws IllegalArgumentException, NoSuchCodedItemException, Exception {
				
		logger.info(
			"function service: processing event {}", 
			event.toString());
		
		EntityId code = new EntityId(event.getCode());		
		EntityId validatedCode = this.helper.validateComponentId(code);
		
		try {
			EntityId unlinkedComp = this.service.deleteComponent(validatedCode);
			
			if(unlinkedComp != null) {
				logger.info(
					"function service : unlinked all component-ref={} from any functions", 
					validatedCode);
			} else {
				logger.info(
					"function service : no component-ref={} to unlink - do nothing", 
					validatedCode);
			}
			
			// delete foreign key
			this.componentRepository.deleteById(validatedCode);
			
		} catch(Exception e) {
			logger.error(
				"function service: could not delete component-ref with code={}. Message: {}", 
				validatedCode,
				e.getMessage());
		}
	}
	
	@StreamListener(ComponentSubscriberChannels.RECODED)
	public void handleEvent(@Payload ComponentRecodedEvent event) 
		throws IllegalArgumentException, NoSuchCodedItemException, Exception {
		
		logger.info(
				"function service: received event {}, but no implementation provided, yet.", 
				event.toString());
	}
	
	/**
	 * We expect to receive a FunctionLinkEvent in case another service instance 
	 * has initially unlinked a function from this component, usually due to 
	 * request from a client, and we are supposed to replicate this action. 
	 * 
	 */
	@StreamListener(ComponentSubscriberChannels.FUNCTION_LINK)
	public void handleEvent(@Payload FunctionLinkEvent event) 
		throws 
			IllegalArgumentException, 
			NoSuchCodedItemException, 
			Exception {
		
		logger.info(
				"function service: received event {}.", 
				event.toString());
		
		EntityId componentId = new EntityId(event.getComponent());
		EntityId functionId = new EntityId(event.getFunction());
				
		FunctionLinkEvent.Type type = event.getType();
		try {
			switch(type) {
				case LINKED:
					this.service.linkComponentToFunction(functionId, componentId);
				break;
				case UNLINKED:
					this.service.unlinkComponentFromFunction(functionId, componentId);				
				break;
				default:
					throw new IllegalArgumentException("event.type not valid");
			}		
		} catch (Exception ex) {
			logger.error(
				"function-service: error while handling {}: {}",
				event.toString(),
				ex.getMessage());
		}
	}
}
