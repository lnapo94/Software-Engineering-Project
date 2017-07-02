package it.polimi.ingsw.ps42.model.action;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.message.FamiliarUpdateMessage;
import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.Response;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.CouncilPosition;

/**
 * Class used to do a Council Action
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class CouncilAction extends Action {

	//Private Variables, the game logic must ask to the table the first free council position
	private CouncilPosition tablePosition;
	
	//Logger
	private transient Logger logger = Logger.getLogger(CouncilAction.class);

	/**
	 * Constructor for normal action called by the action creator
	 * @param type							The type of the action
	 * @param familiar						The familiar the player wants to move
	 * @param tablePosition					The position took from the table
	 * @throws NotEnoughResourcesException	Thrown if the player hasn't enough resources
	 */
	public CouncilAction(ActionType type, Familiar familiar, CouncilPosition tablePosition) throws NotEnoughResourcesException{
		
		super(type, familiar);
		this.tablePosition = tablePosition;
	}
	
	/**
	 * Constructor for a bonus action
	 * @param type							The type of the action
	 * @param player						The interested player
	 * @param tablePosition					The position took from the table
	 * @param actionIncrement				The increment the player wants
	 * @throws NotEnoughResourcesException	Thrown if the player hasn't enough resources
	 */
	public CouncilAction(ActionType type, Player player, CouncilPosition tablePosition, int actionIncrement) throws NotEnoughResourcesException{
		super(type, player, 1, actionIncrement);
		this.tablePosition = tablePosition;
	}
	
	/**
	 * Private method to check if the action value is at least the position value
	 * 
	 * @return	True if the player can positioning his familiar here, otherwise False
	 */
	private boolean checkActionValue(){
		return actionValue >= tablePosition.getLevel();
	}
	
	/**
	 * Method used to control the action
	 */
	@Override
	public Response checkAction() {
		
		if( player.canPlay() ){
			if(familiar != null) {		//If is a normal action check increase effect and position malus
				
				if(familiar.isPositioned()) {
					familiar.resetIncrement();
					return Response.FAILURE;
				}
				
				checkIncreaseEffect();
				addIncrement(-tablePosition.getMalus());
				if( !checkActionValue()) {
					familiar.resetIncrement();
					return Response.LOW_LEVEL;
				}
				else return Response.SUCCESS;
			}
			//If is a bonus action 
			else return Response.SUCCESS;
		}
		else { 
			familiar.resetIncrement();
			return Response.CANNOT_PLAY;
		}
	}

	/**
	 * Method used to apply the action
	 */
	@Override
	public void doAction() {
		if(familiar == null)
			tablePosition.applyCouncilPositionBonus(player);
		else {
			try {
				tablePosition.setFamiliar(familiar);
			} catch (FamiliarInWrongPosition e) {
				logger.error("[DEBUG]: There is a wrong familiar in council Positions");
				logger.info(e);
			}
			Message familiarUpdate = new FamiliarUpdateMessage(player.getPlayerID(), familiar.getColor(), getType(), 0);
			setChanged();
			notifyObservers(familiarUpdate);
		}
		player.synchResource();
	}
}
