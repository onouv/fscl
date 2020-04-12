package fscl.core.domain;

import fscl.core.domain.CodeFormat;

import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.PersistenceConstructor;

public class EntityCode {
	
	private String code;
	
	@Transient
	private String separator;
	
	@Transient
	private String prefix; 
	
	public enum STATE {
			CREATED,
			INITIALIZED			
	}
	
	@Transient
	private STATE state = STATE.CREATED;
	
	public STATE getState() {
		return state;
	}

	@PersistenceConstructor
	public EntityCode(String code) {
		
		if(code == null)
			throw new IllegalArgumentException("EntityCode: cannot construct with null code");
		
		if(code.isEmpty())
			throw new IllegalArgumentException("EntityCode: cannot construct with empty code");
		
		this.code = code;		
		this.state = STATE.CREATED;		
	}
	
	public EntityCode(String code, CodeFormat format) throws IllegalArgumentException {
		
		if(code == null)
			throw new IllegalArgumentException("EntityCode: cannot construct with null code");
		
		if(code.isEmpty())
			throw new IllegalArgumentException("EntityCode: cannot construct with empty code");
		
		this.init(format);
		this.code = code;
		this.state = STATE.INITIALIZED;
		
	}
		
	public EntityCode(EntityCode origin, CodeFormat format) {		
		this.init(format);		
		this.code = origin.code;
		this.state = STATE.INITIALIZED;
	}
		
	public String toString() {		
		return this.code;
	}
	
	public boolean isEmpty() {
		return this.code.isEmpty();
	}
	
	public String getSeparator() {
		return separator;
	}
			
	/**
	 * 
	 * @return	the String after the last separator
	 * @throws  IllegalStateException if it is called before the
	 * 			validate(...) method 
	 */
	public String getTail() {
		if(this.state == STATE.CREATED) 
			throw new IllegalStateException(
				"EntityCode: getTail() called on state CREATED instead of INITIALIZED. Recommend calling init(CodeConfig config) first");

		String tail;
		int i = this.code.lastIndexOf(this.separator);
		if(i < 0) {
			
			// we are a root. Cut away prefix...
			tail = this.code.substring(1, this.code.length());		
			
		} else {
			
			// we are a child. Extract trailing segment...
			tail = this.code.substring(i + 1);
		}
		
		return tail;
		
	}
	
	public int getTailValue(String prefix) {
		
		if(this.state == STATE.CREATED) 
			throw new IllegalStateException(
				"EntityCode: getTailValue() called on state CREATED instead of INITIALIZED. Recommend calling init(CodeConfig config) first");

		String tail = this.getTail();
		if(tail.isEmpty()) {
			tail = this.code.substring(this.code.indexOf(prefix) + 1);
		}
		return Integer.parseInt(tail);
	}

	/**
	 * @return 	the code our parent would have, if possible. 
	 * 			Otherwise return null. I.e. : 
	 * 			=200 			--> null
	 * 			=200.001 		--> =200
	 * 			=200.001.019 	--> =200.001
	 * @throws 	IllegalStateException when called in state CREATED. This 
	 * 			being the case when called before init(CodeConfig config)
	 * 			has been called.
	 */
	public EntityCode getParent() throws IllegalStateException {

		if(this.state == STATE.CREATED) 
			throw new IllegalStateException(
				"EntityCode: getParent() called on state CREATED instead of INITIALIZED. Recommend calling init(CodeConfig config) first");

		int index = this.code.lastIndexOf(this.separator);
		
		if(index > -1) {
			// there is a parent...
			String parent = this.code.substring(0, index);
			return new EntityCode(
					parent, 
					new CodeFormat(this.prefix, this.separator));
		} else {
			
			// there is no parent, we are a root
			return null;
		}
	}
	
	/**
	 * Validates that 
	 * 
	 * (1) code is not empty
	 * 
	 * (2) code only contains combinations of :
	 * (2.1) alphas [a...z, A...Z]
	 * (2.2) numbers [0...9]
	 * (2.3) separators (any String)	 * 
	 * 	 where: 
	 * 	 (a) no separator may follow directly on another separator 
	 * 	 (b) no separator stand on beginning or end
	 * 
	 * (3) code starts with prefix
	 * (4) prefix not used anywhere else in code
	 * 
	 * @return
	 */
	public boolean isValid(CodeFormat config) {
		
		this.init(config);		
		
		if(this.isEmpty())
			return false;
		
		if(!this.startsWithPrefix(this.prefix))
			return false;
		
		if(hasAdditionalPrefix(this.prefix))
			return false;

		if(hasInValidCharacters())		
			return false;
			
		if(hasEdgeSeparators(this.prefix, this.separator))
			return false;
			
		if(hasAdjacentSeparators(this.separator))
			return false;
				
		return true;
	}
	
	
	public void init(CodeFormat config) {
		this.separator = config.getSeparator();
		this.prefix = config.getPrefix();
		this.state = STATE.INITIALIZED;
	}
	
	public boolean isRoot() {
		
		if(this.state == STATE.CREATED) 
			throw new IllegalStateException(
				"EntityCode: isRoot() called on state CREATED instead of INITIALIZED. Recommend calling init(CodeConfig config) first");

		return (this.code.indexOf(this.separator) < 0 ) ? true: false;
	}
	
	private boolean startsWithPrefix(String prefix) {
		return this.code.startsWith(prefix);
	}
	
	private boolean hasAdditionalPrefix(String prefix) {
		int i = this.code.indexOf(prefix);
		String rest = this.code.substring(i+prefix.length());
		return rest.contains(prefix);
	}
	
	private boolean hasInValidCharacters() {
		// must do some regexp magic here ...
		return false;
	}
	
	private boolean hasEdgeSeparators(String prefix, String separator) {
		if(this.code.startsWith(separator))
			return true;
		
		if(this.code.endsWith(separator))
			return true;
		
		int i = this.code.indexOf(prefix);
		String rest = this.code.substring(i+prefix.length());
		if(rest.startsWith(separator))
			return true;
		
		return false;
	}
	
	private boolean hasAdjacentSeparators(String separator) {
		String fault = separator + separator;
		return this.code.contains(fault);		
	}
	
	
	@Override
	public boolean equals(Object o) {
		if(o==null)
			return false;
		
		if(!(o instanceof EntityCode))
			return false;
		
		if(o == this)
			return true;
		
		EntityCode c = (EntityCode) o;
		
		return (
			(this.code == null ? c.code == null : this.code.equals(c.code)) //&&
			//(this.state == null ? c.state == null : this.state.equals(c.state)) &&
			//(this.prefix == null ? c.prefix == null : this.prefix.equals(c.prefix)) &&
			//(this.separator == null ? c.separator == null : this.separator.equals(c.separator))
		);
		
	}
}