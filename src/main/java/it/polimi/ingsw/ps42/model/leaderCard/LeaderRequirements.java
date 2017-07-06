package it.polimi.ingsw.ps42.model.leaderCard;

import java.io.Serializable;
import java.util.HashMap;

import it.polimi.ingsw.ps42.model.Printable;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

/**
 * Class used to represents the particular leader requirements
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class LeaderRequirements implements Serializable, Printable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8459959317570182779L;

	//Variable used to control the requirements
	private boolean checker;
	
	//Used in leader card for normal requirements
	private Packet resourceRequirements;
	
	//Used in case the leader card has some cards requirements
	private HashMap<CardColor, Integer> cardRequirements;
	
	/**
	 * Constructor of leader requirements
	 * @param resourceRequirements	Packet that represents the resource requirements of the leader card
	 * @param cardRequirements		An HashMap that represents the cards requirements of the leader card
	 */
	public LeaderRequirements(Packet resourceRequirements, HashMap<CardColor, Integer> cardRequirements) {
		this.resourceRequirements = resourceRequirements;
		this.cardRequirements = cardRequirements;
	}
	
	/**
	 * Method used to verify if the player satisfy the requirements
	 * 
	 * @param owner		The card's owner
	 * @return			True if the requirements are satisfied, otherwise False
	 */
	public boolean satisfyRequirement(Player owner) {
		//Check if it is possible to enable the leader card by the control
		//of this requirements
		checker = true;
		
		//If color == all, i control all the player cards
		//If this requirement is in the HashMap, then I control only this requirement
		//because is the most important
		if(cardRequirements != null) {
			if(cardRequirements.get(CardColor.ALL) != null && cardRequirements.get(CardColor.ALL) > 0) {
				//Control all the cards, if one of the decks in player satisfies
				//the requirements, then all the cards requirements are satisfied
				int allCardRequirement = cardRequirements.get(CardColor.ALL);
				
				if(owner.getCardList(CardColor.GREEN).size() >= allCardRequirement)
					return true;
				if(owner.getCardList(CardColor.YELLOW).size() >= allCardRequirement)
					return true;
				if(owner.getCardList(CardColor.BLUE).size() >= allCardRequirement)
					return true;
				if(owner.getCardList(CardColor.VIOLET).size() >= allCardRequirement)
					return true;
				return false;
			}
			
			//Lambda expression used to control all the cards requirements
			cardRequirements.forEach((color, cardRequirement) -> {
				if(owner.getCardList(color).size() < cardRequirement) {
					checker = false;
				}
			});
		}
		
		if(resourceRequirements != null)
			for(Unit unit : resourceRequirements) {
				if(unit.getQuantity() > owner.getResource(unit.getResource()))
					checker = false;
			}
		
		return checker;
	}
	
	/**
	 * Method used to clone the leader requirements of the card
	 */
	@Override
	public LeaderRequirements clone() {
		Packet tempResourceRequirements = null;
		HashMap<CardColor, Integer> tempCardRequirements = new HashMap<>();
		
		if(resourceRequirements != null)
			tempResourceRequirements = resourceRequirements.clone();
		
		if(cardRequirements != null) {
			cardRequirements.forEach((color, cardRequirement) -> {
				tempCardRequirements.put(color, cardRequirement);
			});
		}
		return new LeaderRequirements(tempResourceRequirements, tempCardRequirements);
	}
	
	/**
	 * Method used to print this requirements
	 */
	@Override
	public String print() {
		return "Card Requirements: " + this.cardRequirements.toString() + "\n" +
				"Resource Requirements: " + this.resourceRequirements.print();
	}
}
