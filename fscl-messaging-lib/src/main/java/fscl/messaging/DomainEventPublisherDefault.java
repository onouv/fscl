package fscl.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.util.List;
import java.util.ListIterator;


public class DomainEventPublisherDefault<EventType> 
	implements DomainEventPublisher<EventType> {
	
	@Autowired
	private Source source;
	
	@Override
	public void publish(List<EventType> events) {
		
		ListIterator<EventType> iter = events.listIterator();
		while(iter.hasNext()) {
			EventType event = iter.next();
			Message<EventType> msg = MessageBuilder.withPayload(event).build();
			source.output().send(msg);
		}
	}
}
