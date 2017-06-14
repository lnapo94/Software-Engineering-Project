package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

public class NoMoneyMalusLeader extends Effect{

	protected NoMoneyMalusLeader() {
		super(EffectType.NO_MONEY_MALUS);
	}

	@Override
	public void enableEffect(Player player) {
		player.setNoMoneyMalus();
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
