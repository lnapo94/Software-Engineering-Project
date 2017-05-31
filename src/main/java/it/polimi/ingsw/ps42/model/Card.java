package it.polimi.ingsw.ps42.model;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.request.CardRequest;
import it.polimi.ingsw.ps42.model.request.FinalRequest;
import it.polimi.ingsw.ps42.model.request.ImmediateRequest;
import it.polimi.ingsw.ps42.model.request.PayRequest;
import it.polimi.ingsw.ps42.model.request.PermanentRequest;
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
	private CardColor color;
	private int period;
	private int level;
	private Player owner;
	private List<Packet> costs;
	private List<Packet> requirements;
	private List<Effect> immediateEffects;
	private List<Effect> permanentEffects;
	private List<Effect> finalEffects;
	
	//ArrayList used for check if player can pay to obtain the card or enable the effect
	private List<Printable> possibleChoice;
	private List<Integer> possibleChoiceIndex;
	
	public Card(String name, String description, CardColor color, int period, 
			int level, List<Packet> costs, List<Effect> immediateEffects, List<Packet> requirements,
			List<Effect> permanentEffect, List<Effect> finalEffects){
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
		this.possibleChoiceIndex = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public CardColor getColor() {
		return color;
	}
	
	public int getPeriod() {
		return period;
	}
	
	public int getLevel() {
		return level;
	}
	
	public List<Packet> getCosts() {
		//Return a copy of costs array
		return copyPacketList(costs);
	}
	
	public List<Packet> getRequirements() {
		//Return a copy of requirements array
		return copyPacketList(requirements);
	}
	
	public void setPlayer(Player owner) {
		this.owner = owner;
	}
	
	public void payCard(Player player, Packet discount, int choice) throws NotEnoughResourcesException {
		if(discount != null)
			player.increaseResource(discount);
		player.decreaseResource(costs.get(choice));
	}
	
	public void payCard(Player player, Packet discount) throws NotEnoughResourcesException {
		if(checkRequirements(player)) {
			if(costs != null) {
				if(costs.size() == 1)
					payCard(player, discount, 0);
				else {
					for(Packet cost : costs)
						if(checkPlayerCanPay(cost, player)) {
							possibleChoice.add(cost);
							possibleChoiceIndex.add(costs.indexOf(cost));
							CardRequest request = new PayRequest(player, this, discount, possibleChoiceIndex, possibleChoice);
							player.addRequest(request);
						}
				}
			}
		}
		else
			throw new NotEnoughResourcesException("Player hasn't enough requirements");
	}
	
	/*	IMMEDIATE EFFECT */
	public void enableImmediateEffect() throws NotEnoughResourcesException {
		if(immediateEffects != null && !( immediateEffects.isEmpty() ) ) {
			//If the effectList exist
			if(canEnableNowEffect(immediateEffects))
				enableEffect(0, immediateEffects);
			else{
				controlPossibleChoice();
				CardRequest request = new ImmediateRequest(this, possibleChoiceIndex, possibleChoice);
				owner.addRequest(request);
			}
		}
	}
	
	public void enableImmediateEffect(int choice) {
		enableEffect(choice, immediateEffects);
	}
	/* END IMMEDIATE EFFECT */
	
	/*	PERMANENT EFFECT */
	public void enablePermanentEffect() throws NotEnoughResourcesException {
		if(permanentEffects != null && !( permanentEffects.isEmpty() ) ) {
			//If the effectList exist
			if(canEnableNowEffect(permanentEffects))
				enableEffect(0, permanentEffects);
			else {
				controlPossibleChoice();
				CardRequest request = new PermanentRequest(this, possibleChoiceIndex, possibleChoice);
				owner.addRequest(request);
			}
		}
	}
	
	public void enablePermanentEffect(int choice) {
		enableEffect(choice, permanentEffects);
	}
	/*	END PERMANENT EFFECT */
	
	/* FINAL EFFECT */
	public void enableFinalEffect() throws NotEnoughResourcesException {
		if(finalEffects != null && !( finalEffects.isEmpty() ) ) {
			//If the effectList exist
			if(canEnableNowEffect(finalEffects))
				enableEffect(0, finalEffects);
			else {
				controlPossibleChoice();
				CardRequest request = new FinalRequest(this, possibleChoiceIndex, possibleChoice);
				owner.addRequest(request);
			}
		}
	}
	
	public void enableFinalEffect(int choice) {
		enableEffect(choice, finalEffects);
	}
	/* END FINAL EFFECT */
	
	public Packet checkCosts(Player player) {
		for(Packet cost : costs)
			if(checkPlayerCanPay(cost, player))
				return cost;
		return null;
	}
	
	//PRIVATE METHODS FOR CARD CLASS
	private void controlPossibleChoice() throws NotEnoughResourcesException {
		//ONLY PRIVATE request
		//Used only to verify if the arrays of choices isn't empty
		if(possibleChoice.isEmpty() || possibleChoiceIndex.isEmpty()) 
			throw new NotEnoughResourcesException("The possibleChoice array is empty, cannot pay this");
	}
	
	private void enableEffect(int choice, List<Effect> effectList) {
			effectList.get(choice).enableEffect(owner);
	}
	
	private boolean canEnableNowEffect(List<Effect> effectList) {
		//Control if the effect can be enabled immediately
		
		if(effectList.size() == 1) {
			//If card has only one effect, control it and return if it is activated
			return checkActivable(effectList.get(0), 0);	
		}
		for(Effect effect : effectList) {
			//Else if card has more than one effect, control all the effect, and add it to the possibleChoice array
			//But return false because the card need to know the user's choice
			if(checkActivable(effect, effectList.indexOf(effect))) {
				possibleChoiceIndex.add(effectList.indexOf(effect));
				possibleChoice.add(effect);
			}
		}
		return false;
	}
	
	private boolean checkActivable(Effect effect, int index) {
		//Return true if the effect can be enabled
		boolean checker = true;
		
		//If the effect is obtain, cast the effect to get the obtain costs
		if(effect.getTypeOfEffect() == EffectType.OBTAIN) {
			Obtain obtainEffect = (Obtain)effect;
			Packet obtainCosts = obtainEffect.getCosts();
			
			//If there is at least one cost
			if(obtainCosts != null) {
				//Check if player can pay
				//If player can pay this cost, then checker = false because the effect cannot be immediately consumed
				//Else the effect cannot be payed nor added to the possible choice array
				checker = checkOwnerCanPay(obtainCosts);
				if(checker == true) {
					possibleChoice.add(effect);
					possibleChoiceIndex.add(index);
					checker = false;
				}
			}
		}
		return checker;
	}
	
	private boolean checkOwnerCanPay(Packet costs) {
		//Check if the owner can pay an effect
		return checkPlayerCanPay(costs, owner);
	}
	
	private boolean checkPlayerCanPay(Packet costs, Player player) {
		//Check if a generic player can pay a packet of costs
		for (Unit unit : costs) {
			if(unit.getQuantity() > player.getResource(unit.getResource()))
					return false;
		}
		return true;
	}
	
	private boolean checkRequirements(Player player) {
		if(requirements == null)
			return true;
		
		for(Packet requirement : requirements) {
			if(checkPlayerCanPay(requirement, player))
				return true;
		}
		return false;
	}
	
	private List<Packet> copyPacketList(List<Packet> start) {
		List<Packet> temp = new ArrayList<>();
		for(Packet packet : start) {
			temp.add(packet);
		}
		return temp;
	}
}
