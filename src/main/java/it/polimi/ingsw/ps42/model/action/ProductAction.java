package it.polimi.ingsw.ps42.model.action;

import java.util.List;

import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.Response;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.YieldAndProductPosition;

public class ProductAction extends Action {
	
	private List<YieldAndProductPosition> tablePosition;
	private YieldAndProductPosition firstPosition;
	
	
	public ProductAction(ActionType type, Familiar familiar, List<YieldAndProductPosition> tablePosition, YieldAndProductPosition firstPosition) throws NotEnoughResourcesException{
		//Constructor for normal action
		super(type, familiar);
		this.tablePosition = tablePosition;
		this.firstPosition = firstPosition;
	}
	public ProductAction(ActionType type, Player player, List<YieldAndProductPosition> tablePosition, YieldAndProductPosition firstPosition, int actionValue){
		//Constructor for bonus action
		super(type, player, actionValue);
		this.tablePosition = tablePosition;
		this.firstPosition = firstPosition;
	}
	
	@Override
	public Response checkAction() {
		/*
		 * First: Check ban 
		 * Second: Check increase effect
		 * Third: Check the first free position (if the first is occupied check if you are the only familiar of that player, pay attention to neutral familiar)
		 * Fourth: Apply position bonus and malus
		 */
		
		return Response.FAILURE;		
	}

	@Override
	public void doAction() {
		
		/* Zero: Syncronize player resources
		 * First: Take Product cards from player and enable them through the position method 
		 * (the requests are handled by the game logic)
		 */
		
	}

}
