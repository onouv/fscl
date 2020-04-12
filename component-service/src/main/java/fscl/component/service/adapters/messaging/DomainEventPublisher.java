package fscl.component.service.adapters.messaging;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;

import fscl.component.api.messaging.*;

@Service
@EnableBinding(ComponentPublisherChannels.class)
public class DomainEventPublisher {
	
	private static final Logger logger = 
			LoggerFactory.getLogger(DomainEventPublisher.class);
	
	
	@Autowired
	ComponentPublisherChannels channels;
	
	public void publish(final ComponentCreatedEvent e) {
		logger.info("component-service: publishing {}", e.toString());
		
		MessageChannel channel = this.channels.publishCreated();
		channel.send(MessageBuilder
			.withPayload(e)
			.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
			.build());
	}
	
	public void publish(final ComponentDeletedEvent e) {
		logger.info("component-service: publishing {}", e.toString());
		
		MessageChannel channel = this.channels.publishDeleted();
		channel.send(MessageBuilder
			.withPayload(e)
			.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
			.build());
	}
	
	public void publish(final List<ComponentDeletedEvent> events) {
		events.forEach(event -> {
			this.publish(event);
		});
	}
	
	public void publish(final ComponentRecodedEvent e) {
		logger.info("component-service: publishing {}", e.toString());
		
		MessageChannel channel = this.channels.publishRecoded();
		channel.send(MessageBuilder
			.withPayload(e)
			.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
			.build());
	}
	
	public void publish(final FunctionLinkEvent e) {
		logger.info("component-service: publishing {}", e.toString());
		
		MessageChannel channel = this.channels.publishLinked();
		channel.send(MessageBuilder
			.withPayload(e)
			.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
			.build());
	}
}
