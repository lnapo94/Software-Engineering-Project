package it.polimi.ingsw.ps42.model.exception;

public class EmptyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1852135089546334002L;
	
	public EmptyException(String message) {
		super(message);
	}
}
