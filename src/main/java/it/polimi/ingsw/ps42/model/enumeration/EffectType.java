package it.polimi.ingsw.ps42.model.enumeration;

/**
 * Class that represents all the possibles effects available in our implementation
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public enum EffectType {
	
	OBTAIN("Obtain"), OBTAIN_BAN("Obtain_Ban"), DO_ACTION("Do_Action"), INCREASE_ACTION("Increase_Action"), 
	FOR_EACH_OBTAIN("For_Each_Obtain"), CARD_FOR_EACH_OBTAIN("Card_For_Each_Obtain"), COUNCIL_OBTAIN("Council_Obtain"), 
	INCREASE_FAMILIARS("Increase_Familiars"), INCREASE_SINGLE_FAMILIAR("Increase_Single_Familiar"), 
	CARD_BAN("Card_Ban"), NO_MARKET_BAN("No_Market_Ban"), NO_FIRST_ACTION_BAN("No_First_Action_Ban"), 
	YELLOW_COST_BAN("Yellow_Cost_Ban"), SLAVE_BAN("Slave_Ban"), NO_TOWER_BONUS("No_Tower_Bonus"),
	SET_SINGLE_FAMILIAR_LEADER("Increase_Single_Familiar_Leader"), NO_MONEY_MALUS("No_Money_Malus"),
	NO_MILITARY_REQUIREMENTS("No_Military_Requirements"), FIVE_MORE_VICTORY_POINT("Five_More_Victory_Point"),
	CAN_POSITIONING_EVERYWHERE("Can_Positioning_Everywhere"), SET_ALL_FAMILIARS_LEADER("Set_All_Familiars_Leader");
	
	private final String message;
	
	/**
	 * Private method used to set a message in the Enum Object
	 * @param message		The message to set to the Enum Object
	 */
	private EffectType(String message) {
		this.message = message;
	}
	
	/**
	 * Method used to cast a String into an Enum Object
	 * 
	 * @param effectType		The effect that represents the Enum Object
	 * @return					The real Enum Object
	 */
	public static EffectType parseInput(String effectType){
		return Enum.valueOf(EffectType.class, effectType.toUpperCase());
	}
	
	@Override
	public String toString() {
		return this.message;
	}
}
