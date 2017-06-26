package it.polimi.ingsw.ps42.model.effect;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.exception.WrongColorException;
import it.polimi.ingsw.ps42.model.player.Player;

public class CardBan extends Effect{
	//At the end of the match, delete the corresponding cards in the player list
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3508591321623009266L;
	private CardColor color;
	
	private transient Logger logger = Logger.getLogger(CardBan.class);
	
	
	public CardBan(CardColor color) {
		super(EffectType.CARD_BAN);
		this.color=color;
	}

	@Override
	public void enableEffect(Player player) {
		
			this.player=player;
			try{
				logger.info("Effect: " + this.getTypeOfEffect() + " activated");
				StaticList<Card> deck=player.getCardList(color);
				deck.removeAll();
			}
			catch (WrongColorException e) {
				logger.error("Ban failed beacause of a wrong initialization of the effect");
				logger.info(e);
			}
		
	}

	@Override
	public String print() {
		return getTypeOfEffect().toString() + ": Applied to " + this.color.toString() + " cards";
	}
	
	@Override
	public CardBan clone() {
		CardColor cloneColor = this.color;
		return new CardBan(cloneColor);
	}

}
