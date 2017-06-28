package it.polimi.ingsw.ps42.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.Printable;
import it.polimi.ingsw.ps42.model.player.Player;

/**
 * Class used to ask a request to the player, e.g. choose between
 * two effects or two costs or if the player wants to pay for an effect
 * @author Luca Napoletano, Claudio Montanari
 *
 */
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
	
	/**
	 * Constructor of the message
	 * 
	 * @param playerID				The interested player ID
	 * @param card					The interested card
	 * @param possibleChoiceIndex	The arraylist used to convert the showed choice with the real card choice
	 * @param possibleChoice		The possible choice through which the player can choose
	 */
	public CardRequest(String playerID, Card card, List<Integer> possibleChoiceIndex, List<Printable> possibleChoice) {
		super(playerID);
		this.card = card;
		this.possibleChoice = possibleChoice;
		this.possibleChoiceIndex = possibleChoiceIndex;
	}
	
	/**
	 * Private method used to copy the passed List, used to guarantee the unchangeability
	 * of the List
	 * 
	 * @param source	The list of printable to copy
	 * @return			The copied list
	 */
	private List<Printable> copy(List<Printable> source) {
		List<Printable> temp = new ArrayList<>();
		for(Printable printable : source)
			temp.add(printable);
		return temp;
	}

	/**
	 * Return a copy of the possiblechoice list
	 * 
	 * @return	The possibleChoice array
	 */
	public List<Printable> showChoice() {
		return copy(possibleChoice);
	}

	/**
	 * Method used in the view to set the choice
	 * 
	 * @param choice	The player's choice
	 */
	public void setChoice(int choice) {
		this.userChoice = choice;
	}
	
	/**
	 * Method used in GameLogic to know the choice
	 * 
	 * @return	The player's choice
	 */
	public int getChoice() {
		return this.userChoice;
	}
	
	/**
	 * Method used to get the array of index of choice
	 * 
	 * @return	List of possibleChoiceIndex
	 */
	public List<Integer> getPossibleChoiceIndex() {
		return possibleChoiceIndex;
	}

	/**
	 * Abstract method used in the inherited classes (Such as PayRequest, ImmediateRequest, FinalRequest, PermanentRequest...)
	 * 
	 * @param player		The interested player
	 */
	public abstract void apply(Player player);
}
