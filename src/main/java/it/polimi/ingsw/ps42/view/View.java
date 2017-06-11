package it.polimi.ingsw.ps42.view;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import it.polimi.ingsw.ps42.message.BonusBarMessage;
import it.polimi.ingsw.ps42.message.LeaderCardMessage;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;
import it.polimi.ingsw.ps42.model.player.BonusBar;

public class View extends Observable implements Observer {

	
	//private TableView table; da implementare
	//private PlayerView player; da implementare
	
	public void askBonusBar(List<BonusBar> bonusBar, BonusBarMessage message ){
		
	}
	
	public void askLeaderCard(List<LeaderCard> bonusBar, LeaderCardMessage message ){
		
	}
	
	public void setBan(List<Effect> banList){
		
	}
	
	public void setDice(List<Integer> diceValue){
		
	}
	
	public void setResources(HashMap<Resource, Integer> resources, String playerID){
		
	}
	
	public void setGreenCards(List<Card> cards){
		
	}
	
	public void setYellowCards(List<Card> cards){
		
	}
	
	public void setFamiliar(String playerID, FamiliarColor color){
		
	}
	
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

}
