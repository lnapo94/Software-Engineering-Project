package it.polimi.ingsw.ps42.model.position;

import java.util.List;

import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.player.Familiar;

public class MarketPosition extends Position {
	
	/*Implementation of the Market position, gives to the
	 * player a certain amount of resources  
	*/
	
	private List<Familiar> bonusFamiliar;
	
	
	public MarketPosition(ActionType type, int level, Obtain bonus, int malus) {
		super(type, level, bonus, malus);
		// TODO Auto-generated constructor stub
	}
	
	public List<Familiar> getBonusFamiliar() {
		return bonusFamiliar;
	}
	public void setBonusFamiliar(Familiar familiar) {
		
	}

}
