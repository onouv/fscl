package fscl.projectservice.domain;


import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name="id_registration")
public class IdRegistration {

	@Id
	@Column(name = "db_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long dataBaseId;

	private final String code;
	private final UUID clientId;
	private final LocalDateTime expiration;
	
	@PersistenceConstructor
	public IdRegistration(String code, UUID clientId, LocalDateTime expiration) {
		this.code = code;
		this.clientId = clientId;
		this.expiration = expiration;
	}
	
	public IdRegistration(String code, UUID clientId, long secondsExpiring) {
		this.code = code;
		this.clientId = clientId;
		this.expiration = LocalDateTime.now().plusSeconds(secondsExpiring);
	}

	// just for the Hibernate db layer
	public IdRegistration() {
		this.code = "";
		this.clientId = null;
		this.expiration = null;
	}

	public UUID getClientId( ) {
		return this.clientId;
	}
			
	public String getCode() {
		return this.code;
	}
		
	public boolean hasExpired(LocalDateTime now) {
		return (expiration.isBefore(now));
	}
	
	public String toString() {
		StringBuilder buf = new StringBuilder("{ ");
		buf.append(String.format(" code: %s,", this.code));
		buf.append(String.format(" clientId: %s,", this.clientId.toString()));
		buf.append(String.format(" expiration: %s,", this.expiration.toString()));
		buf.append(" }");
		
		return buf.toString();
	}
	
	public boolean equals(Object o) {
		
		if(!(o instanceof IdRegistration))
			return false;
		
		IdRegistration cc = (IdRegistration) o;
		
		return (
			cc.getCode().equals(this.code) &&
			cc.clientId.equals(this.clientId) &&
			cc.expiration.equals(this.expiration)
		);		
	}
}
