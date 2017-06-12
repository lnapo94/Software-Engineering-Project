package it.polimi.ingsw.ps42.model.exception;

public class NotEnoughPlayersException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5957435645781319734L;
	
	public NotEnoughPlayersException(String message) {
		super(message);
	}

}
