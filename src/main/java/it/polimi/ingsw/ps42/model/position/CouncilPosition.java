package it.polimi.ingsw.ps42.model.position;

import java.util.List;

import it.polimi.ingsw.ps42.model.effect.CouncilObtain;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;

public class CouncilPosition extends Position {

	/*Implementation of the Council position, the table will have an arrayList
	 * of this class. Gives the player the right to recive a Privilege, then 
	 * converted in a resource, in addiction he has an advantage for playing first 
	 * in the next round
	*/
	
	private final CouncilObtain councilBonus;
	
	public CouncilPosition(ActionType type, int level, Obtain bonus, int malus, CouncilObtain councilBonus) {
		super(type, level, bonus, malus);
		this.councilBonus=councilBonus;
		
	}
	
	@Override
	public void setFamiliar(Familiar familiar) throws FamiliarInWrongPosition {
		
		super.setFamiliar(familiar);
		applyCouncilPositionBonus(familiar.getPlayer());
		
	}
	
	public void applyCouncilPositionBonus(Player player){
		
		councilBonus.enableEffect(player);
	}
	
}
