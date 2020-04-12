package fscl.function.service.api;

public class Url {
	
	/*
	 * 
	 * /api/v4/functions/
	 *  
	 */
	
	protected static final String base = "/api/v4";	
	protected static final String functions = base + "/functions";
	
	
	protected class Base {
		public static final String pathVarProject = "project";
		public static final String pathVarEntity = "entity";			
		public static final String url = functions + "/{" 
				+ Base.pathVarProject + "}/{" 
				+ Base.pathVarEntity + "}"; 
				
	}
	
	
	public class CREATE {
		
		public class Function extends Url.Base {
			
			public class NewIdRegistration extends Url.Base {
				protected static final String newIdRegistration = "new";
				public static final String url = functions + newIdRegistration + "/{" + pathVarProject + "}";				
			}
		}		
		
	}	
	
	public class READ {
		public class Function extends Url.Base {			
			public class Project extends Url.Base {
				public static final String url = functions +  "/{" + pathVarProject + "}";				
			}			
		}
	}
	
	public class UPDATE {
		public class Function extends Url.Base {			
		}
	}
	
	public class DELETE {
		public class Function extends Url.Base {			
		}
	}
	

}
