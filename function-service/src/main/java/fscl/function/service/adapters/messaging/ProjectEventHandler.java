package fscl.function.service.adapters.messaging;


import fscl.core.db.NoSuchCodedItemException;
import fscl.function.service.adapters.db.ProjectRepository;
import fscl.function.service.domain.FunctionService;
import fscl.project.api.events.ProjectEvent;
import fscl.project.foreignkeys.Project;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@EnableBinding(Sink.class)
@Service
public class ProjectEventHandler {
	
	@Autowired
	private ProjectRepository repository;
	
	@Autowired
	private FunctionService service;
	
	private static final Logger logger = LoggerFactory.getLogger(ProjectEventHandler.class);
	
	@StreamListener(Sink.INPUT)
	public void handleEvent(ProjectEvent event) 
		throws IllegalArgumentException, Exception {
		
		String code = event.getProjectCode();
		ProjectEvent.Type type = event.getType();
				
		logger.info(
			"function service: processing event project:type={}:{}", 
			code, type);
		
		if(code.isEmpty())
			throw new IllegalArgumentException("invalid projectCode argument!");
		
		if(type == null)
			throw new IllegalArgumentException("type argument found null!");
				
		Project projectFound = this.repository.findByCode(code);
		
		switch(type) {
			case CREATED:
				if(projectFound == null) {
					Project newbie = new Project(
							code, 
							event.functionConfig,
							event.systemConfig,
							event.locationConfig,
							event.componentConfig);
					this.repository.save(newbie);
					logger.info(
							"function service: created project-ref for code={}", 
							code);
				}
				else {
					logger.info(
						"function service: leaving intact pre-existing project-ref for code={}", 
						code);
				}			
				break;
				
			case DELETED:				
				try {
					this.service.deleteProject(code);
					logger.info("function service : deleted project-ref={}", code);
				} 
				catch(NoSuchCodedItemException e) {
					logger.info("function service: {}",	e.getMessage());
				}
				catch(Exception e){
					logger.error(
						"function service: could not delete project-ref with code={}. Message: {}", 
						code,
						e.getMessage());
				}
				break;
			default: 
				throw new IllegalArgumentException("function service: invalid component event type.");
		}
	}
}
