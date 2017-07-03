package it.polimi.ingsw.ps42.model.enumeration;

/**
 * Class that represents all the resources available in the game
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public enum Resource {
	//Type of Resources present in the game. Council isn't added in this enum because it is
	//immediately converted in a resource
	
	VICTORYPOINT("VictoryPoint"), FAITHPOINT("FaithPoint"), 
	MILITARYPOINT("MilitaryPoint"), WOOD("Wood"), SLAVE("Slave"), 
	MONEY("Money"), STONE("Stone");
	
	private final String message;
	
	/**
	 * Private method used to set a message in the Enum Object
	 * @param message		The message to set to the Enum Object
	 */
	private Resource(String message) {
		this.message = message;
	}
	
	/**
	 * Private method used to cast a String into the correct Enum Object
	 * @param resource		The string to cast into an Enum Object
	 * @return				The real Enum Object
	 */
	public static Resource parseInput(String resource){
		return Enum.valueOf(Resource.class, resource.toUpperCase());
	}
	
	/**
	 * Method used to print this Enum
	 */
	@Override
	public String toString() {
		return this.message;
	}
}
