package it.polimi.ingsw.ps42.message.leaderRequest;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;
import it.polimi.ingsw.ps42.model.player.Player;

/**
 * Class which represent a familiar request of a leader card. This request ask 
 * to the player which familiar he wants to increase
 * @author luca
 *
 */
public class LeaderFamiliarRequest extends LeaderRequest {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5337440997852193033L;
	private FamiliarColor color;

	public LeaderFamiliarRequest(String playerID, LeaderCard card) {
		super(playerID, card);
	}

	/**
	 * Method used to apply the request
	 */
	@Override
	public void apply(Player player) {
		if(color != null)
			card.enableOnceARoundEffect(player, color);
	}
	
	/**
	 * Setter for the color of the familiar the player wants to increas
	 * @param color		The chosen color
	 */
	public void setFamiliarColor(FamiliarColor color) {
		this.color = color;
	}
	
	/**
	 * Method used to know which color the player has chosen
	 * @return			The chosen color
	 */
	public FamiliarColor getColor() {
		return this.color;
	}

	/**
	 * Method used to start the visit of this message
	 */
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
