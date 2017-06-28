package it.polimi.ingsw.ps42.message;

import java.util.List;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.exception.WrongChoiceException;
import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;

/**
 * Message used to choose the leader card in the view
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class LeaderCardMessage extends Message{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1791114275385425547L;
	private List<LeaderCard> leaderCardList;
	private int choice;
	
	/**
	 * Constructor of this message
	 * 
	 * @param playerID			The interested player
	 * @param leaderCardList	The available leader cards
	 */
	public LeaderCardMessage(String playerID, List<LeaderCard> leaderCardList) {
		super(playerID);
		this.leaderCardList = leaderCardList;
	}
	
	public List<LeaderCard> getAvailableLeaderCards() {
		return leaderCardList;
	}
	
	public void setChoice(int choice) throws WrongChoiceException {
		if(choice < 0 || choice >= leaderCardList.size())
			throw new WrongChoiceException("Selected choice is not valid");
		this.choice = choice;
	}
	
	public int getChoice() {
		return this.choice;
	}
	
	/**
	 * Method used to visit this message
	 */
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
