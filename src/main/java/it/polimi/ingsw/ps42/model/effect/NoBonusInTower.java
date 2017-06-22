package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

public class NoBonusInTower extends Effect{
	//This method set a player variable to disable the Tower Bonus

	/**
	 * 
	 */
	private static final long serialVersionUID = 3468865507577574258L;

	public NoBonusInTower() {
		super(EffectType.NO_TOWER_BONUS);
		
	}

	@Override
	public void enableEffect(Player player) {
		this.player=player;
		player.disableBonusInTower();
	}

	@Override
	public String print() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NoBonusInTower clone() {
		return new NoBonusInTower();
	}
}
