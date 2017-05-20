package it.polimi.ingsw.ps42.model.enumeration;

public enum EffectType {
	//Type of effect used in "normal" cards and ban cards
	
	OBTAIN("Obtain"), DO_ACTION("Do another Action"), INCREASE_ACTION("Increase an Action"), 
	FOR_EACH_OBTAIN("Obtain something for each resources"), COUNCIL_OBTAIN("Obtain council privilege"), 
	INCREASE_FAMILIARS("Increase All Familiars"), INCREASE_SINGLE_FAMILIAR("Increase Single Familiar"), 
	CARD_BAN("Card Ban"), NO_MARKET_BAN("No Market Ban"), NO_FIRST_ACTION_BAN("No First Action Ban"), 
	YELLOW_COST_BAN("Yellow Cost Ban"), RESOURCE_BAN("Resource Ban");
	
	private final String message;
	
	private EffectType(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return this.message;
	}
}
