package fscl.projectservice.domain;

import java.util.List;

public interface CodeProvider {
	
	public String generateCode(List<String> committedCodes, List<String> cachedCodes);
	
}
