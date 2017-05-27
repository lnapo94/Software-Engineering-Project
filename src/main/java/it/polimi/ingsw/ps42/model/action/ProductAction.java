package it.polimi.ingsw.ps42.model.action;

import java.util.List;

import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.YieldAndProductPosition;

public class ProductAction extends Action {
	
	private List<YieldAndProductPosition> tablePosition;
	private YieldAndProductPosition firstPosition;
	
	
	public ProductAction(ActionType type, Familiar familiar, List<YieldAndProductPosition> tablePosition, YieldAndProductPosition firstPosition){
		//Constructor for normal action
		super(type, familiar);
		this.tablePosition = tablePosition;
		this.firstPosition = firstPosition;
	}
	public ProductAction(ActionType type, Player player, List<YieldAndProductPosition> tablePosition, int actionValue){
		//Constructor for bonus action
		super(type, player, actionValue);
		this.tablePosition = tablePosition;
		this.firstPosition = firstPosition;
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
