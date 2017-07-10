package it.polimi.ingsw.ps42.message;

import java.util.List;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.Printable;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

/**
 * This request is created to know which cost player wants to pay
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class PayRequest extends CardRequest{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3625839207062064800L;
	private final Packet discount;
	
	public PayRequest(String playerID, Card card, List<Integer> possibleChoiceIndex, List<Printable> possibleChoice, Packet discount) {
		super(playerID, card, possibleChoiceIndex, possibleChoice);
		this.discount = discount;
	}
	
	@Override
	public void apply(Player player) {
		try {
			card.payCard(player, possibleChoiceIndex.get(userChoice), discount);
		} catch (NotEnoughResourcesException e) {
			throw new ArithmeticException("Card cannot be payed, control goes down");
		}
	}
	
	/**
	 * Method used to visit this message
	 */
	@Override
	public void accept(Visitor v) {
		//Method used to start the visit
		v.visit(this);
	}
}
