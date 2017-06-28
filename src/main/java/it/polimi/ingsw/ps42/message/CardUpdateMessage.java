package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;

/**
 * Message used to notify the movement of a Card (A player taked a card from a tower)
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class CardUpdateMessage extends Message{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1778540813432689685L;
	private ActionType type;
	private int position;
	
	/**
	 * Constructor of this message
	 * 
	 * @param playerID		The interested player
	 * @param type			The type of Action (TAKE_GREEN, TAKE_YELLOW, TAKE_BLUE, TAKE_VIOLET)
	 * @param position		The position from what the card was taken
	 */
	public CardUpdateMessage(String playerID, ActionType type, int position) {
		super(playerID);
		this.type = type;
		this.position = position;
	}
	
	/**
	 * 
	 * @return	The position in this message
	 */
	public int getPosition() {
		return position;
	}
	
	/**
	 * 
	 * @return	The type of Action in this message
	 */
	public ActionType getType() {
		return type;
	}
	
	/**
	 * Method used to visit this message
	 */
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
