package it.polimi.ingsw.ps42.model;

import java.util.ArrayList;

import it.polimi.ingsw.ps42.model.action.Request;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.Color;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
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
	
	public ArrayList<Packet> getCosts() {
		//Return a copy of costs array
		return copyPacketList(costs);
	}
	
	public ArrayList<Packet> getRequirements() {
		//Return a copy of requirements array
		return copyPacketList(requirements);
	}
	
	public void setPlayer(Player owner) {
		this.owner = owner;
	}
	
	public void payCard(int choice, Packet discount) {
		//Apply the chosen card cost
		if(!( costs.isEmpty() ) && costs != null) {
			if(discount != null) {
				owner.increaseResource(discount);
			}
			owner.decreaseResource(costs.get(choice));
		}
	}

	public void payCard(int choice) {
		//Apply the chosen card cost without a discount
		payCard(choice, null);
	}
	
	/*	IMMEDIATE EFFECT */
	public void enableImmediateEffect() {
		enableEffect(immediateEffects);
	}
	
	public void enableImmediateEffect(int choice) {
		enableEffect(choice, immediateEffects);
	}
	/* END IMMEDIATE EFFECT */
	
	/*	PERMANENT EFFECT */
	public void enablePermanentEffect() {
		enableEffect(permanentEffects);
	}
	
	public void enablePermanentEffect(int choice) {
		enableEffect(choice, permanentEffects);
	}
	/*	END PERMANENT EFFECT */
	
	/* FINAL EFFECT */
	public void enableFinalEffect() {
		enableEffect(finalEffects);
	}
	
	public void enableFinalEffect(int choice) {
		enableEffect(choice, finalEffects);
	}
	/* END FINAL EFFECT */
	
	//PRIVATE METHODS FOR CARD CLASS
	private void createRequest() {
		//ONLY PRIVATE request
		//Used only to support effect
		if(possibleChoice.size() == 0) {
			throw new EmptyException("possibleChoice in Card Class is empty");
		}
		Request request = new Request(this, possibleChoice);
		owner.addRequest(request);
	}
	
	private void enableEffect(ArrayList<Effect> effectList) {
		if(effectList != null && !( effectList.isEmpty() ) ) {
			//If the effectList exist
			
			//Control all the effectList
			checkEffect(effectList);
			
			if(effectList.size() == 1 && effectList.size() == 0) {
				//If there is only one effectList and it isn't an ObtainEffect
				enableEffect(0, effectList);
			}
			else
				createRequest();
		}
	}
	
	private void enableEffect(int choice, ArrayList<Effect> effectList) {
		if(effectList != null && !( effectList.isEmpty() ) )
			effectList.get(0).enableEffect(owner);
	}
	
	private void checkEffect(ArrayList<Effect> effectList) {
		//Control the effect and create the possibleChoice arraylist
		
		for(Effect effect : effectList) {
			//For each effect in effectList
			
			if(effect.getTypeOfEffect() == EffectType.OBTAIN) {
				//Check if effect is an OBTAIN effect
				//In this case, control the costs of this effect
				
				Obtain obtainEffect = (Obtain) effect;
				Packet packet = obtainEffect.getCosts();
				
				//Boolean is used to control that all the packet can be payed
				boolean checkCanApplyEffect = true;
				
				for(Unit unit : packet) {
					//For each unit, control if player can pay the effect costs
					//If he can't pay, the boolean goes to false
					if(unit.getQuantity() > owner.getResource(unit.getResource()))
						checkCanApplyEffect = false;
				}
				
				//If player can pay, the boolean is true, then add this index to
				//the possibleChoice
				if(checkCanApplyEffect)
					possibleChoice.add(effectList.indexOf(effect));
			}
		}
	}
	
	private ArrayList<Packet> copyPacketList(ArrayList<Packet> start) {
		ArrayList<Packet> temp = new ArrayList<>();
		for(Packet packet : start) {
			temp.add(packet);
		}
		return temp;
	}
	
	//TODO
	/*
	 * DA TESTARE ASSOLUTAMENTE
	 */
}
