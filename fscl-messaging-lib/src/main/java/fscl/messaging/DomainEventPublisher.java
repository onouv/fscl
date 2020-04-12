package fscl.messaging;

import java.util.List;


public interface DomainEventPublisher<EventType> {

	public void publish(List<EventType> events);
}
