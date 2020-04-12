package fscl.core.domain.registration;

import fscl.core.domain.CodeFormat;
import fscl.core.domain.EntityId;
import fscl.core.domain.ProjectCode;
import fscl.core.domain.EntityCode;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class DefaultIdProvider  {
		
	public static EntityId generateNewId(
			List<EntityId> committed, 
			List<EntityId> registered,
			String parentCode,
			ProjectCode project,
			CodeFormat format) {
		
		List<EntityId> combined = new ArrayList<EntityId>();
		combined.addAll(committed);
		combined.addAll(registered);
		
		String prefix = format.getPrefix();
		
		Optional<EntityId> highest = combined.stream().max(new Comparator<EntityId> () {
			@Override
			public int compare(EntityId o1, EntityId o2) {
				int a = o1.entity.getTailValue(prefix);
				int b = o2.entity.getTailValue(prefix);
				return (int) (a - b);
			}
		});
		
		String newTail;
		String newCode;
		if(highest.isPresent()) {
			
			EntityId id = highest.get();
			int newValue = id.entity.getTailValue(prefix) + 1;
			newTail = String.format("%03d", newValue);
			EntityCode pC = id.entity.getParent();			
			if( pC != null ) {
				newCode = pC.toString() + "." + newTail;
			} else {
				newCode = format.getPrefix() + newTail;
			}			
			EntityCode newEntityCode = new EntityCode(newCode, format);
			
			return new EntityId(project, newEntityCode);
			
		} else {
			
			return new EntityId(project, generateDefault(parentCode, format)); 
		}
		
		
	}
	
	protected static EntityCode generateDefault(String parentCode, CodeFormat format) {
		
		String code;
		if(parentCode == null) 
			code = format.getPrefix() + "001";			
		 else 
			code = parentCode + format.getSeparator() + "001";			
				
		return new EntityCode(code, format);
	}
}