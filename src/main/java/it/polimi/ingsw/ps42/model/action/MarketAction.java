package it.polimi.ingsw.ps42.model.action;

import java.util.ArrayList;

import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.Position;

public class MarketAction extends Action {

	
	public MarketAction(ActionType type, Familiar familiar, ArrayList<Position> tablePosition, int positionInTableList){
		//Constructor for normal action
	}
	
	public MarketAction(ActionType type, Player player, ArrayList<Position> tablePosition, int positionInTableList){
		//Constructor for bonus action 
	}
	
	private boolean banCheck(){		//Checks if the player has a Market ban
		
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
