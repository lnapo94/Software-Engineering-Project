package it.polimi.ingsw.ps42.message;

import java.util.List;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.Printable;
import it.polimi.ingsw.ps42.model.player.Player;

public class ImmediateRequest extends CardRequest{
	//Used to know which immediate card effect player wants to enable

	public ImmediateRequest(String playerID, Card card, List<Integer> possibleChoiceIndex, List<Printable> possibleChoice) {
		super(playerID, card, possibleChoiceIndex, possibleChoice);
	}

	@Override
	public void apply(Player player) {
		card.enableImmediateEffect(possibleChoiceIndex.get(userChoice), player);
	}

	@Override
	public void accept(Visitor v) {
		//Method used to start the visit
		v.visit(this);
	}

}
