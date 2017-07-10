package it.polimi.ingsw.ps42.model.action;


import it.polimi.ingsw.ps42.message.FamiliarUpdateMessage;
import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.Response;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.MarketPosition;

/**
 * Class that represents a market action
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class MarketAction extends Action {
	
	private StaticList<MarketPosition> tablePosition;
	private int positionInTableList;
	
	/**
	 * Constructor for a normal action
	 * @param type								The type of the action
	 * @param familiar							The familiar the player wants to move
	 * @param tablePosition						The StaticList of market position in table
	 * @param positionInTableList				The exact index of the chosen position
	 * @throws NotEnoughResourcesException		Thrown if the player hasn't enough resources
	 */
	public MarketAction(Familiar familiar, StaticList<MarketPosition> tablePosition, int positionInTableList) {

		super(ActionType.MARKET, familiar);
		this.tablePosition = tablePosition;
		this.positionInTableList = positionInTableList;
	}
	
	/**
	 * Constructor for bonus action 
	 * @param type								The type of the action
	 * @param player							The interested player
	 * @param tablePosition						The StaticList of market position in table
	 * @param positionInTableList				The exact index of the chosen position
	 * @param actionIncrement					The increment of this bonus action
	 * @throws NotEnoughResourcesException		Thrown if the player hasn't enough resources
	 */
	public MarketAction(Player player, StaticList<MarketPosition> tablePosition, int positionInTableList, int actionIncrement) {

		super(ActionType.MARKET, player, 1, actionIncrement);
		this.tablePosition = tablePosition;
		this.positionInTableList = positionInTableList;
	}
	
	/**
	 * Method used to verify if the player can do this action
	 * 
	 * @return		True if the position has a lower value than the action value, otherwise False
	 */
	private boolean checkActionValue(){
		return actionValue >= tablePosition.get(positionInTableList).getLevel();
	}
	
	/**
	 * Method used to control the action
	 */
	@Override
	public Response checkAction() {
		
		//If there isn't any existing position
		if(tablePosition.get(positionInTableList) == null) {
			if(familiar != null)
				familiar.resetIncrement();
			return Response.FAILURE;
		}
			
		
		if( player.canPlay() ){				//Checks if the player has ban 
			
			checkIncreaseEffect();
			if( familiar != null ){			//Checks if is a bonus action 

				if( isFree() && player.canStayInMarket() && !familiar.isPositioned()){
					this.addIncrement(-(tablePosition.get(positionInTableList).getMalus()));
					if(checkActionValue() )
						//Checks if the action has a sufficient level for the position
						return Response.SUCCESS;
					else {
						familiar.resetIncrement();
						return Response.LOW_LEVEL;
					}
				}
				else {
					//The position is occupied or the player has a market ban
					familiar.resetIncrement();
					return Response.FAILURE;
				}
					
			}
			else								//If is a bonus action then only check action value
				if( checkActionValue())
					return Response.SUCCESS;
				else {
					
					return Response.LOW_LEVEL;
				}
		}
		if(familiar != null)
			familiar.resetIncrement();
		return Response.CANNOT_PLAY;
	}	

	/**
	 * Method used to check if the position is free and if the player cannot positioning everywhere
	 * 
	 * @return	True is the position is free or if the player can position everywhere, otherwise False
	 */
	private boolean isFree(){
		return this.tablePosition.get(positionInTableList).isEmpty() || player.canPositioningEverywhere(); 
	}
	
	/**
	 * Method used to apply the action
	 */
	@Override
	public void doAction() throws FamiliarInWrongPosition {
		if( familiar != null ){			
			//If it is a normal action: set the familiar (this will automatically add the position bonuses)
			
			tablePosition.get(positionInTableList).setFamiliar(familiar);
			
			//Notify the view of the changes
			Message familiarUpdate = new FamiliarUpdateMessage(player.getPlayerID(), familiar.getColor(), getType(), positionInTableList);
			setChanged();
			notifyObservers(familiarUpdate);
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
