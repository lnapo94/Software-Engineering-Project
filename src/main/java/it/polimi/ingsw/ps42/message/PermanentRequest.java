package it.polimi.ingsw.ps42.message;

import java.util.List;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.Printable;
import it.polimi.ingsw.ps42.model.player.Player;

public class PermanentRequest extends CardRequest{
	//Used to know which permanent card effect player wants to enable

	/**
	 * 
	 */
	private static final long serialVersionUID = 8250539263820109345L;

	public PermanentRequest(String playerID, Card card, List<Integer> possibleChoiceIndex, List<Printable> possibleChoice) {
		super(playerID, card, possibleChoiceIndex, possibleChoice);
	}

	@Override
	public void apply(Player player) {
		card.enablePermanentEffect(possibleChoiceIndex.get(userChoice), player);
	}

	@Override
	public void accept(Visitor v) {
		//Method used to start the visit
		v.visit(this);
	}

}
