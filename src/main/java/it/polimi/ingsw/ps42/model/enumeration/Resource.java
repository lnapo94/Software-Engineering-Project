package it.polimi.ingsw.ps42.model.enumeration;

public enum Resource {
	//Type of Resources present in the game. Council isn't added in this enum because it is
	//immediately converted in a resource
	
	VICTORYPOINT("VictoryPoint"), FAITHPOINT("FaithPoint"), 
	MILITARYPOINT("MilitaryPoint"), WOOD("Wood"), SLAVE("Slave"), 
	MONEY("Money"), STONE("Stone");
	
	private final String message;
	
	private Resource(String message) {
		this.message = message;
	}
	
	public static Resource parseInput(String color){
		return Enum.valueOf(Resource.class, color.toUpperCase());
	}
	@Override
	public String toString() {
		return this.message;
	}
}
