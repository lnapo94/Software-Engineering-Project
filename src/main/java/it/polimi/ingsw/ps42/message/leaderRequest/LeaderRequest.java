package it.polimi.ingsw.ps42.message.leaderRequest;

import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;
import it.polimi.ingsw.ps42.model.player.Player;

/**
 * Abstract class which represent a generic leader card request to send to the player
 * It is used to apply the Strategy Pattern
 * @author luca
 *
 */
public abstract class LeaderRequest extends Message{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8091197061220579813L;
	protected LeaderCard card;
	
	public LeaderRequest(String playerID, LeaderCard card) {
		super(playerID);
		this.card = card;
	}

	public abstract void apply(Player player);
	
	@Override
	public abstract void accept(Visitor v);

}
