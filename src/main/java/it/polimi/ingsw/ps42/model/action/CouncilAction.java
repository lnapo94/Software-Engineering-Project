package it.polimi.ingsw.ps42.model.action;

import java.util.List;

import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.CouncilPosition;

public class CouncilAction extends Action {

	//Private Variables
	private List<CouncilPosition> tablePosition;

	public CouncilAction(ActionType type, Familiar familiar, List<CouncilPosition> tablePosition){
		//Constructor for normal action
		super(type, familiar);
		this.tablePosition = tablePosition;
	}
	
	public CouncilAction(ActionType type, Player player, List<CouncilPosition> tablePosition, int actionValue){
		//Constructor for bonus action
		super(type, player, actionValue);
		this.tablePosition = tablePosition;
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
