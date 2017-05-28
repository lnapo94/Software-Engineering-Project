package it.polimi.ingsw.ps42.model.request;

import java.util.List;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.Printable;

public class ImmediateRequest extends CardRequest{

	public ImmediateRequest(Card card, List<Integer> possibleChoiceIndex, List<Printable> possibleChoice) {
		super(card, possibleChoiceIndex, possibleChoice);
	}

	@Override
	public void apply() {
		// TODO Auto-generated method stub
		
	}

}
