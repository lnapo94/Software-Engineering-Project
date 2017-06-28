package it.polimi.ingsw.ps42.message;

import java.util.List;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.Printable;
import it.polimi.ingsw.ps42.model.player.Player;

/**
 * Class used to know which immediate card effect player wants to enable
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class ImmediateRequest extends CardRequest{
	//Used to know which immediate card effect player wants to enable

	/**
	 * 
	 */
	private static final long serialVersionUID = -9074379648212729652L;

	public ImmediateRequest(String playerID, Card card, List<Integer> possibleChoiceIndex, List<Printable> possibleChoice) {
		super(playerID, card, possibleChoiceIndex, possibleChoice);
	}

	@Override
	public void apply(Player player) {
		card.enableImmediateEffect(possibleChoiceIndex.get(userChoice), player);
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
