package it.polimi.ingsw.ps42.model.action;

import java.util.List;
import java.util.Observable;

import it.polimi.ingsw.ps42.message.CardRequest;
import it.polimi.ingsw.ps42.message.CouncilRequest;
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

/**
 * Class for basic action, requires the implementation of checkAction,
 * doAction, createRequest
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public abstract class Action extends Observable{	
	
	private ActionType type;
	private int payedSlave;
	
	private int slaveToPay;
	
	protected Familiar familiar;
	protected Player player;
	protected Packet discount;
	
	/**
	 * 	actionValue is the current value of the action. We have two possibilities for the
	 *	action: a "normal" action, in which the player move the familiar in one position
	 *	and a "bonus" action, in which the player select only a position, without positioning
	 *	any familiar. In this last case, we have an action value, taken from the card, and we
	 *	haven't a value taken from the familiar. So we need a variable to have the correct
	 *	value, to do some control, to rise a bonus action and so on...
	 *	Then we decide to use this variable also for the "normal" action with familiar, and we
	 *	set it while the action is constructed
	 *	
	 *  The increment the player wants to do is in familiar.getIncrement, the others kinds
	 *  of increments are stored in actionValue
	 */
	protected int actionValue;

	/**
	 * Constructor for normal action, player is get from familiar
	 * 
	 * @param type							The type of the action
	 * @param familiar						The familiar the player wants to move
	 * @throws NotEnoughResourcesException	Thrown if the player hasn't enough resources
	 */
	public Action(ActionType type, Familiar familiar) throws NotEnoughResourcesException{
		
		this.type=type;
		this.familiar=familiar;
		this.player=familiar.getPlayer();
		
		//Control if player has enough slave to increment his familiar
		
		this.slaveToPay = familiar.getIncrement() * player.getDivisory();

		this.actionValue = familiar.getIncrement() + familiar.getValue();
	}
	
	/**
	 * Constructor for bonus action (no familiar involved, so requires the player) 
	 * 
	 * @param type								The type of the bonus action
	 * @param player							The interested player
	 * @param actionValue						The value of the bonus action
	 * @param actionIncrement					The increment the player wants
	 * @throws NotEnoughResourcesException		Thrown if the player hasn't enough resources
	 */
	public Action(ActionType type, Player player, int actionValue, int actionIncrement) throws NotEnoughResourcesException {
		
		
		this.type = type;
		this.player = player;
		this.actionValue = actionValue;
		
		//Check player slave resources
		this.slaveToPay = actionIncrement;
		this.actionValue += actionIncrement;
	}
	
	/**
	 * Private method used to pay slaves to increase an action
	 * @param slaveToPay						The number of slave the player wants to use
	 * @throws NotEnoughResourcesException		Thrown if the player hasn't enough resources
	 */
	public void paySlave() throws NotEnoughResourcesException {
		
		Packet slavePacket = new Packet();
		slavePacket.addUnit(new Unit(Resource.SLAVE, slaveToPay));
		player.decreaseResource(slavePacket);
		this.payedSlave = slaveToPay;
	}
	
	/**
	 * Method used to roll back an action when there is a problem with an Action, such as
	 * the player can't play or the player can't afford a card cost
	 */
	public void rollBackAction() {
		Packet slavePacket = new Packet();
		slavePacket.addUnit(new Unit(Resource.SLAVE, this.payedSlave));
		player.increaseResource(slavePacket);
		removeIncrement();
		this.payedSlave = 0;
	}
	
	/**
	 * Method used to remove the familiar increment when there is a problem with this action, in other
	 * words when the action response is FAILURE or CANNOT_PLAY
	 */
	public void removeIncrement() {
		if(familiar != null)
			familiar.resetIncrement();
		player.synchResource();
	}
	
	/**
	 * Does all the required checks before the action is applicated 
	 * @return	A response which represents the Success/Failure of the method: SUCCESS for a successful control, FAILURE for a generic problem, LOW_LEVEL if the player hasn't enough resources, CANNOT_PLAY if the player can not play this action
	 */
	public abstract Response checkAction();
	
	public abstract void doAction() throws FamiliarInWrongPosition;		//Apply the player action 

	public boolean playerHasRequests() {
		//Control if player has some request, and in that case notify to the view
		
		List<CardRequest> requests = player.getRequests();
		
		if(requests.isEmpty())
			return false;
		
		notifyChangesToPlayer(requests);
		return true;
	}
	
	/**
	 * Control if player has some council requests
	 * 
	 * @return	True if the player has some council requests to satisy, otherwise False
	 */
	public boolean playerHasCouncilRequests() {
		
		List<CouncilRequest> councilRequests = player.getCouncilRequests();
		if(councilRequests.isEmpty())
			return false;
		
		notifyChangesToPlayer(councilRequests);
		return true;
	}
	
	/**
	 * Method used to notify the player of some changes
	 * 
	 * @param list		List of Generic Object to send to the player
	 */
	private void notifyChangesToPlayer(List<?> list) {
		for(Object object : list) {
			setChanged();
			notifyObservers(object);
		}
	}
	
	/**
	 * Checks if the player has some increase effects active and apply them
	 */
	protected void checkIncreaseEffect(){		
		List<IncreaseAction> playerIncreaseAction = player.getIncreaseEffect();
		
		for (IncreaseAction increaseAction : playerIncreaseAction) {
			increaseAction.activeIncrease(this);
		}
		
	}
	
	/**
	 * Adds a discount at the cost of the action
	 * 
	 * @param discount		A discount to apply to the action
	 */
	public void addDiscount(Packet discount) {
		if(this.discount==null)
			this.discount = discount;
		else{
			for (Unit unit : discount) {
				this.discount.addUnit(unit);
			}	
		}
			
	}
	
	/**
	 * Getter for the type of action
	 * @return	The type of action
	 */
	public ActionType getType() {
		return type;
	}
	
	/**
	 * Getter for the value of the action
	 * @return	The value of the action
	 */
	public int getActionValue() {
		return actionValue;
	}
	
	/**
	 * Method used to add the increment chosen by the player to the action value
	 * @param increment
	 */
	public void addIncrement(int increment){
		
		this.actionValue = this.actionValue + increment;
	}
	
	/**
	 * Method used in other classes to control if the player can do the action
	 * @return	SUCCESS if the player can player, CANNOT_PLAY if the player can't
	 */
	protected Response checkBanInPlayer() {
		if(!player.canPlay())
			return Response.CANNOT_PLAY;
		return Response.SUCCESS;
	}
	
	/**
	 * Method used to know if the action is really a bonus action. If the player is doing a 
	 * bonus action, he doesn't use a player
	 * 
	 * @return True if the familiar is null, otherwise False
	 */
	public boolean isBonusAction() {
		return this.familiar == null;
	}
	
}
