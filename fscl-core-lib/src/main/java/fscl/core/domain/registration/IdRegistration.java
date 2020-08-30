package fscl.core.domain.registration;

import fscl.core.domain.EntityId;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="id_registration")
public class IdRegistration {

	@Id
	@Column(name = "db_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long dataBaseId;

	@Embedded
	private final EntityId entityId;

	@Column(name = "client")
	private final UUID clientId;

	@Column(name = "expires")
	private final LocalDateTime expiration;

	public IdRegistration() {
		this.dataBaseId = null;
		this.entityId = null;
		this.clientId = null;
		this.expiration = null;
	}
	
	public IdRegistration(EntityId entityId, UUID clientId, LocalDateTime expiration) {
		this.entityId = entityId;
		this.clientId = clientId;
		this.expiration = expiration;
	}
	
	public IdRegistration(EntityId entityId, UUID clientId, long secondsExpiring) {
		this.entityId = entityId;
		this.clientId = clientId;
		this.expiration = LocalDateTime.now().plusSeconds(secondsExpiring);
	}
		
	public UUID getClientId( ) {
		return this.clientId;
	}
			
	public EntityId getId() {
		return this.entityId;
	}

	public boolean hasExpired(LocalDateTime now) {
		return (expiration.isBefore(now));
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer("{ ");
		buf.append(String.format(" code: %s,", this.entityId));
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
			cc.getId().equals(this.entityId) &&
			cc.clientId.equals(this.clientId) &&
			cc.expiration.equals(this.expiration)
		);		
	}
	
	public LocalDateTime getExpiration() {
		return expiration;
	}

}
