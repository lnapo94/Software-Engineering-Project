package it.polimi.ingsw.ps42.message;

import java.util.List;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.Printable;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Player;

public class PayRequest extends CardRequest{
	
	private Player player;
	
	public PayRequest(Player player, Card card, List<Integer> possibleChoiceIndex, List<Printable> possibleChoice) {
		super(card, possibleChoiceIndex, possibleChoice);
		this.player = player;
	}
	
	@Override
	public void apply() {
		try {
			card.payCard(player, userChoice);
		} catch (NotEnoughResourcesException e) {
			throw new ArithmeticException("Card cannot be payed, control goes down");
		}
	}
}
