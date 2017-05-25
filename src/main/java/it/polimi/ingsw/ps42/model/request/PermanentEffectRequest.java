package it.polimi.ingsw.ps42.model.request;

import java.util.List;

import it.polimi.ingsw.ps42.model.Card;

public class PermanentEffectRequest extends Request {

	private int choice;
	private List<Integer> possibleChoice;
	
	public PermanentEffectRequest(Card card, List<Integer> possibleChoice) {
		super(card);
		this.possibleChoice=possibleChoice;
	}

	@Override
	public void apply() {
		// TODO Auto-generated method stub
		
	}
	public void setChoice(int choice) {
		this.choice = choice;
	}
	public int getChoice() {
		return choice;
	}
}
