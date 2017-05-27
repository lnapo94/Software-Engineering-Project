package it.polimi.ingsw.ps42.model.position;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.player.Familiar;

public class YieldAndProductPosition extends Position {
	
	/*Implementation for both the Yield and Product position since 
	 * they do the same job but on different type of cards:
	 * they allow the player to do a Yield/Product action
	*/
	private List<Familiar> bonusFamiliars;
	
	public YieldAndProductPosition(ActionType type, int level, Obtain bonus, int malus) {
		super(type, level, bonus, malus);
		bonusFamiliars = new ArrayList<>();
	}
	
	public void enableCards(List<Card> cards){		//Enables the permanent effect of all the cards in the arrayList for the first familiar placed
		enableCards(cards, super.getFamiliar());
	}
	
	public void enableCards(List<Card> cards, Familiar familiar){		//Enables the permanent effect of all the cards in the arrayList
		if(cards!=null && familiar!=null){
			for (Card card : cards) { 
				if(canApply(card, familiar))			//Checks if the effect of the card can be applied 
					card.enablePermanentEffect();
			}
		}
	}
	public void addBonusFamiliar(Familiar familiar) throws FamiliarInWrongPosition {		//Adds a bonus Familiar in the position, requires a Leader Card activation
		if(canStay(familiar)){
			this.bonusFamiliars.add(familiar);
			this.applyPositionEffect(familiar);
			this.bonusFamiliars.add(familiar);
		}
		else throw new FamiliarInWrongPosition("The bonus Familiar does not satisfy the position pre-requirements");
	}
	
	public List<Familiar> getBonusFamiliar() {			//Returns all the bonus Familiar in the position
		return bonusFamiliars;
	}
	public void removeBonusFamiliars(){			//Removes all the bonus familiar from the position
		
		this.bonusFamiliars=new ArrayList<>();
	}
	
	private boolean canApply(Card card, Familiar familiar){
		
		return card.getLevel()<= (familiar.getValue()+familiar.getIncrement());	//TO-DO: discutere se serve anche l'incremento o basta il valore
	}

}
