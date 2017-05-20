package it.polimi.ingsw.ps42.model.position;

import java.util.ArrayList;

public class MarketPosition extends Position {
	
	/*Implementation of the Market position, gives to the
	 * player a certain amount of resources  
	*/
	private ArrayList<Familiar> bonusFamiliar;
	
	public ArrayList<Familiar> getBonusFamiliar() {
		return bonusFamiliar;
	}
	public void setBonusFamiliar(Familiar familiar) {
		
	}

}
