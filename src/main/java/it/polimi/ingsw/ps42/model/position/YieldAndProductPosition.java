package it.polimi.ingsw.ps42.model.position;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Familiar;

/**Implementation for both the Yield and Product position since 
 * they do the same job but on different type of cards:
 * they allow the player to do a Yield/Product action
 * 
 * @author Luca Napoletano, Claudio Montanari
*/
public class YieldAndProductPosition extends Position {
	
	private List<Familiar> bonusFamiliars;
	
	/**
	 * Default Constructor used to set the Position logger
	 */
	public YieldAndProductPosition() {
		logger = Logger.getLogger(YieldAndProductPosition.class);
	}
	
	/**
	 * Constructor for a Yield or Product Position
	 * @param type the type of Position
	 * @param level the level of the Position
	 * @param bonus the Bonus of the Position
	 * @param malus the malus of the Position
	 */
	public YieldAndProductPosition(ActionType type, int level, Obtain bonus, int malus) {
		super(type, level, bonus, malus);
		bonusFamiliars = new ArrayList<>();
	}
	
	/**
	 * Method used to enable the permanent effect of all the cards in the StaticList passed 
	 * @param cards the StaticList of Cards that have to be activated
	 * @param actionValue the value of the action performed on the Position
	 */
	public void enableCards(StaticList<Card> cards, int actionValue){		
		if(cards!=null ){
			for (Card card : cards) { 
				if(card.getLevel()<= actionValue){
					try {
						card.enablePermanentEffect();
					} catch (NotEnoughResourcesException e) {
						logger.debug("The player can not afford this effect");
						logger.info(e);
					}
				}
			}
		}
	}
	
	/**
	 * Setter for the Position Familiar, checks if requires the Familiar to be placed in a bonus position
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
	}
	
	/**
	 * Setter for the bonus Familiar, adds a bonus Familiar in the position, requires a Leader Card activation
	 * @param familiar the Familiar that have to be set
	 * @throws FamiliarInWrongPosition if the bonus Familiar does not satisfy the position pre-requirements
	 */
	public void addBonusFamiliar(Familiar familiar) throws FamiliarInWrongPosition {		
		if(canStay(familiar)){
			this.bonusFamiliars.add(familiar);
			this.applyPositionEffect(familiar);
		}
		else throw new FamiliarInWrongPosition("The bonus Familiar does not satisfy the position pre-requirements");
	}
	
	/**
	 * Getter for the List of all the bonus Familiar in the position
	 * @return the List of bonus Familiar
	 */
	public List<Familiar> getBonusFamiliar() {			
		return bonusFamiliars;
	}
	
	/**
	 * Removes all the bonus familiar from the position
	 */
	public void removeBonusFamiliars(){			
		
		for (Familiar familiar : bonusFamiliars) {
			familiar.resetIncrement();
		}
		this.bonusFamiliars=new ArrayList<>();
	}
	
	/**
	 * Clone method for the Position, also Clone all the internal Attributes
	 */
	@Override
	public YieldAndProductPosition clone() {
		return new YieldAndProductPosition(getType(), getLevel(), getBonus(), getMalus());
	}

}
