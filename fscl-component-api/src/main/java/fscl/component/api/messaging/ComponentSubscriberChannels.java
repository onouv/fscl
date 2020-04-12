package fscl.component.api.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ComponentSubscriberChannels {
	
	public final String CREATED = "SubscribeComponentCreated";
	public final String DELETED = "SubscribeComponentDeleted";
	public final String RECODED = "SubscribeComponentRecoded";
	public final String FUNCTION_LINK = "SubscribeComponentFunctionLinking";
	    
	@Input(CREATED)
	SubscribableChannel subscribeCreated();
	
	@Input(DELETED)
	SubscribableChannel subscribeCeleted();
	
	@Input(RECODED)
	SubscribableChannel subscribeRecoded();
	
	@Input(FUNCTION_LINK)
	SubscribableChannel subscribeLinking();
		
}
