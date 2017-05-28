package it.polimi.ingsw.ps42.model.action;

import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.enumeration.Response;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.MarketPosition;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public class MarketAction extends Action {
	
	private StaticList<MarketPosition> tablePosition;
	private int positionInTableList;
	
	public MarketAction(ActionType type, Familiar familiar, StaticList<MarketPosition> tablePosition, int positionInTableList) throws NotEnoughResourcesException{
		//Constructor for normal action
		super(type, familiar);
		this.tablePosition = tablePosition;
		this.positionInTableList = positionInTableList;
	}
	
	public MarketAction(ActionType type, Player player, StaticList<MarketPosition> tablePosition, int positionInTableList){
		//Constructor for bonus action 
		super(type, player, 1);
		this.tablePosition = tablePosition;
		this.positionInTableList = positionInTableList;
	}
	
	private boolean banCheck(){		//Checks if the player has a Market ban or can play
		return player.canPlay() && player.canStayInMarket();
	}
	
	@Override
	public Response checkAction() {
		
		if(banCheck() && isFree()  ){	//Checks if the player has ban or if the position is occupied
			if(actionValue >= tablePosition.get(positionInTableList).getLevel()) {
				//Checks if the action has a sufficient level for the position
				return Response.SUCCESS;
			}
			else {
				return Response.LOW_LEVEL;
			}
		}
		return Response.CANNOT_PLAY;
	}

	private boolean isFree(){
		return this.tablePosition.get(positionInTableList).isEmpty(); //TO-DO: controllare se il giocatore ha il bonus per le posizioni già occupate
	}
	
	@Override
	public void doAction() throws FamiliarInWrongPosition {
		if(super.familiar != null){			
			//If is a normal action: set the familiar (this will automatically add the position bonuses)
			
			tablePosition.get(positionInTableList).setFamiliar(familiar);

		}
		else{
			//If is a bonus action, apply the effect and the money malus directly to the player 
			Obtain bonus = tablePosition.get(positionInTableList).getBonus();
			bonus.enableEffect(player);		
		}
		
		player.synchResource();  	 //Synchronize the resources in player
	}	

}
