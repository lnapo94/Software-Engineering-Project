package it.polimi.ingsw.ps42.model.effect;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

/**
 * ObtainBan Class. This effect is the classical obtain presents in bans cards. For example:
 * 		Earn one less money every time the player earns money...
 * This effect is taken from file, so it can be customizable
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class ObtainBan extends Effect{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1609999679326262487L;
	private Packet costs;
	private Resource resource;
	
	/**
	 * Simple constructor to enable the logger
	 */
	public ObtainBan() {
		super();
	}
	
	/**
	 * Construct of this effect
	 * 
	 * @param costs 	Packet that represent the costs of this effect. This costs will be applied
	 * 					each time the player earns the specify requirement
	 */
	public ObtainBan(Packet costs) {
		super(EffectType.OBTAIN_BAN);
		this.costs = costs;
	}

	/**
	 * Enable the ObtainBan effect, hence decrease player's resource if
	 * he gains an exactly resource
	 */
	@Override
	public void enableEffect(Player player) {
		logger = Logger.getLogger(ObtainBan.class);
		logger.info("Effect: " + this.getTypeOfEffect() + " activated");
		for(Unit cost : costs)
		if(resource == cost.getResource()) {
			Packet packetForDecrement = new Packet();
			packetForDecrement.addUnit(cost);
			try {
				player.decreaseResource(packetForDecrement);
			} catch (NotEnoughResourcesException e) {
				logger.info("Player has enough resources, so set it to zero");
				logger.info(e);
				player.setToZero(cost.getResource());
			} finally {
				//Finally case is done because every time this method is called
				//is needed a control with player resource
				this.resource = null;
			}
		}
	}
	
	/**
	 * Method used to set the incremented resource in player to check with this effect
	 * @param resource		The incremented resource to check
	 */
	public void setResource(Resource resource) {
		//Used to set the Resource for a control
		this.resource = resource;
	}
	
	/**
	 * Method used to clone this effect
	 */
	@Override
	public ObtainBan clone() {
		return new ObtainBan(costs.clone());
	}
	
	/**
	 * Method used to print this effect in the view
	 */
	@Override
	public String print() {
		String stringToShow = new String();
		for(Unit unit : costs) {
			stringToShow = stringToShow + "Everytime player gains " + unit.getResource() + ", he gains " + unit.getQuantity() + " " + unit.getResource() + " less";
		}
		return stringToShow;
	}

}
