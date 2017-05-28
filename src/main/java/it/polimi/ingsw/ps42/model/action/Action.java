package it.polimi.ingsw.ps42.model.action;

import java.util.List;

import it.polimi.ingsw.ps42.model.effect.IncreaseAction;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.enumeration.Response;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public abstract class Action {	
	
	/*Class for basic action, requires the implementation of checkAction,
	* doAction, createRequest
	*/
	
	private ActionType type;
	protected Familiar familiar;
	protected Player player;
	protected Packet discount;
	protected int actionValue;
	
	
	public Action(ActionType type, Familiar familiar) throws NotEnoughResourcesException{
		//Constructor for normal action, player is get from familiar
		
		this.type=type;
		this.familiar=familiar;
		this.player=familiar.getPlayer();
		
		//Control if player has enough slave to increment his familiar
		if(player.getResource(Resource.SLAVE) > familiar.getIncrement())
			throw new NotEnoughResourcesException("Player hasn't enough slaves to increment the action");
		
		this.actionValue = familiar.getIncrement() + familiar.getValue();
	}
	public Action(ActionType type, Player player, int actionValue){
		//Constructor for bonus action (no familiar involved, so requires the player) 
		
		this.type=type;
		this.player=player;
		this.actionValue=actionValue;
	}
	
	public abstract Response checkAction();		//Does all the required checks before the action is applicated 
	
	public abstract void doAction() throws FamiliarInWrongPosition;		//Apply the player action 

	
	protected void checkIncreaseEffect(){			//Checks if the player has some increase effects active and apply them
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
	
	
	public ActionType getType() {
		return type;
	}
	
	public int getActionValue() {
		return actionValue;
	}
	
	public void addIncrement(int increment){
		
		this.actionValue += this.actionValue + increment;
	}
	
	//Method used in other classes to control if the player can do the action
	protected Response checkBanInPlayer() {
		if(!player.canPlay())
			return Response.CANNOT_PLAY;
		return Response.SUCCESS;
	}
	
}
