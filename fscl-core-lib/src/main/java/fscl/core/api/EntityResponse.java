package fscl.core.api;

import java.util.List;
import java.util.ArrayList;
import fscl.core.domain.Entity;

public class EntityResponse<T extends Entity<T>> {	
	
	EntityResponse() {
		this.entities = new ArrayList<ReadEntityContent<T>>();
		this.error = null;
	}
	
	
	public EntityResponse(List<T> entities) {		
		this.entities = new ArrayList<ReadEntityContent<T>>();
		this.error = null;
		
		for(T entity : entities) {
			ReadEntityContent<T> responseData = new ReadEntityContent<T>(entity);
			this.entities.add(responseData);
		}
	}
	
	public EntityResponse(String error) {
		this.entities = new ArrayList<ReadEntityContent<T>>();
		this.error = error;
	}
	
	public List<ReadEntityContent<T>> getEntities() {
		return this.entities;
	}
	
	public String getError() {
		return this.error;
	}
	
	public void setError(String errMsg) {
		this.error = errMsg;
	}
			
	private ArrayList<ReadEntityContent<T>> entities;
	private String error;
	
}
