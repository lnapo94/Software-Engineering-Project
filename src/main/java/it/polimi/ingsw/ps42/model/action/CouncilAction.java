package it.polimi.ingsw.ps42.model.action;

import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.Response;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.CouncilPosition;

public class CouncilAction extends Action {

	//Private Variables
	private CouncilPosition tablePosition;

	public CouncilAction(ActionType type, Familiar familiar, CouncilPosition tablePosition) throws NotEnoughResourcesException{
		//Constructor for normal action
		super(type, familiar);
		this.tablePosition = tablePosition;
	}
	
	public CouncilAction(ActionType type, Player player, CouncilPosition tablePosition){
		//Constructor for bonus action
		super(type, player, 1);
		this.tablePosition = tablePosition;
	}
	
	private boolean checkActionValue(){
		return actionValue >= tablePosition.getLevel();
	}
	@Override
	public Response checkAction() {
		if( player.canPlay() ){
			if(familiar != null) {		//If is a normal action check increase effect and position malus
				checkIncreaseEffect();
				addIncrement(-tablePosition.getMalus());
				if( !checkActionValue())
					return Response.FAILURE;
			}
			//If is a bonus action 
			return Response.SUCCESS;
		}
		else return Response.CANNOT_PLAY;
	}

	@Override
	public void doAction() {
		if(familiar == null)
			tablePosition.applyCouncilPositionBonus(player);
		else {
			try {
				tablePosition.setFamiliar(familiar);
			} catch (FamiliarInWrongPosition e) {
				System.out.println("[DEBUG]: There is a wrong familiar in council Positions");
			}
		}
	}
}
