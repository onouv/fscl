package fscl.function.api.foreignkeys;

import fscl.core.domain.EntityId;

import static org.springframework.data.util.CastUtils.cast;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="functions")
public class Function {	
	
	
	@Id
	private EntityId code;
	
	public Function() {
		this.code = new EntityId("", "");
	}
	
	@PersistenceConstructor
	public Function(EntityId code) {
		this.code = code;
	}
	
	public void setCode(EntityId code) {
		this.code = code;
	}

	public EntityId getCode() {
		return code;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o==null)
			return false;		
		if(!(o instanceof Function))
			return false;		
		if(o == this)
			return true;		
		
		Function c = cast(o);	
		
		return (this.code.equals(c.code));
			
	}
	
}
