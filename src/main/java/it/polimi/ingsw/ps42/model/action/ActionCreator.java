package it.polimi.ingsw.ps42.model.action;

import it.polimi.ingsw.ps42.model.enumeration.ActionType;

public class ActionCreator {
		//Factory of Action, gives methods to build different kind of Action
	
	public Action actionFactory(ActionType type, Player player, Familiar familiar, ArrayList<Position> tableposition){
		//Returns the required action
		
	}
	
	public Action actionFactory(ActionType type, Player player, ArrayList<Position> tableposition){
		//Returns the required action, usually called when a bonus action occurs (no familiar)
		
	}
}
