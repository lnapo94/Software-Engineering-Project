package it.polimi.ingsw.ps42.model.action;

import java.util.List;

import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.MarketPosition;

public class MarketAction extends Action {
	
	private StaticList<MarketPosition> tablePosition;
	private int positionInTableList;
	
	public MarketAction(ActionType type, Familiar familiar, StaticList<MarketPosition> tablePosition, int positionInTableList){
		//Constructor for normal action
		super(type, familiar);
		this.tablePosition = tablePosition;
		this.positionInTableList = positionInTableList;
	}
	
	public MarketAction(ActionType type, Player player, StaticList<MarketPosition> tablePosition, int positionInTableList, int actionValue){
		//Constructor for bonus action 
		super(type, player, actionValue);
		this.tablePosition = tablePosition;
		this.positionInTableList = positionInTableList;
	}
	
	private boolean banCheck(){		//Checks if the player has a Market ban
		return player.canPlay();
	}
	
	@Override
	public void checkAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createRequest() {
		// TODO Auto-generated method stub
		
	}
	

}
