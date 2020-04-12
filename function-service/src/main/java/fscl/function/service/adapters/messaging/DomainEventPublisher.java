package fscl.function.service.adapters.messaging;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;

import fscl.function.api.messaging.*;

import java.util.List;

@Service
@EnableBinding(FunctionPublisherChannels.class)
public class DomainEventPublisher {
	
	private static final Logger logger = 
		LoggerFactory.getLogger(DomainEventPublisher.class);
	
	
	@Autowired
	FunctionPublisherChannels channels;
	
	public void publish(final FunctionCreatedEvent e) {
		logger.info("function-service: publishing {}", e.toString());
		
		MessageChannel channel = this.channels.publishCreated();
		channel.send(MessageBuilder
			.withPayload(e)
			.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
			.build());
	}
	
	public void publish(final FunctionDeletedEvent e) {
		logger.info("function-service: publishing {}", e.toString());
		
		MessageChannel channel = this.channels.publishDeleted();
		channel.send(MessageBuilder
			.withPayload(e)
			.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
			.build());
	}
	
	public void publish(final List<FunctionDeletedEvent> events) {
		events.forEach(event -> {
			this.publish(event);
		});
	}
	
	public void publish(final FunctionRecodedEvent e) {
		logger.info("function-service: publishing {}", e.toString());
		
		MessageChannel channel = this.channels.publishRecoded();
		channel.send(MessageBuilder
			.withPayload(e)
			.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
			.build());
	}
	
	public void publish(final ComponentLinkEvent e) {
		logger.info("function-service: publishing {}", e.toString());
		
		MessageChannel channel = this.channels.publishLinked();
		channel.send(MessageBuilder
			.withPayload(e)
			.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
			.build());
	}
}
