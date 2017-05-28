package it.polimi.ingsw.ps42.model.request;

import java.util.List;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.Printable;

public class PayRequest extends CardRequest{
	
	public PayRequest(Card card, List<Integer> possibleChoiceIndex, List<Printable> possibleChoice) {
		super(card, possibleChoiceIndex, possibleChoice);
	}
	
	@Override
	public void apply() {
		card.payCard(userChoice);
	}
}
