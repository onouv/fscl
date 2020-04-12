package fscl.core.api;

import fscl.core.domain.EntityContent;
import fscl.core.api.EntityApiId;

import java.util.UUID;

public class CreateEntityContent extends EntityContent {
	
	protected final EntityApiId parent;
	protected final UUID clientId;
	
	public UUID getClientId() {
		return this.clientId;
	}

	public CreateEntityContent() {		
		this.parent = null;
		this.clientId = null;
	}	
	
	CreateEntityContent(EntityApiId parent, UUID clientId) {
		this.parent = parent;
		this.clientId = clientId;
	}	
	
	public EntityApiId getParent() {
		return parent;
	}
	
	public boolean hasParent() {
		return this.parent != null;
	}
	
	public boolean isValid() {
		return clientId != null;
	}

}
