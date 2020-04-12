package fscl.component.service.adapters.messaging;


import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;


public interface ComponentPublisherChannels {

	public final String CREATED = "PublishComponentCreated";
	public final String DELETED = "PublishComponentDeleted";
	public final String RECODED = "PublishComponentRecoded";
	public final String FUNCTION_LINK = "PublishComponentFunctionLinking";
	
	@Output(CREATED)
	MessageChannel publishCreated();
	
	@Output(DELETED)
	MessageChannel publishDeleted();
	
	@Output(RECODED)
	MessageChannel publishRecoded();
	
	@Output(FUNCTION_LINK)
	MessageChannel publishLinked();
	
}
