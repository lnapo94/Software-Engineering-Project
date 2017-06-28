package it.polimi.ingsw.ps42.message;

import java.util.List;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.exception.WrongChoiceException;
import it.polimi.ingsw.ps42.model.player.BonusBar;

/**
 * Class used to send the bonus bars from the controller to the view
 * @author luca
 *
 */
public class BonusBarMessage extends Message{

	/* Message to ask the View to choice between the available BonusBar
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 120657403510463998L;
	private List<BonusBar> availableBonusBar;
	private int choice;
	
	/**
	 * Constructor of the message
	 * 
	 * @param playerID				The interested player
	 * @param availableBonusBar		The available bonus bars
	 */
	public BonusBarMessage(String playerID, List<BonusBar> availableBonusBar) {
	
		super(playerID);
		this.availableBonusBar = availableBonusBar;
	}
	
	
	/**
	 * Getter of the bonus bars
	 * 
	 * @return	A list of available bonus bars
	 */
	public List<BonusBar> getAvailableBonusBar() {
		return availableBonusBar;
	}
	
	/**
	 * Method used to set the choice of the player
	 * 
	 * @param choice					The player's choice
	 * @throws WrongChoiceException		Thrown if the player's choice is wrong
	 */
	public void setChoice(int choice) throws WrongChoiceException {
		if(choice < availableBonusBar.size() && choice>=0 ){			//Check if is a valid choice
			this.choice = choice;
		}
		else throw new WrongChoiceException("Choice for the BonusBar is not valid");
	}
	
	/**
	 * The getter of the choice in the message
	 * @return	The player's choice
	 */
	public int getChoice() {
		return choice;
	}
	
	/**
	 * Method used to visit the message
	 */
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
