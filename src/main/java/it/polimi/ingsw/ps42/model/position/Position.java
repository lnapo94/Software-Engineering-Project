package it.polimi.ingsw.ps42.model.position;

import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.exception.FamiliarInWrongPosition;
import it.polimi.ingsw.ps42.model.player.Familiar;

public abstract class Position {

	//this is the abstract class for the position, familiar will be set by the game logic when necessary
	
	private final ActionType type;
	private Familiar familiar;
	private final Obtain bonus;
	private final int malus;
	private final int level;
	
	public Position(ActionType type, int level, Obtain bonus, int malus){
		
			this.type=type;
			this.level=level;
			this.bonus=bonus;
			this.malus=malus;
		
	}
	
	public Obtain getBonus() {
		//Clone of the position bonus to avoid problem from position clone in the setup
		return new Obtain(bonus.getCosts(), bonus.getGains());
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
	public void setFamiliar(Familiar familiar) throws FamiliarInWrongPosition{	//Invoked when the player move the familiar in this very position 
		if( familiar != null ){		//TO-DO:decidere come gestire caso familiar==null
			applyPositionEffect(familiar);
			this.familiar=familiar;
		}
		else throw new FamiliarInWrongPosition("The familiar does not satisfy the position pre-requisites");
	}
	public void  removeFamiliar(){			//Have to be invoked to remove the familiar when the round ends
		this.familiar=null;
	}
	public boolean isEmpty(){			//Checks if there is a familiar in the position
		return this.familiar == null;		
	}
	
	protected void applyPositionEffect(Familiar familiar){
		
		familiar.setIncrement(-malus);
		if(bonus!=null){
			bonus.enableEffect(familiar.getPlayer()); 		//enables the position bonus on the player
	
		}
	}
	
	protected boolean canStay(Familiar familiar){
		return (familiar.getValue()-this.malus)>=this.level;
	}
	
}
