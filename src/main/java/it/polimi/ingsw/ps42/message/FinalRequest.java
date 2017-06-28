package it.polimi.ingsw.ps42.message;

import java.util.List;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.Printable;
import it.polimi.ingsw.ps42.model.player.Player;

/**
 * Message used to know which final card effects the player wants to enable
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class FinalRequest extends CardRequest{
	//Used to know which final card effect player wants to enable

	/**
	 * 
	 */
	private static final long serialVersionUID = 2486050455783188238L;

	public FinalRequest(String playerID, Card card, List<Integer> possibleChoiceIndex, List<Printable> possibleChoice) {
		super(playerID, card, possibleChoiceIndex, possibleChoice);
	}

	@Override
	public void apply(Player player) {
		
		card.enableFinalEffect(possibleChoiceIndex.get(userChoice), player);
	}

	/**
	 * Method used to visit this message
	 */
	@Override
	public void accept(Visitor v) {
		//Method used to start the visit
		v.visit(this);
	}

}
