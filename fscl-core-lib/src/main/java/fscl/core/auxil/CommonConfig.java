package fscl.core.auxil;

public class CommonConfig {
		
	public class Web {		
		
		public static final String allowedCorsClient = "http://localhost:3000";
		
		public class CachedCodes {
			// fscl.web.cached_codes.max_timeout_seconds
			public static final long maxTimeoutSeconds = 30000;
		}
	}
	
	public class DB {
		
	}
}
