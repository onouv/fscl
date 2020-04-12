package fscl.core.domain;


import de.bechte.junit.runners.context.HierarchicalContextRunner;

import org.junit.runner.RunWith;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

@RunWith(HierarchicalContextRunner.class)
public class EntityCodeTest {
	
	public class GivenCodeConfig {
		
		private final String prefix = "#";
		private final String separator = ".";
		private final CodeFormat config = new CodeFormat(prefix, separator);
		
		public class GivenNoPrefix  {

			private final String code = "100.100.001";			
			public class WhenValidated {
				
				private EntityCode entityCode = new EntityCode(code, config);
				
				@Test 
				public void shouldNotValidate() {
					
					assertFalse(entityCode.isValid(config));
					
				}
			}
		}
		

		public class GivenSinglePrefix  {

			private final String code = prefix + "100.100.001";
			
			public class WhenValidated {
				
				private EntityCode entityCode = new EntityCode(code, config);
				
				@Test 
				public void shouldValidate() {
					
					assertTrue(entityCode.isValid(config));					
				}
			}
			
			public class WhenQueriedForParent {
				
				private EntityCode entityCode = new EntityCode(code, config);
				
				@Test
				public void shouldReturnParentString() {
					
					String parent = prefix + "100.100";	
					String codeStr = entityCode.getParent().toString();
					assertEquals(codeStr, parent);
				}
				
			}
		}

		public class GivenAdditionalPrefix {
			
			private final String code = prefix + "100.100" + prefix + ".001";
			
			public class WhenValidated {
				
				private EntityCode entityCode = new EntityCode(code, config);
				
				@Test 
				public void shouldNotValidate() {
					
					assertFalse(entityCode.isValid(config));
					
				}
			}
		}
		
		public class GivenNoEdgeSeparators {
			
			private final String code = prefix + "100.100.001";
			
			public class WhenValidated {
				
				private EntityCode entityCode = new EntityCode(code, config);
				
				@Test 
				public void shouldValidate() {
					
					assertTrue(entityCode.isValid(config));
					
				}
			}
		}
		
		public class GivenEndSeparator {
			
			private final String code = prefix + "100.100.001.";
			
			public class WhenValidated {
				
				private EntityCode entityCode = new EntityCode(code, config);
				
				@Test 
				public void shouldNotValidate() {
					
					assertFalse(entityCode.isValid(config));
					
				}
			}
		}
		
		public class GivenStartSeparator {
			
			private final String code = prefix + ".100.100.001";
			
			public class WhenValidated {
				
				private EntityCode entityCode = new EntityCode(code, config);
				
				@Test 
				public void shouldNotValidate() {
					
					assertFalse(entityCode.isValid(config));
					
				}
			}
		}
		
		public class GivenSeparatorBothEdges {
			
			private final String code = prefix + ".100.100.001.";
			
			public class WhenValidated {
				
				private EntityCode entityCode = new EntityCode(code, config);
				
				@Test 
				public void shouldNotValidate() {
					
					assertFalse(entityCode.isValid(config));
					
				}
			}
		}
		
		public class GivenAdjacentSeparators {
			
			private final String code = prefix + "100..100.001";
			
			public class WhenValidated {
				
				private EntityCode entityCode = new EntityCode(code, config);
				
				@Test 
				public void shouldNotValidate() {
					
					assertFalse(entityCode.isValid(config));
					
				}
			}
		}
		
	}

}
