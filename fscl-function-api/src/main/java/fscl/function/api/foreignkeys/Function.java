package fscl.function.api.foreignkeys;

import fscl.core.domain.EntityId;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;

import static org.springframework.data.util.CastUtils.cast;

@Entity
@Table(name = "functions_fk")
public class Function {	

	@Id
	@Column(name = "db_id")
	private Long id;

	@Embedded
	private EntityId code;

	// "many" end of unidirectional @OneToMany relation
	@Column(name = "many_ref")
	private Long manyRef;
	
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
