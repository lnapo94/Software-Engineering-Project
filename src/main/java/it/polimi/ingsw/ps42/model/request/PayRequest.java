package it.polimi.ingsw.ps42.model.request;

import java.util.List;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.Printable;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

public class PayRequest extends CardRequest{
	
	private Player player;
	private Packet discount;
	
	public PayRequest(Player player, Card card, Packet discount, List<Integer> possibleChoiceIndex, List<Printable> possibleChoice) {
		super(card, possibleChoiceIndex, possibleChoice);
		this.player = player;
	}
	
	@Override
	public void apply() {
		try {
			card.payCard(player, discount, userChoice);
		} catch (NotEnoughResourcesException e) {
			throw new ArithmeticException("Card cannot be payed, control goes down");
		}
	}
}
