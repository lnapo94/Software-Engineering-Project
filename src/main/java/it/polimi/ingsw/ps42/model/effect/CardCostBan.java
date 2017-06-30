package it.polimi.ingsw.ps42.model.effect;

import java.util.List;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.exception.WrongColorException;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

/**
 *	At the end of the match, the gamelogic calculate the cost of woods and stones of the indicated cards
 *  and remove victory points from the player resources
 *  @author Luca Napoletano, Claudio Montanari
 *
 */
public class CardCostBan extends Effect{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7122058016852083837L;
	private CardColor color;
	
	/**
	 * Empty constructor of this class
	 */
	public CardCostBan() {
		super();
	}

	/**
	 * Constructor for this class
	 * @param color		The color of which cards are counted by this ban
	 */
	public CardCostBan(CardColor color) {
		super(EffectType.YELLOW_COST_BAN); //TO-DO:discutere sul nome effetto e controllo sul colore
		this.color=color;
	}

	/**
	 * Method used to enable this effect
	 */
	@Override
	public void enableEffect(Player player) {
		this.player=player;
		int banCost=0;
		try{
			logger.info("Effect: " + this.getTypeOfEffect() + " activated");
			StaticList<Card> deck = player.getCardList(color);	
			for (Card singleCard : deck) {							//For each card of the player with the def. color
				List<Packet> costs=singleCard.getCosts();		//Obtain for every cost the quantity of wood and stone
				for (Packet singleCost : costs) {
					banCost+=defineCost(singleCost);
				}
			}
			Unit u=new Unit(Resource.VICTORYPOINT, banCost);	//Convert the total amount in victory point to be subtracted later
			Packet p=new Packet();
			p.addUnit(u);
			player.decreaseResource(p); 						//Apply the ban to the player
		}
		catch (WrongColorException e) {
			logger.error("Ban failed beacause of a wrong initialization of the effect");
			logger.info(e);
		} catch (NotEnoughResourcesException e) {
			logger.info("Player hasn't enough resources, set it to zero");
			logger.info(e);
			player.setToZero(Resource.VICTORYPOINT);
		}
	}
	
	/**
	 * Defines the single cost of a single card in terms of Woods and Stones
	 * @param cost		The cost of this ban
	 * @return			The quantity of the cost
	 */
	private int defineCost (Packet cost){
		
		List<Unit> tempUnit = cost.getPacket();
		int quantity=0;
		for (Unit singleUnit : tempUnit) {
			if(singleUnit.getResource()==Resource.WOOD || singleUnit.getResource()==Resource.STONE){
				quantity+=singleUnit.getQuantity();
			}
		}
		return quantity;
		
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
	public CardCostBan clone() {
		CardColor cloneColor = this.color;
		return new CardCostBan(cloneColor);
	}

}
