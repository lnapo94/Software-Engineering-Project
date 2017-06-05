package it.polimi.ingsw.ps42.model.action;


import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.Response;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.MarketPosition;

public class MarketAction extends Action {
	
	private StaticList<MarketPosition> tablePosition;
	private int positionInTableList;
	
	public MarketAction(ActionType type, Familiar familiar, StaticList<MarketPosition> tablePosition, int positionInTableList) throws NotEnoughResourcesException{
		//Constructor for normal action
		super(type, familiar);
		this.tablePosition = tablePosition;
		this.positionInTableList = positionInTableList;
	}
	
	public MarketAction(ActionType type, Player player, StaticList<MarketPosition> tablePosition, int positionInTableList, int actionIncrement) throws NotEnoughResourcesException{
		//Constructor for bonus action 
		super(type, player, 1, actionIncrement);
		this.tablePosition = tablePosition;
		this.positionInTableList = positionInTableList;
	}
	
	private boolean checkActionValue(){
		return actionValue >= tablePosition.get(positionInTableList).getLevel();
	}
	
	@Override
	public Response checkAction() {
		
		if( player.canPlay() ){				//Checks if the player has ban 
			
			checkIncreaseEffect();
			if( familiar != null ){			//Checks if is a bonus action 

				if( isFree() && player.canStayInMarket()){
					this.addIncrement(-(tablePosition.get(positionInTableList).getMalus()));
					if(checkActionValue() )
						//Checks if the action has a sufficient level for the position
						return Response.SUCCESS;
					else return Response.LOW_LEVEL;
				}
			else 
				return Response.FAILURE;		//The position is occupied or the player has a market ban
			}
			else								//If is a bonus action then only check action value
				if( checkActionValue())
					return Response.SUCCESS;
				else return Response.LOW_LEVEL;
		}
		return Response.CANNOT_PLAY;
	}	

	private boolean isFree(){
		return this.tablePosition.get(positionInTableList).isEmpty(); //TO-DO: controllare se il giocatore ha il bonus per le posizioni già occupate
	}
	
	@Override
	public void doAction() throws FamiliarInWrongPosition {
		if( familiar != null ){			
			//If is a normal action: set the familiar (this will automatically add the position bonuses)
			
			tablePosition.get(positionInTableList).setFamiliar(familiar);

		}
		else{
			//If is a bonus action, apply the effect directly to the player 
			Obtain bonus = tablePosition.get(positionInTableList).getBonus();
			bonus.enableEffect(player);		
			tablePosition.get(positionInTableList).applyCouncilBonus(player);
		}
		
		player.synchResource();  	 //Synchronize the resources in player
	}	

}
