package it.polimi.ingsw.ps42.model.enumeration;

public enum ActionType {
	//Type of Action, TAKE_ALL stands for take a card of whatever color
	PRODUCE("Produce"), YIELD("Yield"), MARKET("Market"), COUNCIL("Council"),
	TAKE_GREEN("Take_Green"), TAKE_YELLOW("Take_Yellow"), TAKE_BLUE("Take_Blue"),
	TAKE_VIOLET("Take_Violet"), TAKE_ALL("Take_All");
	
	private final String message;
	
	private ActionType(String message) {
		
		this.message=message;
	}
	public static ActionType parseInput(String actionType){
		return Enum.valueOf(ActionType.class, actionType.toUpperCase());
	}
	@Override
	public String toString(){
		
		return this.message;
	}
	
}
