package it.polimi.ingsw.ps42.model;

import java.util.ArrayList;

import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.enumeration.Color;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

public class Card {
	/*Class for Card, when created is placed in a position and do not has a owner, 
	 * the owner is setted only when a player takes the card. In every ArrayList 
	 * (Packet or Effect) the elements have to be considered in OR while IN the single 
	 * Packet/Effect they are in AND
	 */
	private String name;
	private String description;
	private Color color;
	private int period;
	private int level;
	private Player owner;
	private ArrayList<Packet> costs;
	private ArrayList<Packet> requirements;
	private ArrayList<Effect> immediateEffects;
	private ArrayList<Effect> permanentEffects;
	
	public Card(String name, String description, Color color, int period, 
			int level, ArrayList<Packet> costs, ArrayList<Effect> immediateEffects, ArrayList<Effect> permanentEffect){
		
		
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getPeriod() {
		return period;
	}
	
	public int getLevel() {
		return level;
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public ArrayList<Packet> getCosts() {
		return costs;
	}
	
	public ArrayList<Packet> getRequirements() {
		return requirements;
	}
	
	public ArrayList<Effect> getImmediateEffect() {
		return immediateEffects;
	}
	
	public ArrayList<Effect> getPermanentEffect() {
		return permanentEffects;
	}
	
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
	public void enableImmediateEffect(){	//Enables the immediate effects of the card, it may require a request to the  player
		
	}
	
	public void enablePermanentEffect(){	//Enables the permanent effects of the card, it may require a request to the player
		
	}
	
	public void enableImmediateEffect(int choice){		//Enables only the immediate effect passed 
		
	}
	
	public void enablePermanentEffect(int choice){		//Enables only the immediate effect passed
		
	}
	
	
	
	

}
