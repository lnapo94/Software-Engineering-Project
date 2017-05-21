package it.polimi.ingsw.ps42.model.exception;

public class IsNotEmptyException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1216366499317104406L;
	
	public IsNotEmptyException(String message) {
		super(message);
	}

}
