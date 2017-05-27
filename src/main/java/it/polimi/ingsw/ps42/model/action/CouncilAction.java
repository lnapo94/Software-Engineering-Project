package it.polimi.ingsw.ps42.model.action;

import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.Response;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.CouncilPosition;
import it.polimi.ingsw.ps42.model.request.CouncilRequest;

public class CouncilAction extends Action {

	//Private Variables
	private CouncilPosition tablePosition;

	public CouncilAction(ActionType type, Familiar familiar, CouncilPosition tablePosition){
		//Constructor for normal action
		super(type, familiar);
		this.tablePosition = tablePosition;
	}
	
	public CouncilAction(ActionType type, Player player, CouncilPosition tablePosition, int actionValue){
		//Constructor for bonus action
		super(type, player, actionValue);
		this.tablePosition = tablePosition;
	}
	
	@Override
	public Response checkAction() {
		if(familiar != null) {
			checkIncreaseEffect();
			if(familiar.getIncrement() + actionValue < tablePosition.getLevel())
				return Response.FAILURE;
		}
		return Response.SUCCESS;
	}

	@Override
	public void doAction() {
		if(familiar == null)
			tablePosition.applyCouncilPositionBonus(player);
		else {
			try {
				tablePosition.setFamiliar(familiar);
			} catch (FamiliarInWrongPosition e) {
				e.printStackTrace();
			}
		}
	}
}
