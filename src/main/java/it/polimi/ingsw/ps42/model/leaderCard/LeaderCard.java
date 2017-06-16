package it.polimi.ingsw.ps42.model.leaderCard;

import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.player.Player;

public class LeaderCard {
	
	final private String name;
	final private String description;
	final private LeaderRequirements requirements;
	
	//Effects to give a council privilege to the player when the card is discarded
	final private Obtain effect;
	
	//The owner
	private Player owner;
	
	//Effects of leader card
	final private Effect onceARoundEffect;
	final private Effect permanentEffect;
	
	public LeaderCard(String name, String description, LeaderRequirements requirements, Effect onceARoundEffect, Effect permanentEffect, Obtain effect) {
		this.name = name;
		this.description = description;
		this.requirements = requirements;
		this.onceARoundEffect = onceARoundEffect;
		this.permanentEffect = permanentEffect;
		this.owner = null;
		this.effect = effect;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public boolean canEnableCard() {
		return requirements.satisfyRequirement(owner);
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
		if(owner.getLeaderCardList().contains(this)) {
			owner.getLeaderCardList().remove(this);
			this.effect.enableEffect(owner);
		}
	}
	
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
	public boolean equals(LeaderCard card) {
		if(card == null)
			throw new NullPointerException();
		return this.getName() == card.getName();
	}
	
	@Override
	public LeaderCard clone() {
		return new LeaderCard(getName(), getDescription(), requirements.clone(), onceARoundEffect.clone(), permanentEffect.clone(), effect.clone());
	}

}
