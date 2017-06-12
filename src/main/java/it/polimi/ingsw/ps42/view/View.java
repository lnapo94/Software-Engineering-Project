package it.polimi.ingsw.ps42.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import it.polimi.ingsw.ps42.message.BonusBarMessage;
import it.polimi.ingsw.ps42.message.LeaderCardMessage;
import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.message.visitorPattern.ViewVisitor;
import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.Table;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;
import it.polimi.ingsw.ps42.model.player.BonusBar;
import it.polimi.ingsw.ps42.model.player.Player;

public class View extends Observable implements Observer {

	
	private Table table; 
	private Player player;
	private List<Player> otherPlayers; 
	private Visitor viewVisitor;
	
	public View() {
		
		this.viewVisitor = new ViewVisitor(this);
		this.otherPlayers = new ArrayList<>();
	}
	
	public void addPlayer(String playerID){
		
		this.player = new Player(playerID);
	}
	
	public void createTable(List<String> playersID){
		
		//Method to be called from the ViewVisitor when receive the players of the game
		if(playersID.contains(player.getPlayerID())){
			
			playersID.remove(player.getPlayerID());
			//Create the others players
			for (String playerID : playersID) {
				otherPlayers.add(new Player(playerID));
			}
			//Add the players to the Table
			if(playersID.size() == 1)
				this.table = new Table( player, otherPlayers.get(0));
			else if(playersID.size() == 2)
				this.table = new Table( player, otherPlayers.get(0), otherPlayers.get(1));
			else if(playersID.size() == 3)
				this.table = new Table( player,  otherPlayers.get(0), otherPlayers.get(1), otherPlayers.get(2));
			
		}
	}
	
	public void askBonusBar(List<BonusBar> bonusBar, BonusBarMessage message ){
		
	}
	
	public void askLeaderCard(List<LeaderCard> bonusBar, LeaderCardMessage message ){
		
	}
	
	//SETTER FOR TABLE BANS
	public void setFirstBan(Effect firstBan){
		if(firstBan != null)
			this.table.addFirstBan(firstBan);
	}
	
	public void setSecondBan(Effect secondBan){
		if(secondBan != null)
			this.table.addSecondBan(secondBan);
	}
	
	public void setThirdBan(Effect thirdBan){
		if(thirdBan != null)
			this.table.addThirdBan(thirdBan);
	}
	
	public void setOrangeDice(int value){
		
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
		
		if (arg1 instanceof Message) {
			Message message = (Message) arg1;
			message.accept(viewVisitor);
		}
	}

}
