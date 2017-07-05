package it.polimi.ingsw.ps42.model.leaderCard;

import java.io.Serializable;

import it.polimi.ingsw.ps42.message.leaderRequest.LeaderFamiliarRequest;
import it.polimi.ingsw.ps42.message.leaderRequest.LeaderRequest;
import it.polimi.ingsw.ps42.model.effect.CouncilObtain;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.SetSingleFamiliarLeader;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.player.Player;

/**
 * Class that represents the leader cards of the game. This kind of card has one requirement to satisfy
 * and has a once a time effect or a permanent effect. Player can enable the card or can discard it
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class LeaderCard implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5133337512738200024L;
	final private String name;
	final private String description;
	final private LeaderRequirements requirements;
	
	//Effects to give a council privilege to the player when the card is discarded
	final private CouncilObtain effect;
	
	//The owner
	private transient Player owner;
	
	//Effects of leader card
	final private Effect onceARoundEffect;
	final private Effect permanentEffect;
	
	/**
	 * Constructor of a leader card
	 * @param name					The name to give to the leader card
	 * @param description			A brief description of the card
	 * @param requirements			A LeaderRequirements object to check the requirements of the leader card
	 * @param onceARoundEffect		The effect the player can enable each round
	 * @param permanentEffect		An effect that the player can enable one time and it's valid for the entire match
	 * @param effect				The effect to give to the player when he discard this card
	 */
	public LeaderCard(String name, String description, LeaderRequirements requirements, Effect onceARoundEffect, Effect permanentEffect, CouncilObtain effect) {
		this.name = name;
		this.description = description;
		this.requirements = requirements;
		this.onceARoundEffect = onceARoundEffect;
		this.permanentEffect = permanentEffect;
		this.owner = null;
		this.effect = effect;
	}
	
	/**
	 * Getter for the name of the card
	 * 
	 * @return		A string that represents the name of the card
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Getter for the description of the card
	 * 
	 * @return		A string that represents the description of the card
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * Method used to know if the card can be activated
	 * 
	 * @return True if the card can be activated, otherwise False
	 */
	public boolean canEnableCard() {
		return requirements.satisfyRequirement(owner);
	}
	
	/**
	 * Method used to apply the once a round effect of the card
	 */
	public void enableOnceARoundEffect() {
		if(onceARoundEffect != null) {
			if(isEffectWithRequirements(onceARoundEffect)) {
				LeaderRequest request = new LeaderFamiliarRequest(owner.getPlayerID(), this);
				owner.addLeaderRequest(request);
			}				
			else
				onceARoundEffect.enableEffect(this.owner);
		}
	}
	
	/**
	 * Method used to enable the once a round effect when it requires a familiar
	 * 
	 * @param player	The interested player
	 * @param color		The color of the familiar
	 */
	public void enableOnceARoundEffect(Player player, FamiliarColor color) {
		SetSingleFamiliarLeader effect = (SetSingleFamiliarLeader)onceARoundEffect;
		effect.setFamiliarColor(color);
		effect.enableEffect(player);
	}
	
	/**
	 * Method used to enable the permanent effect when it requires a familiar
	 * 
	 * @param player	The interested player
	 * @param color		The color of the familiar
	 */
	public void enablePermanentEffect(Player player, FamiliarColor color) {
		SetSingleFamiliarLeader effect = (SetSingleFamiliarLeader)permanentEffect;
		effect.setFamiliarColor(color);
		effect.enableEffect(player);
	}
	
	/**
	 * Method used to enable the permanent effect of the card
	 */
	public void enablePermanentEffect() {
		if(permanentEffect != null)
			permanentEffect.enableEffect(this.owner);
	}
	
	/**
	 * Method used to control if the leader card has a request
	 * @param effect	The effect to check
	 * @return			True if the effect needs a request, otherwise False
	 */
	private boolean isEffectWithRequirements(Effect effect) {
		
		if(effect.getTypeOfEffect() == EffectType.SET_SINGLE_FAMILIAR_LEADER)
			return true;
		return false;
	}
	
	/**
	 * Delete the card from the owner and add a council request to the owner
	 * This method can be used only if the card isn't yet activated
	 */
	public void discard() {

		if(owner.getLeaderCardList().contains(this)) {
			owner.getLeaderCardList().remove(this);
			this.effect.enableEffect(owner);
		}
	}
	
	/**
	 * Method to set to the card the owner
	 * 
	 * @param owner		The owner of the card
	 */
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
	/**
	 * Method used to control if the leader card name is equals to another card
	 * @param card	The card to check with this one
	 * @return		True if the card is equal to this one, otherwise False
	 */
	public boolean equals(LeaderCard card) {
		if(card == null)
			throw new NullPointerException();
		return this.getName() == card.getName();
	}
	
	/**
	 * Method used to clone this card requirements'
	 * @return	A copy of LeaderRequirements of this card
	 */
	public LeaderRequirements getRequirements() {
		return requirements.clone();
	}
	
	/**
	 * Method used to clone the once a round effect
	 * @return	A copy of once a round effect of this card
	 */
	public Effect getOnceARoundEffect() {
		Effect temp = null;
		if(onceARoundEffect != null)
			temp = onceARoundEffect.clone();
		return temp;
	}
	
	/**
	 * Method used to clone the permanent effect
	 * @return	A copy of permanent effect of this card
	 */
	public Effect getPermanentEffect() {
		Effect temp = null;
		if(permanentEffect != null)
			temp = permanentEffect.clone();
		return temp;
	}
	
	/**
	 * Method used to show this effect thanks to a string
	 */
	@Override
	public String toString() {
		String stringToShow = new String("Name: " + getName() + "| Description: " + getDescription() + "\n");
		
		if(getOnceARoundEffect() != null)
			stringToShow = stringToShow + "Once a round effect: " + getOnceARoundEffect().print() + "\n";
		
		if(getPermanentEffect() != null)
			stringToShow = stringToShow + "Permanent effect: " + getPermanentEffect().print();
		
		return stringToShow;
	}
	
	/**
	 * Method used to clone this leader card
	 */
	@Override
	public LeaderCard clone() {
		LeaderRequirements tempRequirements = null;
		Effect tempOnceARoundEffect = null;
		Effect tempPermanentEffect = null;
		CouncilObtain tempEffect = null;
		
		if(requirements != null) {
			tempRequirements = requirements.clone();
		}
		
		if(onceARoundEffect != null) {
			tempOnceARoundEffect = onceARoundEffect.clone();
		}
		
		if(permanentEffect != null) {
			tempPermanentEffect = permanentEffect.clone();
		}
		
		if(effect != null) {
			tempEffect = effect.clone();
		}
		
		return new LeaderCard(getName(), getDescription(), tempRequirements, tempOnceARoundEffect, tempPermanentEffect, tempEffect);
	}

}
