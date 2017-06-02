package it.polimi.ingsw.ps42.model.position;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.player.Familiar;

public class MarketPosition extends Position {
	
	/*Implementation of the Market position, gives to the
	 * player a certain amount of resources  
	*/
	
	private List<Familiar> bonusFamiliars;
	
	
	public MarketPosition( int level, Obtain bonus, int malus) {
		super( ActionType.MARKET, level, bonus, malus);
		bonusFamiliars=new ArrayList<>();
		
	}
	
	public List<Familiar> getBonusFamiliar() {
		return bonusFamiliars;
	}
	public void addBonusFamiliar(Familiar familiar) throws FamiliarInWrongPosition {
		if(super.canStay(familiar)){
			this.bonusFamiliars.add(familiar);
			applyPositionEffect(familiar);
			this.bonusFamiliars.add(familiar);
		}
		else throw new FamiliarInWrongPosition("The bonus Familiar does not satisfy the position pre-requirements");
	}
	public void removeBonusFamiliars(){
		
		this.bonusFamiliars=new ArrayList<>();
	}

}
