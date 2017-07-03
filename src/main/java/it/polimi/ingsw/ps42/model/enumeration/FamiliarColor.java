package it.polimi.ingsw.ps42.model.enumeration;

/**
 * Class that represents all the colors available for the familiars in the game
 * The All color represent all the familiars
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public enum FamiliarColor {
	//This enum represent all the familiar color
	//"All" color indicates a particular state 
	
	ORANGE("Orange"), BLACK("Black"), WHITE("White"), NEUTRAL("Neutral"), ALL("All color");
	
	private final String message;
	
	/**
	 * Private method used to set a message in the Enum Object
	 * @param message		The message to set to the Enum Object
	 */
	private FamiliarColor(String message) {
		this.message = message;
	}
	
	/**
	 * Method used to cast a String into an Enum Object
	 * @param color		The string to cast into the Enum Object
	 * @return			The real Enum Object
	 */
	public static FamiliarColor parseInput(String color){
		return Enum.valueOf(FamiliarColor.class, color.toUpperCase());
	}
	
	/**
	 * Method used to print this Enum
	 */
	@Override
	public String toString() {
		return this.message;
	}

}
