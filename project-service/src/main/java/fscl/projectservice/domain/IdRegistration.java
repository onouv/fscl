package fscl.projectservice.domain;


import javax.persistence.*;
//import org.springframework.data.annotation.Id;
import org.bson.types.ObjectId;
//import org.springframework.data.annotation.PersistenceConstructor;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name="IDREGISTRATION")
public class IdRegistration {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private ObjectId dataBaseId;
	private final String code;
	private final UUID clientId;
	private final LocalDateTime expiration;
	
	//@PersistenceConstructor
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
		StringBuffer buf = new StringBuffer("{ ");
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
