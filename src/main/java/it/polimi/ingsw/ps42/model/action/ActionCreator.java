package it.polimi.ingsw.ps42.model.action;

import it.polimi.ingsw.ps42.model.Table;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;

public class ActionCreator {
	//Factory of Action, gives methods to build different kind of Action
	Action action;
	
	public ActionCreator(Player player, Table table, PlayerMove playerMove) {
		//Create the static variable for the action
		ActionType type = playerMove.getActionType();
		Familiar familiar = player.getFamiliar(playerMove.getFamiliarColor());
		
		//Create the action, from the type of action selected
		switch(type) {
		case TAKE_GREEN :
			action = new TakeCardAction(type, familiar, table.getGreenTower(), playerMove.getPosition());
			break;
			
		case TAKE_YELLOW :
			action = new TakeCardAction(type, familiar, table.getYellowTower(), playerMove.getPosition());
			break;
			
		case TAKE_BLUE :
			action = new TakeCardAction(type, familiar, table.getBlueTower(), playerMove.getPosition());
			break;
		
		case TAKE_VIOLET :
			action = new TakeCardAction(type, familiar, table.getVioletTower(), playerMove.getPosition());
			break;
		
		case YIELD :
			action = new YieldAction(type, familiar, table.getOtherYield(), table.getFirstYield());
			break;
		
		case PRODUCE :
			action = new ProductAction(type, familiar, table.getOtherProduct(), table.getFirstProduct());
			break;
		
		case MARKET :
			action = new MarketAction(type, familiar, table.getMarket(), playerMove.getPosition());
			break;
		
		case COUNCIL :
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
