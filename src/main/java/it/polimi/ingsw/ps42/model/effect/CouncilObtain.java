package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.request.Request;

public class CouncilObtain extends Effect {
	//Create a request for the gamelogic to obtain the resources from the council privileges
	
	private int quantity;
	private Request request;

	public CouncilObtain(int quantity) {
		super(EffectType.COUNCIL_OBTAIN);
		this.quantity=quantity;
		
	}

	@Override
	public void enableEffect(Player player) {
		
		this.player=player;
		//TO-DO: implementare un tipo di richiesta ad hoc?
	}
	

}
