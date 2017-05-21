package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.Color;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.exception.WrongColorException;
import it.polimi.ingsw.ps42.model.player.CardList;
import it.polimi.ingsw.ps42.model.player.Player;

public class CardBan extends Effect{
	//At the end of the match, delete the corresponding cards in the player list
	
	private Color color;
	
	
	public CardBan(Color color) {
		super(EffectType.CARD_BAN);
		this.color=color;
	}

	@Override
	public void enableEffect(Player player) {
		this.player=player;
		try{
			CardList deck=player.getCardList(color);
			deck.removeAll();
		}
		catch (WrongColorException e) {
			System.out.println("Scomunica fallita causa sbagliata implementazione dell'effetto");
		}
		
	}

}
