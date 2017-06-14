package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.message.LeaderRequest;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.player.Player;

public class IncreaseSingleFamiliarLeader extends Effect{
	
	//Effect used in leader cards to set one familiar value
	
	private FamiliarColor color;
	final private int value;

	protected IncreaseSingleFamiliarLeader(int value) {
		super(EffectType.INCREASE_SINGLE_FAMILIAR_LEADER);
		this.value = value;
	}

	@Override
	public void enableEffect(Player player) {
		LeaderRequest request = new LeaderRequest(player.getPlayerID(), false);
		player.addLeaderRequest(request);
	}
	
	public void apply() {
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
		return new IncreaseSingleFamiliarLeader(value);
	}

	@Override
	public String print() {
		// TODO Auto-generated method stub
		return null;
	}

}
