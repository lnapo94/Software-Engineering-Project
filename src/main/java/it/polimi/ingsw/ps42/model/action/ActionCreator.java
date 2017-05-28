package it.polimi.ingsw.ps42.model.action;

import it.polimi.ingsw.ps42.model.Table;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;

public class ActionCreator {
	//Factory of Action, gives methods to build different kind of Action
	Action action;
	
	public ActionCreator(Player player, Table table, PlayerMove playerMove, int actionValue) {
		//Create the static variable for the action
		ActionType type = playerMove.getActionType();
		Familiar familiar = player.getFamiliar(playerMove.getFamiliarColor());
		
		//Create the action, from the type of action selected
		switch(type) {
		case TAKE_GREEN :
			action = new TakeCardAction(type, player, table.getGreenTower(), playerMove.getPosition(), actionValue);
			break;
			
		case TAKE_YELLOW :
			action = new TakeCardAction(type, player, table.getYellowTower(), playerMove.getPosition(), actionValue);
			break;
			
		case TAKE_BLUE :
			action = new TakeCardAction(type, player, table.getBlueTower(), playerMove.getPosition(), actionValue);
			break;
		
		case TAKE_VIOLET :
			action = new TakeCardAction(type, player, table.getVioletTower(), playerMove.getPosition(), actionValue);
			break;
		
		case YIELD :
			action = new YieldAction(type, player, table.getOtherYield(), table.getFirstYield(), actionValue);
			break;
		
		case PRODUCE :
			action = new ProductAction(type, player, table.getOtherProduct(), table.getFirstProduct(), actionValue);
			break;
		
		case MARKET :
			action = new MarketAction(type, player, table.getMarket(), playerMove.getPosition());
			break;
		
		case COUNCIL :
			action = new CouncilAction(type, player, table.getFreeCouncilPosition());
			break;
			
		default :
			throw new IllegalArgumentException("Argument in ActionCreator is wrong");			
		}
	}
	
	public Action getCreatedAction() {
		return action;
	}
}
