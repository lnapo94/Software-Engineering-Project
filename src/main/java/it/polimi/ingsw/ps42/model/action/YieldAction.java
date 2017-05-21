package it.polimi.ingsw.ps42.model.action;

import java.util.ArrayList;

import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.position.Position;

public class YieldAction extends Action {

	
	public YieldAction(ActionType type, Familiar familiar, ArrayList<Position> tablePosition, int positionInTableList){
		//Constructor for normal action
	}
	public YieldAction(ActionType type, Player player, ArrayList<Position> tablePosition, int positionInTableList){
		//Constructor for bonus action
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
