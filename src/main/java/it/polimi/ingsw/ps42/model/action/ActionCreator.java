package it.polimi.ingsw.ps42.model.action;

import it.polimi.ingsw.ps42.model.Table;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;

public class ActionCreator {
	//Factory of Action, gives methods to build different kind of Action
	Action action;
	
	public ActionCreator(Player player, Table table, PlayerMove playerMove, int actionValue) throws NotEnoughResourcesException{
		//Create the static variable for the action
		ActionType type = playerMove.getActionType();
		int actionIncrement = playerMove.getIncrementWithSlave();
		Familiar familiar = player.getFamiliar(playerMove.getFamiliarColor());
		if(familiar != null)
			familiar.setIncrement(actionIncrement);
		
		//Create the action, from the type of action selected
		switch(type) {
		case TAKE_GREEN :
			if(familiar == null) 
				action = new TakeCardAction(type, player, table.getGreenTower(), playerMove.getPosition(), actionValue, actionIncrement);
			else
				action = new TakeCardAction(type, familiar, table.getGreenTower(), playerMove.getPosition());
			break;
			
		case TAKE_YELLOW :
			if(familiar == null) 
				action = new TakeCardAction(type, player, table.getYellowTower(), playerMove.getPosition(), actionValue, actionIncrement);
			else
				action = new TakeCardAction(type, familiar, table.getYellowTower(), playerMove.getPosition());
			break;
			
		case TAKE_BLUE :
			if(familiar == null) 
				action = new TakeCardAction(type, player, table.getBlueTower(), playerMove.getPosition(), actionValue, actionIncrement);
			else
				action = new TakeCardAction(type, familiar, table.getBlueTower(), playerMove.getPosition());
			break;
		
		case TAKE_VIOLET :
			if(familiar == null) 
				action = new TakeCardAction(type, player, table.getVioletTower(), playerMove.getPosition(), actionValue, actionIncrement);
			else
				action = new TakeCardAction(type, familiar, table.getVioletTower(), playerMove.getPosition());
			break;
		
		case YIELD :
			if(familiar == null)
				action = new YieldAndProductAction(type, player, table.getOtherYield(), table.getFirstYield(), actionValue, actionIncrement);
			else
				action = new YieldAndProductAction(type, familiar, table.getOtherYield(), table.getFirstYield());
			break;
		
		case PRODUCE :
			if(familiar == null)
				action = new YieldAndProductAction(type, player, table.getOtherProduct(), table.getFirstProduct(), actionValue, actionIncrement);
			else
				action = new YieldAndProductAction(type, familiar, table.getOtherProduct(), table.getFirstProduct());
			break;
		
		case MARKET :
			if(familiar == null)
				action = new MarketAction(type, player, table.getMarket(), playerMove.getPosition(), actionIncrement);
			else
				action = new MarketAction(type, familiar, table.getMarket(), playerMove.getPosition());
			break;
		
		case COUNCIL :
			if(familiar == null)
				action = new CouncilAction(type, player, table.getFreeCouncilPosition(), actionIncrement);
			else
				action = new CouncilAction(type, familiar, table.getFreeCouncilPosition());
			break;
			
		default :
			throw new IllegalArgumentException("Argument in ActionCreator is wrong");			
		}
	}
	
	public Action getCreatedAction() {
		return action;
	}
}
