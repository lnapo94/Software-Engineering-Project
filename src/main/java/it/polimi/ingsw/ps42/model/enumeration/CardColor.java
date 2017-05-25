package it.polimi.ingsw.ps42.model.enumeration;

public enum CardColor {
	//This enum represents the available card color
	//All color is used to choose all the cards
	
	GREEN("Green Card"), YELLOW("Yellow Card"), BLUE("Blue Card"), VIOLET("Violet Card"), ALL("All cards");
	
	private final String message;
	
	private CardColor(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return this.message;
	}

}
