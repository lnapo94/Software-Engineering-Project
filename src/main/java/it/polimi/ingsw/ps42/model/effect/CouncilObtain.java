package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.Player;

public class CouncilObtain extends Effect {
	//Create a request for the gamelogic to obtain the resources from the council privileges
	
	private int quantity;
	private Request request;

	public CouncilObtain(EffectType typeOfEffect) {
		super(typeOfEffect);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enableEffect(Player player) {
		// TODO Auto-generated method stub
		
	}
	

}
