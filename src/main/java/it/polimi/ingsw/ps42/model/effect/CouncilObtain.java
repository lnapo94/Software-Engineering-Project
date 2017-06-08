package it.polimi.ingsw.ps42.model.effect;

import java.util.List;

import it.polimi.ingsw.ps42.message.CouncilRequest;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;


public class CouncilObtain extends Effect {
	//Create a request for the gamelogic to obtain the resources from the council privileges
	
	private final int quantity;
	private final List<Obtain> possibleConversion;

	public CouncilObtain(int quantity,List<Obtain> possibleConversion ) {
		super(EffectType.COUNCIL_OBTAIN);
		this.possibleConversion=possibleConversion;
		this.quantity=quantity;
		
	}

	@Override
	public void enableEffect(Player player) {
		
		this.player=player;
		CouncilRequest request=new CouncilRequest(possibleConversion, quantity);
		player.addCouncilRequests(request);
		
	}

	@Override
	public String print() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
