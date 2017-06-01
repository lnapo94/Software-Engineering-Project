package it.polimi.ingsw.ps42.model.player;

import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;

public class Familiar {
	//This class represent the familiar in the game. It is linked to player
	
	private Player player;
	private FamiliarColor color;
	private int value;
	
	//This variable represent the temporary value of familiar for checking in gamelogic
	private int increment;
	
	public Familiar(Player player, FamiliarColor color) {
		this.player = player;
		this.color = color;
		this.value = 0;
		this.increment=0;	
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public FamiliarColor getColor() {
		return this.color;
		
	}
	
	public void setValue(int value) {
		this.value = value;
		player.familiarBan();
	}
	
	public int getValue() {
		return this.value;
	}
	
	public void setIncrement(int i) {
		this.increment += i;
	}
	
	public int getIncrement() {
		return this.increment;
	}
	
	public boolean isNeutral() {
		return this.color == FamiliarColor.NEUTRAL;
	}
	

}
