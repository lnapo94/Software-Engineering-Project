package it.polimi.ingsw.ps42.model.action;

import java.util.List;

import it.polimi.ingsw.ps42.message.FamiliarUpdateMessage;
import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.Response;
import it.polimi.ingsw.ps42.model.exception.EmptyException;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.YieldAndProductPosition;

/**
 * Class that represents a yield or a product action, in other words the action done when
 * the player positions his familiars in the yield or in the produce positions. This class also
 * represents a bonus action of the same kind
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class YieldAndProductAction extends Action {
	
	private List<YieldAndProductPosition> tablePosition;
	private YieldAndProductPosition firstPosition;
	private YieldAndProductPosition firstFreePosition;		//The position used to enable the action
	
	/**
	 * Constructor for a normal action
	 * @param type							The type of the action
	 * @param familiar						The familiar to position
	 * @param tablePosition					The list of the other yield or product positions in table
	 * @param firstPosition					The first yield or product position in the table (For default game, is that has no malus)
	 * @throws NotEnoughResourcesException	Thrown if the player hasn't enough resources
	 */
	public YieldAndProductAction(ActionType type, Familiar familiar, List<YieldAndProductPosition> tablePosition, YieldAndProductPosition firstPosition) throws NotEnoughResourcesException{
		//Constructor for normal action
		super(type, familiar);
		this.tablePosition = tablePosition;
		this.firstPosition = firstPosition;
	}
	
	/**
	 * Constructor for bonus action
	 * @param type							The type of the action
	 * @param player						The interested player
	 * @param tablePosition					The list of the other yield or product positions in table
	 * @param firstPosition					The first yield or product position in the table (For default game, is that has no malus)
	 * @param actionValue					The value of the bonus action
	 * @param actionIncrement				The increment the player wants for this action
	 * @throws NotEnoughResourcesException	Thrown if the player hasn't enough resources
	 */
	public YieldAndProductAction(ActionType type, Player player, List<YieldAndProductPosition> tablePosition,
			YieldAndProductPosition firstPosition, int actionValue, int actionIncrement) throws NotEnoughResourcesException {

		super(type, player, actionValue, actionIncrement);
		this.tablePosition = tablePosition;
		this.firstPosition = firstPosition;
	}
	
	/**
	 * Method used to check the correctness of the action
	 */
	@Override
	public Response checkAction() {
		/*
		 * First: Check ban 
		 * Second: Check increase effect
		 * Third: Check the first free position (if the first is occupied check if you are the only familiar of that player, pay attention to neutral familiar)
		 * Fourth: Check action value
		 * Fifth: Apply position bonus and malus
		 */
		
		if(this.player.canPlay()){
			checkIncreaseEffect();
			if(familiar != null){		//If is a normal action get the first free position otherwise you can get the firstPosition
				
				if(canStay() && !familiar.isPositioned()){
					this.firstFreePosition = getFirstFreePosition();
					this.addIncrement( -firstFreePosition.getMalus() );
					if( checkLevel() == Response.SUCCESS )	
						try {
								//Set the familiar and apply position bonus and malus 
								firstFreePosition.setFamiliar(familiar);
								return Response.SUCCESS;
							} catch (FamiliarInWrongPosition e) {
								familiar.resetIncrement();
								return Response.FAILURE;
							}
					else { 
						familiar.resetIncrement();
						return Response.LOW_LEVEL;		
						}
				}
				else {		
					//Unable to do this move due to canStay()
					familiar.resetIncrement();
					return Response.FAILURE;
				}
			}
			else{		//If is a bonus action you get the first position
				this.firstFreePosition = firstPosition;
				this.addIncrement( -firstFreePosition.getMalus() );
				return checkLevel();
			}
		}
		else{
			//The player has a ban for the current action
			familiar.resetIncrement();
			return Response.FAILURE;		
		}
	}
	
	/**
	 * Private method use to know if the player can position in the first position
	 * 
	 * @return	True if the specified position has a lower level than the action value, False if the position has a higher level than then the action value, otherwise return the value of checkOtherPosition()
	 */
	private boolean canStay(){
		
		if( firstPosition.isEmpty())
			if(actionValue < firstPosition.getLevel()) 	
				//The first Position requires at least an Action of level 1 (no neutral familiar without increment)
				return false;
			else 
				return true;
			//If there are Familiars in others Positions different from Neutral, then the Action is illegal
		return checkOtherPositions();
		
	}
	
	/**
	 * Private method used to verify if the player can position in the other positions
	 * 
	 * @return	True if the specified position has a lower level than the action value and if there isn't other current player's familiars (apart the neutral), otherwise False
	 */
	private boolean checkOtherPositions(){
		//Checks the Color rules of the YieldAndProductPosition
		if( tablePosition != null ) {
			
			//Check if the Player has already a Familiar in this zone and if the level of the Action is sufficient
			for (YieldAndProductPosition position : tablePosition) {
				
				if(!position.isEmpty() && position.getFamiliar().getPlayer().getPlayerID().equals(familiar.getPlayer().getPlayerID())){
					if(!position.getFamiliar().isNeutral() || !familiar.isNeutral()){
						return false;	//There would be two familiar of the same player, none of them neutral
					}
					if( firstPosition.getFamiliar().getPlayer().getPlayerID().equals(familiar.getPlayer().getPlayerID()))
						return false;	//There would be three familiar of the same player
				}
			}
			return true;
		}
		return false; 	//The extra-product position are disabled (2-player game)
		
	}
	
	/**
	 * Control the level of the first free position
	 * 
	 * @return	SUCCESS if the firstFreePosition level is lower than the action value, otherwise False
	 */
	private Response checkLevel(){
		if( this.actionValue < firstFreePosition.getLevel())
			return Response.LOW_LEVEL;
		else 
			return Response.SUCCESS;
	}
	
	/**
	 * Method used to have the first free position from the tablePosition list
	 * @return					The first free position in table
	 * @throws EmptyException	If there isn't another free position, it is a fatal error
	 */
	private YieldAndProductPosition getFirstFreePosition(){
		
		if(firstPosition.isEmpty() || player.canPositioningEverywhere())
			return firstPosition;
		else {
			for (YieldAndProductPosition position : tablePosition) {
				if(position.isEmpty())
					return position;
			}
		}
		throw new EmptyException("The Product action was not able to find a free position for the action");
	}
	
	/**
	 * Method used to apply the action
	 */
	@Override
	public void doAction() {
		
		/* Zero: Syncronize player resources
		 * First: Take Yield/Product cards from player and enable them through the position method 
		 * (the requests are handled by the game logic)
		 * Second: Enable bonusBar Bonuses
		 */
		
		//Synch player resources (pay for the increment)
		player.synchResource();
		
		//Enable card effects
		if( getType() == ActionType.PRODUCE ) {
			firstFreePosition.enableCards( player.getCardList(CardColor.YELLOW), actionValue);
			//Enable BonusBar bonuses
			player.enableBonus(ActionType.PRODUCE);
		}
		
		if( getType() == ActionType.YIELD ) {
			firstFreePosition.enableCards( player.getCardList(CardColor.GREEN), actionValue);
			//Enable BonusBar bonuses
			player.enableBonus(ActionType.YIELD);
		}
		
		//Notify
		if(familiar != null) {
			//Create the message for the view
			Message familiarUpdate = new FamiliarUpdateMessage(player.getPlayerID(), familiar.getColor(), getType(), 0);
		
			//Notify the view of this change
			setChanged();
			notifyObservers(familiarUpdate);
		}
		
	}

}
