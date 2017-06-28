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
		logger.info("Effect: " + this.getTypeOfEffect() + " activated");
		this.player=player;
		player.disableBonusInTower();
	}

	@Override
	public String print() {
		return "Now player can't take the tower bonuses";
	}

	@Override
	public NoBonusInTower clone() {
		return new NoBonusInTower();
	}
}
