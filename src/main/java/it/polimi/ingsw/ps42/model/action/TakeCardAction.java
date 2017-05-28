package it.polimi.ingsw.ps42.model.action;


import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.Response;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.TowerPosition;

public class TakeCardAction extends Action{
	
	private StaticList<TowerPosition> tablePosition;
	private int positionInTableList;

	public TakeCardAction(ActionType type, Familiar familiar, StaticList<TowerPosition> tablePosition, int positionInTableList) throws NotEnoughResourcesException{
		//Constructor for normal action
		super(type, familiar);
		this.tablePosition = tablePosition;
		this.positionInTableList = positionInTableList;
	}
	public TakeCardAction(ActionType type, Player player, StaticList<TowerPosition> tablePosition, int positionInTableList, int actionValue){
		//Constructor for bonus action
		super(type, player, actionValue);
		this.tablePosition = tablePosition;
		this.positionInTableList = positionInTableList;
	}
	
	@Override
	public Response checkAction() {
		
		//Check if player can play
		if(!player.canPlay())
			return Response.CANNOT_PLAY;
		
		//Check if there is a card in position and if the player
		//can position a familiar in tower
		TowerPosition position = tablePosition.get(positionInTableList);
		if(!position.hasCard() || (familiar != null && position.getLevel() > familiar.getIncrement() + actionValue))
			return Response.FAILURE;
		
		for(TowerPosition tower : tablePosition) {
			if(tower.getFamiliar().getPlayer() == this.player){
				
			}
				
		}
		
		checkIncreaseEffect();
		return Response.FAILURE;
	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub		
	}

}
