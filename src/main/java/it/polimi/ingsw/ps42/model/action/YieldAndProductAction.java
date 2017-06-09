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

public class YieldAndProductAction extends Action {
	
	private List<YieldAndProductPosition> tablePosition;
	private YieldAndProductPosition firstPosition;
	private YieldAndProductPosition firstFreePosition;		//The position used to enable the action
	
	public YieldAndProductAction(ActionType type, Familiar familiar, List<YieldAndProductPosition> tablePosition, YieldAndProductPosition firstPosition) throws NotEnoughResourcesException{
		//Constructor for normal action
		super(type, familiar);
		this.tablePosition = tablePosition;
		this.firstPosition = firstPosition;
	}
	public YieldAndProductAction(ActionType type, Player player, List<YieldAndProductPosition> tablePosition,
			YieldAndProductPosition firstPosition, int actionValue, int actionIncrement) throws NotEnoughResourcesException {
		//Constructor for bonus action
		super(type, player, actionValue, actionIncrement);
		this.tablePosition = tablePosition;
		this.firstPosition = firstPosition;
	}
	
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
				if(canStay()){
					this.firstFreePosition = getFirstFreePosition();
					this.addIncrement( -firstFreePosition.getMalus() );
					if( checkLevel() == Response.SUCCESS )	
						try {
								//Set the familiar and apply position bonus and malus 
								firstFreePosition.setFamiliar(familiar);
								return Response.SUCCESS;
							} catch (FamiliarInWrongPosition e) {
								return Response.FAILURE;
							}
					else return Response.LOW_LEVEL;		//CheckAction control failed
				}
				else		//Unable to do this move due to canStay()
					return Response.FAILURE;
			}
			else{		//If is a bonus action you get the first position
				this.firstFreePosition = firstPosition;
				this.addIncrement( -firstFreePosition.getMalus() );
				return checkLevel();
			}
		}
		else{
			//The player has a ban for the current action
			return Response.FAILURE;		
		}
	}
	
	private boolean canStay(){
		
		if( firstPosition.isEmpty())
			if(actionValue < firstPosition.getLevel()) 	
				//The first position requires at least an action of level 1 (no neutral familiar without increment)
				return false;
			else
				return true;
		
		else if( tablePosition != null ) {
			
			//Check if the player has already a familiar in this zone and if the level of the action is sufficient
			for (YieldAndProductPosition position : tablePosition) {
				
				if(!position.isEmpty() && position.getFamiliar().getPlayer() == familiar.getPlayer()){
					if(!position.getFamiliar().isNeutral() || !familiar.isNeutral()){
						return false;	//There would be two familiar of the same player, none of them neutral
					}
					if( firstPosition.getFamiliar().getPlayer() == familiar.getPlayer())
						return false;	//There would be three familiar of the same player
				}
			}
			return true;
		}
		return false; 	//The extra-product position are disabled (2-player game)
	}
	
	private Response checkLevel(){
		if( this.actionValue < firstFreePosition.getLevel())
			return Response.LOW_LEVEL;
		else 
			return Response.SUCCESS;
	}
	private YieldAndProductPosition getFirstFreePosition(){
		
		if(firstPosition.isEmpty())
			return firstPosition;
		else {
			for (YieldAndProductPosition position : tablePosition) {
				if(position.isEmpty())
					return position;
			}
		}
		throw new EmptyException("The Product action was not able to find a free position for the action");
	}
	
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
