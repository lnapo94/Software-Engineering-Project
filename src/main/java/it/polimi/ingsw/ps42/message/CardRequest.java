package it.polimi.ingsw.ps42.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.Printable;
import it.polimi.ingsw.ps42.model.player.Player;

public abstract class CardRequest extends Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4089318481414975208L;

	/*Class for asking a request to the player, 
	 * e.g. choose between two effects or two costs or
	 * if wants to pay for a effect
	 */
	//List of possible choice to show to the user
	protected List<Printable> possibleChoice;
	
	//Possible choice index in card
	protected List<Integer> possibleChoiceIndex;
	
	//The user's choice
	protected int userChoice;
	
	protected Card card;
	
	public CardRequest(String playerID, Card card, List<Integer> possibleChoiceIndex, List<Printable> possibleChoice) {
		super(playerID);
		this.card = card;
		this.possibleChoice = possibleChoice;
		this.possibleChoiceIndex = possibleChoiceIndex;
	}
	
	private List<Printable> copy(List<Printable> source) {
		List<Printable> temp = new ArrayList<>();
		for(Printable printable : source)
			temp.add(printable);
		return temp;
	}

	public List<Printable> showChoice() {
		return copy(possibleChoice);
	}

	public void setChoice(int choice) {
		this.userChoice = choice;
	}
	
	public int getChoice() {
		return this.userChoice;
	}
	
	public List<Integer> getPossibleChoiceIndex() {
		return possibleChoiceIndex;
	}

	public abstract void apply(Player player);
}
