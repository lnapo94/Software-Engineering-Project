package it.polimi.ingsw.ps42.model.player;

import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.position.Position;

/**
 * This class represent the familiar in the game
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class Familiar {
	
	private Player player;
	private FamiliarColor color;
	private int value;
	private Position position;
	
	//This variable represent the temporary value of familiar for checking in GameLogic
	private int increment;
	
	/**
	 * Constructor of the familiar
	 * @param player	The player who has this familiar
	 * @param color		The color of this familiar
	 */
	public Familiar(Player player, FamiliarColor color) {
		this.player = player;
		this.color = color;
		this.value = 0;
		this.increment=0;	
	}
	
	/**
	 * Getter for the player associated to this familiar
	 * 
	 * @return 	The player who has this familiar
	 */
	public Player getPlayer() {
		return this.player;
	}
	
	/**
	 * Getter for the color of the familiar
	 * @return	The FamiliarColor of this familiar
	 */
	public FamiliarColor getColor() {
		return this.color;
		
	}
	
	/**
	 * Method used to set the value of this familiar when a die is thrown
	 * @param value		The value to set to this familiar
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
	/**
	 * Method used to enable an increase familiar effect
	 * @param 	The value used to increment the familiar
	 */
	public void enableFamiliarEffect(int value) {
		this.value = this.value + value;
		if(this.value < 0)
			this.value = 0;
	}
	
	/**
	 * Getter for the value of this familiar
	 * @return	The value of this familiar
	 */
	public int getValue() {
		return this.value;
	}
	
	/**
	 * Method used to add an increment to this familiar, used, for example, to increment the
	 * action's value
	 * @param i		The value to add to the increment of this familiar (This method ADD the variable i to the familiar)
	 */
	public void setIncrement(int i) {
		this.increment += i;
	}
	
	/**
	 * Getter used to know the increment of this familiar
	 * @return		The increment of this familiar
	 */
	public int getIncrement() {
		return this.increment;
	}
	
	/**
	 * Method used to know if a familiar is neutral
	 * @return	True if the familiar is neutral, otherwise False
	 */
	public boolean isNeutral() {
		return this.color == FamiliarColor.NEUTRAL;
	}
	
	/**
	 * Method used to know if a familiar is positioned yet
	 * @return	True if a familiar is positioned, otherwise false
	 */
	public boolean isPositioned() {
		return position != null;
	}
	
	/**
	 * Method used to set the position to the familiar
	 * @param position		The position where the familiar is
	 */
	public void setPosition(Position position) {
		this.position = position;
	}
	
	/**
	 * Method used to reset the current position
	 */
	public void resetPosition() {
		this.position = null;
	}
	
	/**
	 * Method used to reset the current increment
	 */
	public void resetIncrement() {
		this.increment = 0;
	}
	

}
