package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public class ObtainBan extends Effect{
	
	private Packet costs;
	private Resource resource;
	
	public ObtainBan(Packet costs) {
		super(EffectType.OBTAIN_BAN);
		this.costs = costs;
	}

	@Override
	public void enableEffect(Player player) {
		//Enable the ObtainBan effect, hence decrease player's resource if
		//he gains an exactly resource
		for(Unit cost : costs)
		if(resource == cost.getResource()) {
			Packet packetForDecrement = new Packet();
			packetForDecrement.addUnit(cost);
			try {
				player.decreaseResource(packetForDecrement);
			} catch (NotEnoughResourcesException e) {
				player.setToZero(cost.getResource());
			} finally {
				//Finally case is done because every time this method is called
				//is needed a control with player resource
				this.resource = null;
			}
		}
	}
	
	public void setResource(Resource resource) {
		//Used to set the Resource for a control
		this.resource = resource;
	}
	
	@Override
	public ObtainBan clone() {
		return new ObtainBan(costs.clone());
	}
	
	@Override
	public String print() {
		// TODO Auto-generated method stub
		return null;
	}

}
