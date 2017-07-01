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

public class YieldAndProductPosition extends Position {
	
	/*Implementation for both the Yield and Product position since 
	 * they do the same job but on different type of cards:
	 * they allow the player to do a Yield/Product action
	*/
	private List<Familiar> bonusFamiliars;
	
	public YieldAndProductPosition() {
		logger = Logger.getLogger(YieldAndProductPosition.class);
	}
	
	public YieldAndProductPosition(ActionType type, int level, Obtain bonus, int malus) {
		super(type, level, bonus, malus);
		bonusFamiliars = new ArrayList<>();
	}
	
	
	public void enableCards(StaticList<Card> cards, int actionValue){		//Enables the permanent effect of all the cards in the arrayList
		if(cards!=null ){
			for (Card card : cards) { 
				//Controllo su valore azione poichè passo solo carte del giocatore e il valore dell'azione
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
	
	public void addBonusFamiliar(Familiar familiar) throws FamiliarInWrongPosition {		//Adds a bonus Familiar in the position, requires a Leader Card activation
		if(canStay(familiar)){
			this.bonusFamiliars.add(familiar);
			this.applyPositionEffect(familiar);
		}
		else throw new FamiliarInWrongPosition("The bonus Familiar does not satisfy the position pre-requirements");
	}
	
	public List<Familiar> getBonusFamiliar() {			//Returns all the bonus Familiar in the position
		return bonusFamiliars;
	}
	public void removeBonusFamiliars(){			//Removes all the bonus familiar from the position
		
		this.bonusFamiliars=new ArrayList<>();
	}
	
	@Override
	public YieldAndProductPosition clone() {
		return new YieldAndProductPosition(getType(), getLevel(), getBonus(), getMalus());
	}

}
