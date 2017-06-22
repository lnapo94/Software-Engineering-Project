package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.player.Player;

public class SetSingleFamiliarLeader extends Effect{
	
	//Effect used in leader cards to set one familiar value
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3007031091349849711L;
	private FamiliarColor color;
	final private int value;

	public SetSingleFamiliarLeader(int value) {
		super(EffectType.SET_SINGLE_FAMILIAR_LEADER);
		this.value = value;
	}

	@Override
	public void enableEffect(Player player) {
		if(this.color != null)
			player.getFamiliar(color).setValue(value);
	}
	
	public void setFamiliarColor(FamiliarColor color) {
		if(this.color == null)
			this.color = color;
	}
	
	public int getIncrementValue() {
		return this.value;
	}

	@Override
	public Effect clone() {
		return new SetSingleFamiliarLeader(value);
	}

	@Override
	public String print() {
		// TODO Auto-generated method stub
		return null;
	}

}
