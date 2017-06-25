package it.polimi.ingsw.ps42.model.effect;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

public class NoFirstActionBan extends Effect {
	//Set a player variable, then the GameLogic knows the player cannot play the first action

	/**
	 * 
	 */
	private static final long serialVersionUID = -4347788955119043485L;
	
	private transient Logger logger = Logger.getLogger(NoFirstActionBan.class);

	public NoFirstActionBan() {
		super(EffectType.NO_FIRST_ACTION_BAN);
	}

	@Override
	public void enableEffect(Player player) {
		logger.info("Effect: " + this.getTypeOfEffect() + " activated");
		this.player=player;
		player.setCanPlay(false);
	}

	@Override
	public String print() {
		return "Player can't to his first action";
	}

	@Override
	public NoFirstActionBan clone() {
		return new NoFirstActionBan();
	}
}
