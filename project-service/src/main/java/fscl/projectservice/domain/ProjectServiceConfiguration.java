package fscl.projectservice.domain;

import fscl.messaging.DomainEventPublisher;
import fscl.messaging.DomainEventPublisherDefault;
import fscl.project.api.events.ProjectEvent;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectServiceConfiguration {
		
	@Bean
	public DomainEventPublisher<ProjectEvent> domainEventPublisher() {
		return new DomainEventPublisherDefault<ProjectEvent>();
	}

}
