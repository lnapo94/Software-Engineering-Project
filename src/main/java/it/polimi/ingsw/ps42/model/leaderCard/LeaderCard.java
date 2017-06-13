package it.polimi.ingsw.ps42.model.leaderCard;

import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.player.Player;

public class LeaderCard {
	
	final private String name;
	final private String description;
	final private LeaderRequirements requirements;
	
	//The owner
	private Player owner;
	
	//Effects of leader card
	final private Effect onceARoundEffect;
	final private Effect permanentEffect;
	
	public LeaderCard(String name, String description, LeaderRequirements requirements, Effect onceARoundEffect, Effect permanentEffect) {
		this.name = name;
		this.description = description;
		this.requirements = requirements;
		this.onceARoundEffect = onceARoundEffect;
		this.permanentEffect = permanentEffect;
		this.owner = null;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public boolean canEnableCard() {
		return false;
	}
	
	public void enableOnceARoundEffect() {
		if(onceARoundEffect != null)
			onceARoundEffect.enableEffect(this.owner);
	}
	
	public void enablePermanentEffect() {
		if(permanentEffect != null)
			permanentEffect.enableEffect(this.owner);
	}
	
	public void discard() {
		//Delete the card from the owner and add a council request to the owner
		//This method can be used only if the card isn't yet activated
	}
	
	public boolean equals(LeaderCard card) {
		return this.getName() == card.getName();
	}
	
	@Override
	public LeaderCard clone() {
		return new LeaderCard(getName(), getDescription(), requirements.clone(), onceARoundEffect.clone(), permanentEffect.clone());
	}

}