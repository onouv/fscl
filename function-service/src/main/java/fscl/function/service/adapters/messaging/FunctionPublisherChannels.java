package fscl.function.service.adapters.messaging;


import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;


public interface FunctionPublisherChannels {

	public final String CREATED = "PublishFunctionCreated";
	public final String DELETED = "PublishFunctionDeleted";
	public final String RECODED = "PublishFunctionRecoded";
	public final String COMPONENT_LINK = "PublishFunctionComponentLinking";
	
	@Output(CREATED)
	MessageChannel publishCreated();
	
	@Output(DELETED)
	MessageChannel publishDeleted();
	
	@Output(RECODED)
	MessageChannel publishRecoded();
	
	@Output(COMPONENT_LINK )
	MessageChannel publishLinked();
	
}
