package fscl.core.domain;

import fscl.core.api.EntityApiId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.data.util.CastUtils.cast;

@MappedSuperclass
public abstract class Entity<T extends Entity<T>> 
	extends EntityContent {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "db_id")		// forcing name as so it can be used in @JoinColumn
	protected Long id;

	@Embedded
	protected EntityId code;	// must not use "id" since the repository function
								// cannot be called FindById(..), as that
								// collides with an existing name

	// *******************************************************
	// Note: this.parent and this.children belong to the same
	// bidirectional OneToMany relation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id")
	protected T parent;

	@OneToMany(mappedBy = "parent")
	protected List<T> children;
	// *******************************************************

	public Entity() {
		this.code = null;
		this.parent = null;
		this.children = new LinkedList<T>();
	}

	public Entity(Entity<T> e, CodeFormat cfg) {
		this.code = e.code;	
		this.parent = e.parent;
		this.children = e.children;		
	}

	public Entity(EntityId code, T parent, List<T> children) {
		this.code = code;
		this.parent = parent;
		this.children = new LinkedList<T>(children);
	}
	
	public Entity(EntityId id) {
		this.children = new LinkedList<T>();
		this.code = id;
	}
	
	public EntityId getCode() {
		return code;
	}

	public void setCode(EntityId id) {
		this.code = id;
	}
	
	public T getParent() {
		return this.parent;
	}
	
	public void setParent(T parent) {
		this.parent = parent;
	}
	
	public boolean hasParent() { 
		return (this.parent != null); 
	}
		
	public void addChild(T child) {
		this.children.add(child);
	}
	
	/**
	 * Remove the entity from children collection
	 * iff its code matches code. Note this is robust in presence
	 * of changed state as it doesn't evaluate children or parent but only 
	 * the code field.
	 * 
	 * @param code	Code of child to remove
	 */
	public boolean removeChild(EntityId code) {		
		
		boolean removedAtLeastOne = false;
		
		T e = null;
		Iterator<T> iter = this.children.iterator();
		while(iter.hasNext()) {
			e = iter.next();
			if(e.code.equals(code)) {
				removedAtLeastOne = this.children.remove(e);
				break;
			}				
		}
		
		return removedAtLeastOne;
	}
	
	public List<T> getChildren() {
		return new LinkedList<T>(this.children); 
	}
	
	public List<EntityApiId> getChildrenId() {
		List<EntityApiId> reduced = new ArrayList<EntityApiId>();
		this.children.forEach(e -> {
			reduced.add(e.getCode().toEntityApiId());
		});
		return reduced;
	}
	
	public EntityApiId getParentId() {
		if(this.hasParent())
			return new EntityApiId(
				this.parent.code.project.toString(), 
				this.parent.code.entity.toString());
		else
			return new EntityApiId(null, null);
	}
	
	public ProjectCode getProjectCode() {
		return this.code.project;
	}
	
	public EntityCode getEntityCode() {
		return this.code.entity;
	}
	
	
	@Override
	public String toString() {
		
		StringBuilder str = new StringBuilder();
		str.append("{code={").append(code.toString()).append("}");
		if(parent != null)
			str.append(" parent={").append(parent.getCode().toString()).append("}");
		else 
			str.append("parent={null}");
		str.append(" children: ").append(children.size()).append("}");
		
		return str.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o==null)
			return false;		
		if(!(o instanceof Entity<?>))
			return false;		
		if(o == this)
			return true;		
		Entity<T> e = cast(o);	
		
		return(
			super.equals(o) &&
			(this.parent == null ? e.parent == null : this.parent.equals(e.parent)) &&
			(this.children == null ? e.children == null : this.children.equals(e.children)) &&			
			(this.code == null ? e.code == null : this.code.equals(e.code))
		);		
	}

	/**
	 * Validate a given code as childcode:
	 * 1) must have a child part
	 * 2) parent part must match our own code
	 * 3) project must match
	 * 
	 * @return true if given child code valid, otherwise false
	 * @throws IllegalStateException if parent is not initialized
	 */
	public boolean isValidChildCode(EntityId childCode) throws IllegalStateException {
		
		if(childCode.project.equals(this.code.project)) {
			
			EntityCode otherParent = childCode.entity.getParent();
			
			if(otherParent.equals(this.code.entity)) {

				String otherTail = childCode.entity.getTail();
				if( ! otherTail.equals(this.code.entity.getCode())) {
					return true;
				} else {
					// has no child part, cannot become our child
					return false;
				}
			} else {
				// parent part not matching
				return false;
			}
		} else {
			// project not matching
			return false;
		}
	}
}
