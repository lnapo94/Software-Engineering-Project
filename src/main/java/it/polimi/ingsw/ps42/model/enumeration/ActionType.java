package it.polimi.ingsw.ps42.model.enumeration;

public enum ActionType {
	//Type of Action, TAKE_ALL stands for take a card of whatever color
	PRODUCE("Production Action"), YIELD("Yield Action"), MARKET("Market Action"), COUNCIL("Council Action"),
	TAKE_GREEN("Take Green Card Action"), TAKE_YELLOW("Take Yellow Card Action"), TAKE_BLUE("Take Blue Card Action"),
	TAKE_VIOLET("Take Violet Card Action"), TAKE_ALL("Take A Card Action");
	
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
