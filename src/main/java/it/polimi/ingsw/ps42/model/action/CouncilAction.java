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
				System.out.println("[DEBUG]: There is a wrong familiar in council Positions");
			}
		}
	}
}
