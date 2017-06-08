package it.polimi.ingsw.ps42.model.action;

import java.util.List;
import java.util.Observable;

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

public abstract class Action extends Observable{	
	
	/*Class for basic action, requires the implementation of checkAction,
	* doAction, createRequest
	*/
	
	private ActionType type;
	protected Familiar familiar;
	protected Player player;
	protected Packet discount;
	protected int actionValue;
	
	/*	actionValue is the current value of the action. We have two possibilities for the
	*	action: a "normal" action, in which the player move the familiar in one position
	*	and a "bonus" action, in which the player select only a position, without positioning
	*	any familiar. In this last case, we have an action value, taken from the card, and we
	*	haven't a value taken from the familiar. So we need a variable to have the correct
	*	value, to do some control, to rise a bonus action and so on...
	*	Then we decide to use this variable also for the "normal" action with familiar, and we
	*	set it while the action is constructed
	*/
	
	/*	The increment the player wants to do is in familiar.getIncrement, the others kinds
	 * 	of increments are stored in actionValue
	 */
	
	public Action(ActionType type, Familiar familiar) throws NotEnoughResourcesException{
		//Constructor for normal action, player is get from familiar
		
		this.type=type;
		this.familiar=familiar;
		this.player=familiar.getPlayer();
		
		//Control if player has enough slave to increment his familiar
		
		int slaveToPay = familiar.getIncrement() * player.getDivisory();
		paySlave(slaveToPay);
		this.actionValue = familiar.getIncrement() + familiar.getValue();
	}
	public Action(ActionType type, Player player, int actionValue, int actionIncrement) throws NotEnoughResourcesException {
		//Constructor for bonus action (no familiar involved, so requires the player) 
		
		this.type = type;
		this.player = player;
		this.actionValue = actionValue;
		
		//Check player slave resources
		paySlave(actionIncrement);
		this.actionValue += actionIncrement;
	}
	
	private void paySlave(int slaveToPay) throws NotEnoughResourcesException {
		
		Packet slavePacket = new Packet();
		slavePacket.addUnit(new Unit(Resource.SLAVE, slaveToPay));
		player.decreaseResource(slavePacket);
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
		
		this.actionValue = this.actionValue + increment;
	}
	
	//Method used in other classes to control if the player can do the action
	protected Response checkBanInPlayer() {
		if(!player.canPlay())
			return Response.CANNOT_PLAY;
		return Response.SUCCESS;
	}
	
}
