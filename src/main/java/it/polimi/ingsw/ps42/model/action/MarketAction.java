package it.polimi.ingsw.ps42.model.action;

import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.Response;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.MarketPosition;

public class MarketAction extends Action {
	
	private StaticList<MarketPosition> tablePosition;
	private int positionInTableList;
	
	public MarketAction(ActionType type, Familiar familiar, StaticList<MarketPosition> tablePosition, int positionInTableList) throws NotEnoughResourcesException{
		//Constructor for normal action
		super(type, familiar);
		this.tablePosition = tablePosition;
		this.positionInTableList = positionInTableList;
	}
	
	public MarketAction(ActionType type, Player player, StaticList<MarketPosition> tablePosition, int positionInTableList){
		//Constructor for bonus action 
		super(type, player, 1);
		this.tablePosition = tablePosition;
		this.positionInTableList = positionInTableList;
	}
	
	private boolean banCheck(){		//Checks if the player has a Market ban
		return player.canPlay();
	}
	
	@Override
	public Response checkAction() {
		// TODO Auto-generated method stub
		return Response.FAILURE;
	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		
	}	

}
