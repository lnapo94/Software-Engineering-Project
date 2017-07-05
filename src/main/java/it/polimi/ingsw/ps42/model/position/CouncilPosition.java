package it.polimi.ingsw.ps42.model.position;


import it.polimi.ingsw.ps42.model.effect.CouncilObtain;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;

/**Implementation of the Council position, the table will have an arrayList
 * of this class. Gives the player the right to receive a Privilege, then 
 * converted in a resource, in addiction he has an advantage for playing first 
 * in the next round.
 * 
 * @author Luca Napoletano, Claudio Montanari
*/
public class CouncilPosition extends Position {

	private final CouncilObtain councilBonus;
	
	/**
	 * Basic Constructor for the Position
	 * @param level the Position level
	 * @param bonus the Bonus of the Position
	 * @param malus the malus of the Position
	 * @param councilBonus the Council Privilege equivalent bonus
	 */
	public CouncilPosition( int level, Obtain bonus, int malus, CouncilObtain councilBonus) {
		super( ActionType.COUNCIL, level, bonus, malus);
		this.councilBonus=councilBonus;
		
	}
	
	/**
	 * Setter for the Council Position, also enable the Position Bonus
	 */
	@Override
	public void setFamiliar(Familiar familiar) throws FamiliarInWrongPosition {
		
		super.setFamiliar(familiar);
		applyCouncilPositionBonus(familiar.getPlayer());
		
	}
	
	/**
	 * Method used to enable the Position Council Bonus
	 * @param player
	 */
	public void applyCouncilPositionBonus(Player player){
		
		councilBonus.enableEffect(player);
	}
	
	/**
	 * Method used to clone the Position, copies all the internal Attributes
	 */
	@Override
	public CouncilPosition clone(){
		return new CouncilPosition(this.getLevel(), this.getBonus(), this.getMalus(), councilBonus);
		
	}
	
}
