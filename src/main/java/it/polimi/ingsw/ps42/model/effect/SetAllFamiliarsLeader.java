package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.player.Player;

public class SetAllFamiliarsLeader extends Effect {
	
	private int value;

	public SetAllFamiliarsLeader(int value) {
		super(EffectType.SET_ALL_FAMILIARS_LEADER);
		this.value = value;
	}

	@Override
	public void enableEffect(Player player) {
		player.getFamiliar(FamiliarColor.ORANGE).setValue(value);
		player.getFamiliar(FamiliarColor.BLACK).setValue(value);
		player.getFamiliar(FamiliarColor.WHITE).setValue(value);
	}

	@Override
	public Effect clone() {
		return new SetAllFamiliarsLeader(value);
	}

	@Override
	public String print() {
		// TODO Auto-generated method stub
		return null;
	}

}
