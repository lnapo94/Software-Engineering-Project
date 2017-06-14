package it.polimi.ingsw.ps42.model.enumeration;

public enum EffectType {
	//Type of effect used in "normal" cards and ban cards
	
	OBTAIN("Obtain"), OBTAIN_BAN("Obtain_Ban"), DO_ACTION("Do_Action"), INCREASE_ACTION("Increase_Action"), 
	FOR_EACH_OBTAIN("For_Each_Obtain"), CARD_FOR_EACH_OBTAIN("Card_For_Each_Obtain"), COUNCIL_OBTAIN("Council_Obtain"), 
	INCREASE_FAMILIARS("Increase_Familiars"), INCREASE_SINGLE_FAMILIAR("Increase_Single_Familiar"), 
	CARD_BAN("Card_Ban"), NO_MARKET_BAN("No_Market_Ban"), NO_FIRST_ACTION_BAN("No_First_Action_Ban"), 
	YELLOW_COST_BAN("Yellow_Cost_Ban"), SLAVE_BAN("Slave_Ban"), NO_TOWER_BONUS("No_Tower_Bonus"),
	INCREASE_SINGLE_FAMILIAR_LEADER("Increase_Single_Familiar_Leader"), NO_MONEY_MALUS("No_Money_Malus"),
	NO_MILITARY_REQUIREMENTS("No_Military_Requirements"), FIVE_MORE_VICTORY_POINT("Five_More_Victory_Point"),
	CAN_POSITIONING_EVERYWHERE("Can_Positioning_Everywhere");
	
	private final String message;
	
	private EffectType(String message) {
		this.message = message;
	}
	public static EffectType parseInput(String effectType){
		return Enum.valueOf(EffectType.class, effectType.toUpperCase());
	}
	
	@Override
	public String toString() {
		return this.message;
	}
}
