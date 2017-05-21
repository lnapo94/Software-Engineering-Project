package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

public class NoFirstActionBan extends Effect {
	//Set a player variable, then the GameLogic knows the player cannot play the first action

	public NoFirstActionBan() {
		super(EffectType.NO_FIRST_ACTION_BAN);
	}

	@Override
	public void enableEffect(Player player) {
		this.player=player;
		player.setCanPlay(false);
	}

}
