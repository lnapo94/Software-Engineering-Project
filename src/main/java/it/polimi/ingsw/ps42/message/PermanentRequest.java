package it.polimi.ingsw.ps42.message;

import java.util.List;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.Printable;

public class PermanentRequest extends CardRequest{

	public PermanentRequest(Card card, List<Integer> possibleChoiceIndex, List<Printable> possibleChoice) {
		super(card, possibleChoiceIndex, possibleChoice);
	}

	@Override
	public void apply() {
		card.enablePermanentEffect(possibleChoiceIndex.get(userChoice));
	}

	@Override
	public void accept(Visitor v) {
		// TODO Auto-generated method stub
		
	}

}
