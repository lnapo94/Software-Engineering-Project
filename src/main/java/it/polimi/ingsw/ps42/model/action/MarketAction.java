package it.polimi.ingsw.ps42.model.action;

import java.util.List;

import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.Position;

public class MarketAction extends Action {

	
	public MarketAction(ActionType type, Familiar familiar, List<Position> tablePosition, int positionInTableList){
		//Constructor for normal action
		super(type, familiar, tablePosition, positionInTableList);
	}
	
	public MarketAction(ActionType type, Player player, List<Position> tablePosition, int positionInTableList){
		//Constructor for bonus action 
		super(type, player, tablePosition, positionInTableList);
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
