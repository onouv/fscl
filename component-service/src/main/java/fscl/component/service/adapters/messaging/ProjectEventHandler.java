package fscl.component.service.adapters.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;

import fscl.core.db.NoSuchCodedItemException;
import fscl.component.service.adapters.db.ProjectRepository;
import fscl.component.service.domain.ComponentService;
import fscl.project.api.events.ProjectEvent;
import fscl.project.foreignkeys.Project;

@EnableBinding(Sink.class)
@Service
public class ProjectEventHandler {
	
	@Autowired
	private ProjectRepository repository;
	
	@Autowired
	private ComponentService service;
	
	private static final Logger logger = LoggerFactory.getLogger(ProjectEventHandler.class);
	
	@StreamListener(Sink.INPUT)
	public void handleEvent(ProjectEvent event) 
		throws IllegalArgumentException, Exception {
		
		String code = event.getProjectCode();
		ProjectEvent.Type type = event.getType();
				
		logger.info(
			"component service: processing event project:type={}:{}", 
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
							"component service: created project-ref for code={}", 
							code);
				}
				else {
					logger.info(
						"component service: leaving intact pre-existing project-ref for code={}", 
						code);
				}			
				break;
				
			case DELETED:				
				try {
					this.service.deleteProject(code);
					logger.info("component service : deleted project-ref={}", code);
				} 
				catch(NoSuchCodedItemException e) {
				logger.info("component service: {}",	e.getMessage());
				}
				catch(Exception e){
					logger.error(
						"component service: could not delete project-ref with code={}. Message: {}", 
						code,
						e.getMessage());
				}
				break;
		}
	}
}
