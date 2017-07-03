package it.polimi.ingsw.ps42.model.enumeration;

/**
 * Class that represents the possibles status of an Action.Used to know the status of 
 * the interested action
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public enum Response {
	SUCCESS("Action Done with Success"), FAILURE("Action not completed"),
	LOW_LEVEL("Low Level"), CANNOT_PLAY("Player can't play");
	
	private final String message;
	
	/**
	 * Private method used to set a message in the Enum Object
	 * @param message		The message to set to the Enum Object
	 */
	private Response(String message) {
		this.message = message;
	}
	
	/**
	 * Method used to print this object
	 */
	@Override
	public String toString() {
		return this.message;
	}
}
