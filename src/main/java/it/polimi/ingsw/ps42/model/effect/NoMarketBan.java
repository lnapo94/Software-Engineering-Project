package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

public class NoMarketBan extends Effect {
	//Disable the Market for the player with the boolean variable setted to false

	/**
	 * 
	 */
	private static final long serialVersionUID = -5719113500926713271L;

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
		return "Player can't position his familiars in market";
	}

	@Override
	public NoMarketBan clone() {
		return new NoMarketBan();
	}
}
