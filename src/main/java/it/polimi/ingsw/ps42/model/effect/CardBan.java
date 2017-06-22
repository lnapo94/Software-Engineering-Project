package it.polimi.ingsw.ps42.model.effect;

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
	
	
	public CardBan(CardColor color) {
		super(EffectType.CARD_BAN);
		this.color=color;
	}

	@Override
	public void enableEffect(Player player) {
		
			this.player=player;
			try{
				StaticList<Card> deck=player.getCardList(color);
				deck.removeAll();
			}
			catch (WrongColorException e) {
				System.out.println("Ban failed beacause of a wrong initialization of the effect");
			}
		
	}

	@Override
	public String print() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public CardBan clone() {
		CardColor cloneColor = this.color;
		return new CardBan(cloneColor);
	}

}
