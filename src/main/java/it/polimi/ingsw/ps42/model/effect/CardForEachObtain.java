package it.polimi.ingsw.ps42.model.effect;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

/**
 * Class that represent the Card For Each Obtain effect. In other words count the cards and give to
 * the player a number of some resources, specify in this class
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class CardForEachObtain extends Effect{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7012927820219406173L;
	private CardColor color;
	private Packet gains;
	
	/**
	 * Empty constructor of this class
	 */
	public CardForEachObtain() {
		super();
	}
	
	/**
	 * The constructor of this Class
	 * 
	 * @param color		The interested color card for this effect
	 * @param gains		The Packet of gains to give to the player with this effect
	 */
	public CardForEachObtain(CardColor color, Packet gains) {
		super(EffectType.CARD_FOR_EACH_OBTAIN);
		this.color = color;
		this.gains = gains;
	}

	/**
	 * Method used to enable this effect
	 */
	@Override
	public void enableEffect(Player player) {
		//For each card player has in his arraylist, he gains the "gains" packet in this object
		
		logger = Logger.getLogger(CardForEachObtainTest.class);
		
		logger.info("Effect: " + this.getTypeOfEffect() + " activated");
		
		this.player = player;
		
		int cardQuantity = player.getCardList(color).size();
		int quantityToObtain;
		Packet giveToPlayer = new Packet();
		
		for(Unit gain : gains) {
			quantityToObtain = gain.getQuantity() * cardQuantity;
			giveToPlayer.addUnit(new Unit(gain.getResource(), quantityToObtain));
		}
		this.player.increaseResource(giveToPlayer);		
	}
	
	/**
	 * Method to clone this effect to send it in a secure way to the Client
	 */
	@Override
	public Effect clone() {
		if(gains != null)
			return new CardForEachObtain(this.color, this.gains);
		else
			return null;
	}
	
	/**
	 * Method used to print this effect in the Client
	 */
	@Override
	public String print() {
		return "For each " + this.color + " card in player, he gains " + this.gains.toString();
	}

}
