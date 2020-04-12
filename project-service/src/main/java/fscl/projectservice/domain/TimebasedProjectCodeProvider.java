package fscl.projectservice.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Component;


/**
 * A class to provide valid codes according to a Date-based schema: 
 * 
 * YYYYMM-ccc
 * 	
 * 		where		YYYY year digits
 * 					  MM months digits (01 ... 12)
 * 					 ccc consecutive counter within month
 *    
 * @author ono *
 */
@Component("ProjectCodeProvider")
class TimebasedProjectCodeProvider implements CodeProvider {
	
	@Override
	public String generateCode(List<String> committedCodes, List<String> cachedCodes) {
	
		List<String> comittedToday = this.filterByFormatAndToday(committedCodes);
		List<String> cachedToday = this.filterByFormatAndToday(cachedCodes);
		
		List<String> todays = new ArrayList<String>();
		todays.addAll(comittedToday);
		todays.addAll(cachedToday);
		
		String now = stripOutKeys(LocalDate.now().toString(), "-");
		String newCode, countPart;		
		if(todays.isEmpty()) {
			countPart = "001";
		} else  {
			// make the new one to be one higher
			todays.sort(null); // use natural ordering
			String highest = todays.get(todays.size() -1);
			String[] splits = highest.split("-");
			Integer count = Integer.valueOf(splits[1]) + 1;
			countPart = String.format("%03d", count);
			//countPart = Integer.valueOf(count + 1).toString();			
		}	
		
		newCode = now + "-" + countPart;		
		return newCode;
	}
		
	
	protected List<String> filterByFormatAndToday(List<String> codes) {
		
		List<String> filtered = new ArrayList<String>();
		
		// find all entries of today
		String now = stripOutKeys(LocalDate.now().toString(), "-");		
		codes.forEach(code -> {			
			String[] splits = code.split("-");			
			if(splits.length == 2) {				
				if(now.equals(splits[0])) {
					filtered.add(code);					
				}
			}			
		});
		
		return filtered;
	}
		
	protected static String stripOutKeys(String datePart, String key) {
		String[] splits = datePart.split(key);
		StringBuffer str = new StringBuffer();
		for(int i = 0; i < splits.length; i++) {
			str.append(splits[i]);			
		}
		
		return str.toString();
	}
}
