package it.polimi.ingsw.ps42.message;

import java.util.List;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.Printable;

public class FinalRequest extends CardRequest{

	public FinalRequest(Card card, List<Integer> possibleChoiceIndex, List<Printable> possibleChoice) {
		super(card, possibleChoiceIndex, possibleChoice);
	}

	@Override
	public void apply() {
		card.enableFinalEffect(possibleChoiceIndex.get(userChoice));
	}

}
