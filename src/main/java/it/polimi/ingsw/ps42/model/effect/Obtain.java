package it.polimi.ingsw.ps42.model.effect;



import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

/**
 * Obtain the indicated resources by paying the following costs
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class Obtain extends Effect{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2273882055244433413L;
	private Packet costs;
	private Packet gains;
	private CouncilObtain councilObtain;
	
	/**
	 * Simple constructor of this effect
	 */
	public Obtain() {
		super();
	}
	
	/**
	 * The constructor of this effect
	 * 
	 * @param costs				The costs the player must pay to enable this effect
	 * @param gains				The gains the player earns when he pays the costs
	 * @param councilObtain		A possible council privilege or more
	 */
	public Obtain(Packet costs, Packet gains, CouncilObtain councilObtain) {
		
		super(EffectType.OBTAIN);
		this.costs=costs;
		this.gains=gains;
		this.councilObtain = councilObtain;
	}
	
	/**
	 * Getter for the costs of this effect
	 * @return		The Packet of costs in this effect
	 */
	public Packet getCosts() {
		return costs;
	}
	
	/**
	 * Getter for the gains of this effect
	 * @return		The Packet of gains in this effect
	 */
	public Packet getGains() {
		return gains;
	}

	/**
	 * Getter for the council privileges in this effect
	 * 
	 * @return		A CouncilObtain effect which represent the council privileges
	 */
	public CouncilObtain getCouncilObtain() {
		return councilObtain;
	}
	
	/**
	 * In this case the method decrease the cost and increase the gain 
	 * in the player. The increase/decrease are done in the secondary 
	 * HashMap of player resources
	 */
	@Override
	public void enableEffect(Player player) {
		logger = Logger.getLogger(Obtain.class);
		this.player=player;
		try {
			logger.info("Effect: " + this.getTypeOfEffect() + " activated");
			player.decreaseResource(costs);
		} catch (NotEnoughResourcesException e) {
			player.increaseResource(costs);
			logger.error("Player hasn't enough resources...");
			logger.info(e);
			throw new ArithmeticException("Effect was enabled, but it can't be payed");
		}
		player.increaseResource(gains);
		
		if(councilObtain != null)
			councilObtain.enableEffect(player);
		
	}

	/**
	 * Method used to print this effect in the view
	 */
	@Override
	public String print() {
		String stringToShow = new String();
		if(costs != null)
			stringToShow = stringToShow + "Pay:" + this.costs.print() + " ";
		if(gains != null)
			stringToShow = stringToShow + "Gain:" +  this.gains.print();
		return stringToShow;
	}
	
	/**
	 * Method used to copy this effect
	 */
	@Override
	public Obtain clone() {
		Packet gainCopy = null;
		Packet costCopy = null;
		CouncilObtain councilObtainCopy = null;
		if(this.gains != null)
			gainCopy = gains.clone();
		if(this.costs != null)
			costCopy = costs.clone();
		if(this.councilObtain != null)
			councilObtainCopy = councilObtain.clone();
		return new Obtain(costCopy, gainCopy, councilObtainCopy);
	}

}
