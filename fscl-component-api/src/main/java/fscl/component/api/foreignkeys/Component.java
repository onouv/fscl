package fscl.component.api.foreignkeys;

import fscl.core.domain.EntityId;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Embedded;

import static org.springframework.data.util.CastUtils.cast;

@Entity
@Table(name = "component_fk")
public final class Component {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "db_id")		// forcing name as so it can be used in @JoinColumn
	private Long id;

	@Embedded
	private EntityId code;

	// "many" end of unidirectional @OneToMany relation
	@Column(name = "many_ref")
	private Long manyRef;
	
	Component() {
		//this.code = new EntityId("invalid!", "invalid!");
	}

	private Component(EntityId code) {
		this.code = code;
	}

	public static Component newInstance(EntityId code) {
		return new Component(code);
	}

	public EntityId getCode() {
		return code;
	}

	public Long getId() {
		return id;
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
