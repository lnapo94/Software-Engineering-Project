package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.action.ActionPrototype;

public class PlayerToken extends Message{

	/*Message to notify the View that the player has to do an Action,
	 * wraps a Player Move and an Action Prototype for the bonus Actions 
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2557321712632368922L;
	private PlayerMove playerMove;
	private ActionPrototype actionPrototype;
	
	public PlayerToken(String playerID, ActionPrototype actionPrototype ) {
		//Constructor for a bonus Action
		super(playerID);
		this.actionPrototype = actionPrototype;
		this.playerMove = null;
	}
	
	public PlayerToken(String playerID) {
		//Constructor for a normal Action
		this(playerID, null);
	}

	public ActionPrototype getActionPrototype() {
		return actionPrototype;
	}
	
	public PlayerMove getPlayerMove() {
		return playerMove;
	}
	
	public void setPlayerMove(PlayerMove playerMove) {
		this.playerMove = playerMove;
	}
	
	public boolean isBonusAction(){
		return this.actionPrototype != null;
	}
	
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

	
}
