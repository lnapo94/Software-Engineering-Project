package it.polimi.ingsw.ps42.model.enumeration;

/**
 * Enumeration class that represents all the Action types in our implementation
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public enum ActionType {
	//Type of Action, TAKE_ALL stands for take a card of whatever color
	PRODUCE("Produce"), YIELD("Yield"), MARKET("Market"), COUNCIL("Council"),
	TAKE_GREEN("Take_Green"), TAKE_YELLOW("Take_Yellow"), TAKE_BLUE("Take_Blue"),
	TAKE_VIOLET("Take_Violet"), TAKE_ALL("Take_All");
	
	private final String message;
	
	/**
	 * Private method used to set a message in the Enum Object
	 * @param message		The message to set to the Enum Object
	 */
	private ActionType(String message) {
		
		this.message=message;
	}
	/**
	 * Method used to cast a string into the Enum Object
	 * 
	 * @param actionType	The String that represents the action type
	 * @return				The real Enum Object
	 */
	public static ActionType parseInput(String actionType){
		return Enum.valueOf(ActionType.class, actionType.toUpperCase());
	}
	
	/**
	 * Method used to print this Enum
	 */
	@Override
	public String toString(){
		
		return this.message;
	}
	
}
