package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.action.ActionPrototype;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

public class DoAction extends Effect{
	//Allow player to do another action
	//This creates an ActionPrototype, that will be consumed by the GameLogic
	
	private ActionType type;
	private int actionLevel;
	private Packet discount;
	private ActionPrototype actionPrototype;

	public DoAction(EffectType typeOfEffect) {
		super(typeOfEffect);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enableEffect(Player player) {
		// TODO Auto-generated method stub
		
	}

}
