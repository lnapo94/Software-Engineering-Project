package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

public class NoMarketBan extends Effect {
	//Disable the Market for the player with the boolean variable setted to false

	public NoMarketBan() {
		
		super(EffectType.NO_MARKET_BAN);
	}

	@Override
	public void enableEffect(Player player) {
		
		this.player=player;
		player.setNoMarketBan();
		
	}

	@Override
	public String print() {
		// TODO Auto-generated method stub
		return null;
	}

}
