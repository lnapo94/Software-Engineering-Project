package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.Player;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

public class IncreaseAction extends Effect {
	//Create a particular effect that increments the level of another Effect
	
	private ActionType type;
	private int value;
	private Packet discount;
	
	//variable used to know if the effect is activated yet or not
	private boolean yetActivated;

	public IncreaseAction(EffectType typeOfEffect) {
		super(typeOfEffect);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enableEffect(Player player) {
		// TODO Auto-generated method stub
		
	}
	

}
