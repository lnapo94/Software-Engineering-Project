package it.polimi.ingsw.ps42.model.position;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.ps42.model.effect.CouncilObtain;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;


/**Implementation of the Market position, gives to the
 * player a certain amount of resources or a council privilege 
 * 
 * @author Luca Napoletano, Claudio Montanari
*/

public class MarketPosition extends Position {
	
	private CouncilObtain councilBonus;
	private List<Familiar> bonusFamiliars;

	/**
	 * Constructor for the Market Position
	 * @param level the Position level
	 * @param bonus the position Bonus
	 * @param malus the position malus
	 * @param councilBonus the position Council Obtain bonus
	 */
	public MarketPosition( int level, Obtain bonus, int malus, CouncilObtain councilBonus) {
		super( ActionType.MARKET, level, bonus, malus);
		bonusFamiliars=new ArrayList<>();
		this.councilBonus = councilBonus;
	}
	
	/**
	 * Getter for the Bonus Familiar List
	 * @return the List of Bonus Familiar
	 */
	public List<Familiar> getBonusFamiliar() {
		return bonusFamiliars;
	}
	
	/**
	 * Setter for the Familiar, checks if the Familiar need to be placed in the bonus List and enable the Council bonus
	 */
	@Override
	public void setFamiliar(Familiar familiar) throws FamiliarInWrongPosition {
		if(this.isEmpty())
			//If the position is empty proceed as usual
			super.setFamiliar(familiar);
		else{
			//If the Position is occupied, set the Familiar to the bonus positions
			this.addBonusFamiliar(familiar);
		}
		applyCouncilBonus(familiar.getPlayer());
	}
	
	/**
	 * Method used to apply the Position Council Bonus
	 * @param player the Player that receive the Bonus
	 */
	public void applyCouncilBonus( Player player){
		if(councilBonus != null)
			councilBonus.enableEffect(player);
	}
	
	/**
	 * Private method used to set the Bonus Familiar
	 * @param familiar the Familiar to set in the Position
	 * @throws FamiliarInWrongPosition, if the Familiar does not satisfy the Position pre-requisites
	 */
	private void addBonusFamiliar(Familiar familiar) throws FamiliarInWrongPosition {
		if(familiar != null){
			this.bonusFamiliars.add(familiar);
			applyPositionEffect(familiar);
			applyCouncilBonus(familiar.getPlayer());
		}
	}
	
	/**
	 * Method used to remove the Bonus Familiar, also reset their increment
	 */
	public void removeBonusFamiliars(){
		
		for (Familiar familiar : bonusFamiliars) {
			familiar.resetIncrement();
		}
		this.bonusFamiliars=new ArrayList<>();
	}

}
