package fscl.function.api.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface FunctionSubscriberChannels {
	
	public final String CREATED = "SubscribeFunctionCreated";
	public final String DELETED = "SubscribeFunctionDeleted";
	public final String RECODED = "SubscribeFunctionRecoded";
	public final String COMPONENT_LINKING = "SubscribeFunctionComponentLinking";
	    
	@Input(CREATED)
	SubscribableChannel subscribeCreated();
	
	@Input(DELETED)
	SubscribableChannel subscribeCeleted();
	
	@Input(RECODED)
	SubscribableChannel subscribeRecoded();
	
	@Input(COMPONENT_LINKING)
	SubscribableChannel subscribeLinking();
		
}
