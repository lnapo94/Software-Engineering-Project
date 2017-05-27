package it.polimi.ingsw.ps42.model.action;

import java.util.List;

import it.polimi.ingsw.ps42.model.effect.IncreaseAction;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.Position;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public abstract class Action {	
	
	/*Class for basic action, requires the implementation of checkAction,
	* doAction, createRequest
	*/
	
	private ActionType type;
	protected Familiar familiar;
	protected Player player;
	protected int positionValue;
	protected List<Position> tableLocation;
	protected Packet discount;
	protected int actionValue;
	
	
	public Action(ActionType type, Familiar familiar,List<Position> tablePosition, int positionInTableList){
		//Constructor for normal action, player is get from familiar
		
		this.type=type;
		this.familiar=familiar;
		this.player=familiar.getPlayer();
		this.positionValue=positionInTableList;
		this.tableLocation=tablePosition;
		this.actionValue=familiar.getValue();
			
	}
	public Action(ActionType type, Player player,List<Position> tablePosition, int positionInTableList, int actionValue){
		//Constructor for bonus action (no familiar involved, so requires the player) 
		
		this.type=type;
		this.player=player;
		this.positionValue=positionInTableList;
		this.tableLocation=tablePosition;
		this.actionValue=actionValue;
	}
	
	public abstract void checkAction();		//Does all the required checks before the action is applicated 
	
	public abstract void doAction();		//Apply the player action 
	
	public void incrementActionValue(int value){		//Increments the value of the action 
		actionValue+=value;
		
	}
	
	private void checkIncreaseEffect(){			//Checks if the player has some increase effects active and apply them
		List<IncreaseAction> playerIncreaseAction = player.getIncreaseEffect();
		
		for (IncreaseAction increaseAction : playerIncreaseAction) {
			increaseAction.activeIncrease(this);
		}
		
	}
	
	public void addDiscount(Packet discount) {		//Adds a discount at the cost of the action
		if(this.discount==null)
			this.discount = discount;
		else{
			for (Unit unit : discount) {
				this.discount.addUnit(unit);
			}	
		}
			
	}
	
	public abstract void createRequest();		//Create a request to better define the action and put it in the player 
	
	public ActionType getType() {
		return type;
	}
	
	public void addIncrement(int increment){
		
		
	}
	
}
