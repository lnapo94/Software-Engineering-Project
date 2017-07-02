package it.polimi.ingsw.ps42.model.effect;



import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

/**
 * Class that represents the For Each obtain, in other words, when this effect is activated, 
 * the player obtain something for each requirements he has among his resources
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class ForEachObtain extends Effect{
	//Obtain some resources for each requirements the player has
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1063976216381694601L;
	private Packet requirements;
	private Packet gains;
	
	/**
	 * Simple empty constructor used to initialize the logger
	 */
	public ForEachObtain() {
		super();
	}

	/**
	 * The constructor of this effect
	 * 
	 * @param requirements	The packet player should have to earn some resources
	 * @param gains			The gains given to the player for each requirements
	 */
	public ForEachObtain(Packet requirements, Packet gains) {
		super(EffectType.FOR_EACH_OBTAIN);
		this.requirements=requirements;
		this.gains=gains;
		
	}

	/**
	 * Method used to apply this effect
	 */
	@Override
	public void enableEffect(Player player) {
		
		logger = Logger.getLogger(ForEachObtain.class);
		
		logger.info("Effect: " + this.getTypeOfEffect() + " activated");
		
		this.player=player;
		int quantity;
		int quantityToObtain;
		Packet packet=new Packet();
		for (Unit unit1 : requirements) {
			quantity=player.getResource(unit1.getResource());	//ottengo da giocatore la quantità della risorsa richiesta 
			quantity=quantity/unit1.getQuantity();				//divido questa quantità per quella richiesta 
			for (Unit unit2 : gains) {
				quantityToObtain=quantity*unit2.getQuantity();		//la quantità finale da ottenere va moltiplicata per la quantità in gains
				packet.addUnit(new Unit(unit2.getResource(), quantityToObtain));	
			}
		}
		player.increaseResource(packet);
	}

	/**
	 * Method used to print this effect in the view
	 */
	@Override
	public String print() {
		return "Player obtains : " + this.gains.print() + " for each " + this.requirements.print() + " he has";
	}

	/**
	 * Method used to clone this effect
	 */
	@Override
	public ForEachObtain clone() {
		Packet cloneRequirements = null;
		Packet cloneGains = null;
		if( requirements != null)
			cloneRequirements = requirements.clone();
		if( gains != null)
			cloneGains = gains.clone();
		return new ForEachObtain( cloneRequirements, cloneGains);
	}
}
