package fscl.component.service.adapters.web;

import org.springframework.web.bind.annotation.RequestMethod;

public class API {
	
	final static String base = "/api/v4/components";
	
	public static class Component {
		
		public static class Create { 
			public static final String project = "project";
			public static final String entity = "entity";
			
			// api/v4/components/{project}
			public static final String url = 
					base + 
					"/{" + project + "}" + 
					"/{" + entity + "}";
			public static final RequestMethod method = RequestMethod.POST;
		}
		
		public static class Update {
			
			public static final String project = "project";
			public static final String entity = "entity";
			
			// api/v4/components/{project}/{entity}
			public static final String url = 
					base + 
					"/{" + project + "}" + 
					"/{" + entity + "}";
			public static final RequestMethod method = RequestMethod.PUT;
		}
		
		public static class Delete {
			
			public static final String project = "project";
			public static final String entity = "entity";
			
			// api/v4/components/{project}/{entity}
			public static final String url = 
					base + 
					"/{" + project + "}" + 
					"/{" + entity + "}";
			public static final RequestMethod method = RequestMethod.DELETE;
			
		}
		
		public static class NewId {
			public static final String pathVar = "project";

			// api/v4/components/new/{project}
			public static final String url = base + "/new/{" + pathVar + "}";
			public static final RequestMethod method = RequestMethod.POST;
		}
		
		public static class Parent {
			public static class NewId {
				public static final String pathVar = "parent";
				
				// api/v4/components/new/{project}/{parent}
				public static final String url = base + "/new/{" + Component.NewId.pathVar + "}/{" + pathVar + "}";
				public static final RequestMethod method = RequestMethod.POST;
			}
		}
		
		public static class Project {
			public static class ReadAll {
				public static final String project = "project";
							
				// api/v4/components/{project}
				public static final String url = 
						base + 
						"/{" + project + "}";						
				public static final RequestMethod method = RequestMethod.GET;
			}
		}
		
		public static class Read {
			public static final String project = "project";
			public static final String entity = "entity";
			
			// api/v4/components/{project}/{entity}
			public static final String url = 
					base + 
					"/{" + project + "}" + 
					"/{" + entity + "}";
			public static final RequestMethod method = RequestMethod.GET;
		}
	}

}
