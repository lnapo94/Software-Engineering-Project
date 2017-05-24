package it.polimi.ingsw.ps42.model.action;

import java.util.ArrayList;

import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.Position;

public class ActionCreator {
		//Factory of Action, gives methods to build different kind of Action
	
	public Action actionFactory(ActionType type, Familiar familiar, ArrayList<Position> tablePosition, int positionInTableList){
		//Returns the required action
		return null;
	}
	
	public Action actionFactory(ActionType type, Player player, ArrayList<Position> tablePosition, int positionInTableList){
		//Returns the required action, usually called when a bonus action occurs (no familiar)
		return null;
	}
}
