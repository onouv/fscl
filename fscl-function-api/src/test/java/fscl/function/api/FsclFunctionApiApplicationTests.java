package fscl.function.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import fscl.core.domain.EntityId;
import fscl.function.api.messaging.FunctionEvent;


@SpringBootTest(classes=FunctionEvent.class)
class FsclFunctionApiApplicationTests {

	class GivenFunctionCode {
			
		private String projectCode = "Sackzement!";	
		private String functionCode = "Hallelujah, Praise the Lard!";
			
		class GivenCreationEvent {
				
			private FunctionEvent event = FunctionEvent.created(
				new EntityId(projectCode, functionCode));
			
			class WhenProbedforMember {
				
				@Test
				public void shouldReturnFunctionCode() {
					assertThat(event.getCode().entity.equals(functionCode));					
				}	
				
				@Test
				public void shouldReturnProjectCode() {
					assertThat(event.getCode().project.equals(projectCode));
				}
				
				@Test
				public void shouldReturnType() {
					assertThat(event.getType().equals(FunctionEvent.Type.CREATED));
				}
			}
		}
		
		class GivenDeletionEvent {
		
			
			private FunctionEvent event = FunctionEvent.deleted(
					projectCode, 
					functionCode);
			
			class WhenProbedforMember {
				
				@Test
				public void shouldReturnFunctionCode() {
					assertThat(event.getCode().entity.equals(functionCode));
				}				
				
				@Test
				public void shouldReturnProjectCode() {
					assertThat(event.getCode().project.equals(projectCode));
				}
				
				@Test
				public void shouldReturnType() {
					assertThat(event.getType().equals(FunctionEvent.Type.DELETED));
				}
			}
		}
	}

}
