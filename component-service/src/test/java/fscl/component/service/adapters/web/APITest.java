package fscl.component.service.adapters.web;

import org.springframework.web.bind.annotation.RequestMethod;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;



//@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class APITest {

	@Nested
	public class GivenProjectAndMethod {
		
		private final String Url = "/api/v4/components/{P1}";
		private RequestMethod method = RequestMethod.POST; 
		final String project = "P1";
		
		
		@Nested
		public class WhenAPIUsed {
								
			@Test
			public void thenShouldContainPOSTMethod() {
				assertEquals(method, API.Component.NewId.method);
			}
			
			@Test 
			public void thenShouldContainUrl() {
				assertEquals(Url, API.Component.NewId.url );
			}
			
		}
	}
}
