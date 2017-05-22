package it.polimi.ingsw.ps42.model;

import java.util.ArrayList;

import org.hamcrest.core.IsInstanceOf;

import it.polimi.ingsw.ps42.model.action.Request;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.enumeration.Color;
import it.polimi.ingsw.ps42.model.exception.EmptyException;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

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
	private ArrayList<Effect> finalEffects;
	
	//ArrayList used for check if player can pay to obtain the card or enable the effect
	private ArrayList<Integer> possibleChoice;
	
	public Card(String name, String description, Color color, int period, 
			int level, ArrayList<Packet> costs, ArrayList<Effect> immediateEffects, ArrayList<Packet> requirements,
			ArrayList<Effect> permanentEffect, ArrayList<Effect> finalEffects){
		//Construct the card
		
		this.name = name;
		this.description = description;
		this.color = color;
		this.period = period;
		this.level = level;
		this.costs = costs;
		this.requirements = requirements;
		this.immediateEffects = immediateEffects;
		this.permanentEffects = permanentEffect;
		this.finalEffects = finalEffects;
		
		//Construct the arraylist
		this.possibleChoice = new ArrayList<>();
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
	
	public void checkPlayerResources(Player player) {
		//Check if the player can pay the card costs
		
		for (Packet packet : costs) {
			//This FOR EACH statement controls all the packet
			
			//canPay is used to control all the unit in one packet
			//If a unit in a packet it's higher than the player costs, that packet cannot be used
			//to pay the card
			//Else it can be used, than it is added to the possible choice 
			boolean canPay = true;
			for (Unit unit : packet) {
				if (unit.getQuantity() > player.getResource(unit.getResource())) {
					canPay = false;
				}
			}
			if (canPay == true) {
				possibleChoice.add(costs.indexOf(packet));
			}
		}
	}
	
	private void payCard() {
		if (costs.size() == 1) {
			owner.decreaseResource(costs.get(0));
		}
		else if (costs.size() > 1) {
			createRequest();
		}
	}
	
	public void setOwner(Player owner) {
		this.owner = owner;
		payCard();
	}
	
	public void enableImmediateEffect(){	
		//Enables the immediate effects of the card, it may require a request to the  player
		if(immediateEffects.size() == 1) {
			
		}
		else if(immediateEffects.size() > 1) {
			
		}
	}
	
	public void enablePermanentEffect(){	
		//Enables the permanent effects of the card, it may require a request to the player
		
	}
	
	public void enableFinalEffect() {
		//Enable the final effect at the end of the game
	}
	
	public void enableImmediateEffect(int choice){		
		//Enables only the immediate effect passed 
		
	}
	
	public void enablePermanentEffect(int choice){		
		//Enables only the immediate effect passed
		
	}
	
	public void enableFinalEffect(int choice) {
		//Enable the specific final effect among the effects in the arraylist
	}
	
	//Private Methods for internal use
	
	private void createRequest() {
		if (possibleChoice.size() < 1) {
			throw new EmptyException("Error in card: possibleChoice array is empty");
		}
		owner.addRequest(new Request(this, possibleChoice));
		for (Integer i : possibleChoice) {
			possibleChoice.remove(i);
		}
	}
	
	//TODO
	/*
	 * Implementare i vari metodi enable stando attenti alla creazione corretta delle varie
	 * richieste.
	 * Ricordarsi inoltre che una richiesta, una volta creata, svuota l'arraylist delle
	 * possibili scelte
	 */
}
