package it.polimi.ingsw.ps42.model.effect;


import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

public class Obtain extends Effect{
	//Obtain the indicated resources by paying the following costs
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2273882055244433413L;
	private Packet costs;
	private Packet gains;
	private CouncilObtain councilObtain;
	
	public Obtain() {
		super();
	}
	
	public Obtain(Packet costs, Packet gains, CouncilObtain councilObtain) {
		
		super(EffectType.OBTAIN);
		logger = Logger.getLogger(Obtain.class);
		this.costs=costs;
		this.gains=gains;
		this.councilObtain = councilObtain;
	}
	
	public Packet getCosts() {
		return costs;
	}
	
	public Packet getGains() {
		return gains;
	}

	public CouncilObtain getCouncilObtain() {
		return councilObtain;
	}
	
	@Override
	public void enableEffect(Player player) {
		/*In this case the method decrease the cost and increase the gain 
		 * in the player. The increase/decrease are done in the secondary 
		 * HashMap of player resources
		 */
		
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

	@Override
	public String print() {
		String stringToShow = new String();
		if(costs != null)
			stringToShow = stringToShow + "Pay:" + this.costs.print() + " ";
		if(gains != null)
			stringToShow = stringToShow + "Gain:" +  this.gains.print();
		return stringToShow;
	}
	
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
