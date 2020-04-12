package fscl.project.api.events;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
//import static org.assertj.core.api.Assertions.assertThat;

import fscl.project.api.events.ProjectEvent;

@SpringBootTest(classes=ProjectEvent.class)
class FsclProjectApiApplicationTests {

	class GivenProjectCode {
		
		//private String projectCode = "Hallelujah, Praise the Lard!";
				
		class GivenEventCreated {
			/*
			private ProjectEvent event = new ProjectEvent(
					ProjectEvent.Type.CREATED, 
					projectCode);
			*/
			class WhenProbedforMember {
				
				@Test
				public void shouldReturnProjectCode() {
					/*
					assertThat(event.getProjectCode().equals(projectCode));
					assertThat(event.getType().equals(ProjectEvent.Type.CREATED));
					*/
				}				
			}
		}
		
		class GivenEventDeleted{
			/*
			private ProjectEvent event = new ProjectEvent(
					ProjectEvent.Type.DELETED, 
					projectCode);
			*/
			class WhenProbedforMember {
				
				@Test
				public void shouldReturnProjectCode() {
				/*	assertThat(event.getProjectCode().equals(projectCode));
					assertThat(event.getType().equals(ProjectEvent.Type.DELETED));
					*/
				}				
			}
		}
	}
}
