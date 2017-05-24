package it.polimi.ingsw.ps42.model.position;

import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.player.Familiar;

public abstract class Position {

	//this is the abstract class for the position, familiar will be set by the game logic when necessary
	
	private ActionType type;
	private Familiar familiar;
	private Obtain bonus;
	private int malus;
	private int level;
	
	public Position(ActionType type, int level, Obtain bonus, int malus){
		
	}
	
	public Obtain getBonus() {
		return bonus;
	}
	public int getMalus() {
		return malus;
	}
	public Familiar getFamiliar() {
		return familiar;
	}
	public int getLevel() {
		return level;
	}
	public ActionType getType() {
		return type;
	}
	public void setFamiliar(Familiar familiar) {	//Invoked when the player move the familiar in this very position 
		this.familiar = familiar;
	}
	public void  removeFamiliar(){			//Have to be invoked to remove the familiar when the round ends
		
		
	}
	public boolean isEmpty(){			//Checks if there is a familiar in the position
		return this.familiar == null;		
	}
	
}
