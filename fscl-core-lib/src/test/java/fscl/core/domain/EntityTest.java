package fscl.core.domain;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

import org.junit.runner.RunWith;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(HierarchicalContextRunner.class)
public class EntityTest {
	
	private class Dummy extends Entity<Dummy>{
		
		private String val;
		
		Dummy(String val) {
			super(null);		
			this.val = val; 
		}
		
		public boolean equals(Object o) {
			Dummy s = (Dummy) o;
			boolean res = this.val.equals(s.val); 
			return res;
		}
	}	

	public class GivenIdenticalLayeredEntities {
		
		private Dummy sut1 = new Dummy("one"); 
		private Dummy sut2 = new Dummy("one");
		
		public class WhenCompared {
			
			@Test
			public void shouldCompareEqual() {
				assertTrue(sut1.equals(sut2));
			}
		}
	}
	
	public class GivenDifferentLayeredEntities {
		
		private Dummy sut1 = new Dummy("one"); 
		private Dummy sut2 = new Dummy("two");
		
		public class WhenCompared {
			@Test
			public void shouldCompareUnequal() {
				assertFalse(sut1.equals(sut2));
			}
		}
	}
}
