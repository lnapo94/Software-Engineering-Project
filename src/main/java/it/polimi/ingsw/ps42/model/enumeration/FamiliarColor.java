package it.polimi.ingsw.ps42.model.enumeration;

public enum FamiliarColor {
	//This enum represent all the familiar color
	//"All" color indicates a particular state 
	
	ORANGE("Orange"), BLACK("Black"), WHITE("White"), NEUTRAL("Neutral"), ALL("All color");
	
	private final String message;
	
	private FamiliarColor(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return this.message;
	}

}
