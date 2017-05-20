package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.Player;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;

public class NoFirstActionBan extends Effect {
	//Set a player variable, then the GameLogic knows the player cannot play the first action

	public NoFirstActionBan(EffectType typeOfEffect) {
		super(typeOfEffect);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enableEffect(Player player) {
		// TODO Auto-generated method stub
		
	}

}
