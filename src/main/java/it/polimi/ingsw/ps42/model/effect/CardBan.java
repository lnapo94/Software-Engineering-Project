package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.Card;

import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.exception.WrongColorException;
import it.polimi.ingsw.ps42.model.player.Player;

/**
 * Class that represent the Card ban effect
 * At the end of the match, delete the corresponding cards in the player list
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class CardBan extends Effect{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3508591321623009266L;
	private CardColor color;
	
	/**
	 * Empty constructor of this class
	 */
	public CardBan() {
		super();
	}
	
	/**
	 * Constructor for this class
	 */
	public CardBan(CardColor color) {
		super(EffectType.CARD_BAN);
		this.color=color;
	}
	
	/**
	 * Method used to enable this effect
	 */
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

	/**
	 * Method used to print this effect in the Client
	 */
	@Override
	public String print() {
		return getTypeOfEffect().toString() + ": Applied to " + this.color.toString() + " cards";
	}
	
	/**
	 * Method to clone this effect to send it in a secure way to the Client
	 */
	@Override
	public CardBan clone() {
		CardColor cloneColor = this.color;
		return new CardBan(cloneColor);
	}

}
