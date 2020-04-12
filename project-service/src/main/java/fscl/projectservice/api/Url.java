package fscl.projectservice.api;

public class Url {
	
	protected static final String base = "/api/v4";
	protected static final String projects = base + "/projects";
	
	public class CREATE {
		public class ProjectCode {
			public static final String url = Url.projects + "/newidrequest";
		}
		public class Project {
			public static final String url = Url.projects + "/new";
		}		
	}	
	
	public class READ {
		public class Project {
			public static final String url = Url.projects;
		}
	}
	
	public class UPDATE {
		public class Project {
			public static final String url = Url.projects + "/{projectCode}";
			public static final String pathVariable = "projectCode";
		}
	}
	
	public class DELETE {
		public class Project {
			public static final String url = Url.projects + "/{projectCode}";
			public static final String pathVariable = "projectCode";
		}
	}
	

}
