package it.polimi.ingsw.ps42.model.enumeration;

/**
 * Class that represents all the color of the cards available in the game.
 * The ALL color refers to all the cards
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public enum CardColor {
	
	GREEN("Green Card"), YELLOW("Yellow Card"), BLUE("Blue Card"), VIOLET("Violet Card"), ALL("All cards");
	
	private final String message;
	
	/**
	 * Private method used to set a message in the Enum Object
	 * @param message		The message to set to the Enum Object
	 */
	private CardColor(String message) {
		this.message = message;
	}
	
	/**
	 * Method used to cast a string into the Enum Object
	 * @param color		The string that represent a CardColor enum
	 * @return			The real Enum Object
	 */
	public static CardColor parseInput(String color) {
		return Enum.valueOf(CardColor.class, color.toUpperCase());
	}
	
	/**
	 * Method used to print this Enum
	 */
	@Override
	public String toString() {
		return this.message;
	}

}
