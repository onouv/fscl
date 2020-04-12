package fscl.component.api.foreignkeys;

import fscl.core.domain.EntityId;

import static org.springframework.data.util.CastUtils.cast;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="components")
public class Component {
	
	@Id
	private EntityId code;
	
	public Component() {
		this.code = new EntityId("invalid!", "invalid!");
	}
	
	@PersistenceConstructor
	public Component(EntityId code) {
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
		if(!(o instanceof Component))
			return false;		
		if(o == this)
			return true;		
		
		Component c = cast(o);	
		
		return (this.code.equals(c.code));
			
	}

}
