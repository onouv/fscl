package fscl.core.domain.registration;

import fscl.core.domain.EntityId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import java.util.UUID;
import java.time.LocalDateTime;

@Document(collection="idregistrations")
public class IdRegistration {
	
	@PersistenceConstructor
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
	
	@Id
	private ObjectId dataBaseId;
	
	private final EntityId entityId;
	private final UUID clientId;
	private final LocalDateTime expiration;
	
	
}
