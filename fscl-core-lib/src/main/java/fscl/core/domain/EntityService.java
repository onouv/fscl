package fscl.core.domain;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import fscl.core.db.NoSuchCodedItemException;
import fscl.core.api.EntityApiId;
import fscl.core.api.UpdateEntityContent;


public abstract class EntityService<T extends Entity<T>> {
	
	public abstract EntityId createNewRegistration(
		ProjectCode projectCode,
		String parentCode,
		UUID clientId,
		long timeOutSeconds) throws NoSuchCodedItemException;
		
	public T updateEntity(T entity,	UpdateEntityContent content)	
		throws NoSuchCodedItemException, IllegalArgumentException {
		
		CodeFormat format = this.loadCodeConfig(entity.getProjectCode());

		//***********************************************************		
		// figure out the update scenario as defined by the following  
		// control variables...
		final boolean isChild = entity.hasParent();
		final boolean hasChildren = ! (entity.getChildren().isEmpty());		
		final boolean changeIdentity = ! entity.getCode().equals(new EntityId(
				content.getSelf(),
				format));
		
		boolean changeParent;
		EntityApiId contentParent = content.getParent();		
		if(contentParent != null) {
			if(isChild) {
				EntityId requestParentId = new EntityId(contentParent,format);
				EntityId currentParentId = entity.getParent().getCode();
				if(requestParentId.equals(currentParentId)) {
					changeParent = false;
				} else {
					changeParent = true;
				}
			} else {				
				changeParent = true;
			}			
		} else {
			changeParent = false;
		}
			
		//***********************************************************
		// work through the different update scenarios...
		
		// case 1 ?
		if(changeIdentity) {
			
			EntityId newCode = new EntityId(content.getSelf(), format);
			EntityId oldCode = entity.getCode();
		
			if(find(newCode) != null) {
				throw new IllegalArgumentException(String.format("requested recoding in collision with duplicate entity %s", newCode));
			}
				
			// Case 1 ?
			if(!changeParent) {
				
				T recoded = this.updateDeleteEntitiesCode(entity, newCode); 	//1
				T updated = this.updateSaveEntitiesContent(recoded, content);	//2
				
				// Case 1.1 ? 
				// nuffin' special at all
				
				// case 1.2 ? 
				if(isChild & !hasChildren) {
					this.updateSaveEntityAtParent(updated, oldCode);				//3					
				}
				
				// case 1.3 ?
				if(isChild && hasChildren ) {
					this.updateSaveEntityAtParent(updated, oldCode);				 //3
					this.updateSaveChildrensParents(updated.getChildren(), updated); //4
				
				}
				
				// case 1.4 ?
				if(!isChild && hasChildren) {
					// this appears to be redundant as MongoDB seems to already have 
					// updated the appropriate @DBRef fields when we get here. 
					// We explicitly update for now, in case this is somehow timing-
					// dependent
					this.updateSaveChildrensParents(updated.getChildren(), updated); //3				
				}
				
				this.publishRecoding(updated, oldCode);
				return updated;					
				
			} else {
				// Case 2S
						
				T oldParent = entity.getParent();
				T newParent = this.find(new EntityId(
						content.getParent(),
						format));			
					
				T recoded = this.updateDeleteEntitiesCode(entity, newCode); 			//1
				T reparented = this.updateEntitiesParent(recoded, newParent);			//2
				T updated = this.updateSaveEntitiesContent(reparented, content); //3
				this.addSaveChildToParent(newParent, updated);					//4
				
				// case 2.1 ?
				if(!isChild && !hasChildren) {				
					; // nuffin' else
				}
				
				// case 2.2 ?
				if(isChild && !hasChildren) {
					this.removeSaveChildFromParent(oldParent, oldCode);					//5
				}
				
				// case 2.3 ?
				if(isChild && hasChildren) {
					this.removeSaveChildFromParent(oldParent, oldCode);					//5
					this.updateSaveChildrensParents(updated.getChildren(), updated);				
				}
				
				// case 2.4 ?
				if(!isChild && hasChildren) {
					this.updateSaveChildrensParents(updated.getChildren(), updated);				
				}
				
				this.publishRecoding(updated, oldCode);
				return updated;
				
			}
			
		} else {
			
			// case 3 ?
			if(changeParent) {
				throw new IllegalArgumentException(
						"must update entity code when changing parent entity!");
			} else {
				// case 4 
				return this.updateSaveEntitiesContent(entity, content);
			}
		}
	}
	
		
	public List<EntityApiId> deleteEntity(T entity, boolean initialCall) 
		throws 	IllegalArgumentException, 
				NoSuchCodedItemException {
		
		ArrayList<EntityApiId> deletedEntities = new ArrayList<EntityApiId>();
		List<T> childs = entity.getChildren();
		if(childs.isEmpty()) {			
			
			T parent = entity.getParent();
			if(parent != null ) {
				parent.removeChild(entity.getCode());
				
				if(initialCall) 
					this.saveToRepository(parent);		
			}

			deletedEntities.add(entity.getCode().toEntityApiId());
			this.deleteFromRepository(entity);
			this.publishDeletion(entity);			
			
		} else {			
			for(T e: childs) { 
				deletedEntities.addAll(this.deleteEntity(e, false)); // recurse...				
			}			
			deletedEntities.add(entity.getCode().toEntityApiId());
			this.deleteFromRepository(entity);
			this.publishDeletion(entity);
			
		}	
		return deletedEntities;
	}	
	
	/**
	 * remove, update parent reference and resave all children
	 *  
	 * @param children
	 * @param newParent
	 */
	protected void updateSaveChildrensParents(List<T> children, T newParent) {
		for(T c: children) {
			//this.deleteFromRepository(c);
			c.setParent(newParent);
			this.saveToRepository(c);
		}
	}
	
	protected void updateSaveEntityAtParent(T recodedEntity, EntityId oldCode) {
		recodedEntity.parent.removeChild(oldCode);
		recodedEntity.parent.addChild(recodedEntity);
		this.saveToRepository(recodedEntity.parent);
	}
	
	protected T updateDeleteEntitiesCode(T entity, EntityId newCode) {
		this.deleteFromRepository(entity);
		entity.setCode(newCode);
		return entity;
	}
	
	protected T updateEntitiesParent(T entity, T parent) {
		entity.setParent(parent);
		return entity;
	}			
	
	protected T updateSaveEntitiesContent(T entity, EntityContent content) {
		entity.setName(content.getName());
		entity.setDescription(content.getDescription());
		this.saveToRepository(entity);
		return entity;
	}
	
	protected void addSaveChildToParent(T parent, T child) {
		parent.addChild(child);
		this.saveToRepository(parent);
	}
	
	protected void removeSaveChildFromParent(T parent, EntityId code) {
		parent.removeChild(code);
		this.saveToRepository(parent);
	}
				
	protected abstract void deleteFromRepository(T entity);
	protected abstract void saveToRepository(T entity);
	protected abstract void publishRecoding(T entity, EntityId oldCode);
	protected abstract void publishDeletion(T entity);
	protected abstract T find(EntityId id);
	protected abstract CodeFormat loadCodeConfig(ProjectCode project);
	
	
}
























