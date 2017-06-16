package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

public class NoMilitaryRequirementsLeader extends Effect{
	
	public NoMilitaryRequirementsLeader() {
		super(EffectType.NO_MILITARY_REQUIREMENTS);
	}

	@Override
	public void enableEffect(Player player) {
		player.setMilitaryRequirements();
	}

	@Override
	public Effect clone() {
		return this;
	}

	@Override
	public String print() {
		// TODO Auto-generated method stub
		return null;
	}

}
