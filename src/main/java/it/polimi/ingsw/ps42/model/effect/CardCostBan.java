package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.Color;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.exception.WrongColorException;
import it.polimi.ingsw.ps42.model.player.CardList;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

import java.util.ArrayList;

import it.polimi.ingsw.ps42.model.Card;

public class CardCostBan extends Effect{
	//At the end of the match, the gamelogic calculate every cost of the indicated cards
	//and remove victory points from the player resources
	
	private Color color;

	public CardCostBan(Color color) {
		super(EffectType.YELLOW_COST_BAN); //TO-DO:discutere sul nome effetto e controllo sul colore
		this.color=color;
	}

	@Override
	public void enableEffect(Player player) {
		this.player=player;
		try{
			CardList deck=player.getCardList(color);
			for (Card card : deck) {
				ArrayList<Packet> costs=card.getCosts();
				//TO-DO: discutere assegnamento scomunica se solo su un costo o solo su carte gialle
			}
		
		}
		catch (WrongColorException e) {
			System.out.println("Applicazione della scomunica fallita causa sbagliata inizializzazione dell'effetto");
		}
	}

}
