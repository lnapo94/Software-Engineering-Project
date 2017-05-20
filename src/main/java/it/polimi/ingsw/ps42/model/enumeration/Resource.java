package it.polimi.ingsw.ps42.model.enumeration;

public enum Resource {
	VICTORYPOINT("VictoryPoint"), FAITHPOINT("FaithPoint"), MILITARYPOINT("MilitaryPoint"), WOOD("Wood"), SLAVE("Slave"), MONEY("Money"), STONE("Stone");
	
	private final String message;
	
	private Resource(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return this.message;
	}
}
