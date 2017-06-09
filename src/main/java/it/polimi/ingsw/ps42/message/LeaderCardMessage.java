package it.polimi.ingsw.ps42.message;

import java.util.List;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.exception.WrongChoiceException;

public class LeaderCardMessage extends Message{
	
	private List<Card> leaderCardList;
	private int choice;
	
	public LeaderCardMessage(String playerID, List<Card> leaderCardList) {
		super(playerID);
		this.leaderCardList = leaderCardList;
	}
	
	public List<Card> getAvailableLeaderCards() {
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

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
