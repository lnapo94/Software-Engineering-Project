package it.polimi.ingsw.ps42.model.position;

import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;

public class CouncilPosition extends Position {

	/*Implementation of the Council position, the table will have an arrayList
	 * of this class. Gives the player the right to recive a Privilege, then 
	 * converted in a resource, in addiction he has an advantage for playing first 
	 * in the next round
	*/
	
	public CouncilPosition(ActionType type, int level, Obtain bonus, int malus) {
		super(type, level, bonus, malus);
		// TODO Auto-generated constructor stub
	}
}
