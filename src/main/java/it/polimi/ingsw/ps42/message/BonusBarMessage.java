package it.polimi.ingsw.ps42.message;

import java.util.List;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.exception.WrongChoiceException;
import it.polimi.ingsw.ps42.model.player.BonusBar;

public class BonusBarMessage extends Message{

	/* Message to ask the View to choice between the available BonusBar
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 120657403510463998L;
	private List<BonusBar> availableBonusBar;
	private int choice;
	
	public BonusBarMessage(String playerID, List<BonusBar> availableBonusBar) {
	
		super(playerID);
		this.availableBonusBar = availableBonusBar;
	}
	
	public List<BonusBar> getAvailableBonusBar() {
		return availableBonusBar;
	}
	
	public void setChoice(int choice) throws WrongChoiceException {
		if(choice < availableBonusBar.size() && choice>=0 ){			//Check if is a valid choice
			this.choice = choice;
		}
		else throw new WrongChoiceException("Choice for the BonusBar is not valid");
	}
	
	public int getChoice() {
		return choice;
	}
	
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
