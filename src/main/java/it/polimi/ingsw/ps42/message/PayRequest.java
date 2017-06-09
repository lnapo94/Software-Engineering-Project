package it.polimi.ingsw.ps42.message;

import java.util.List;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.Printable;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Player;

public class PayRequest extends CardRequest{
	//This request is created to know which cost player wants to pay
	
	private Player player;
	
	public PayRequest(Player player, Card card, List<Integer> possibleChoiceIndex, List<Printable> possibleChoice) {
		super(player.getPlayerID(), card, possibleChoiceIndex, possibleChoice);
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

	@Override
	public void accept(Visitor v) {
		//Method used to start the visit
		v.visit(this);
	}
}
