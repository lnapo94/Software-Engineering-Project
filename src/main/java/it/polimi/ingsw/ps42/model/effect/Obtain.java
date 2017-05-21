package it.polimi.ingsw.ps42.model.effect;


import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

public class Obtain extends Effect{
	//Obtain the indicated resources by paying the following costs
	
	private Packet costs;
	private Packet gains;

	public Obtain(Packet costs, Packet gains) {
		
		// TODO Auto-generated constructor stub
		super(EffectType.OBTAIN);
		this.costs=costs;
		this.gains=gains;
	}
	
	public Packet getCosts() {
		return costs;
	}
	
	public Packet getGains() {
		return gains;
	}

	
	@Override
	public void enableEffect(Player player) {
		/*In this case the method decrease the cost and increase the gain 
		 * in the player. The increase/decrease are done in the secondary 
		 * HashMap of player resources
		 */
		
		this.player=player;
		player.decreaseResource(costs);
		player.increaseResource(gains);

		
	}
	
	

}
