package it.polimi.ingsw.ps42.model.request;

import java.util.List;

import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.player.Player;

public class CouncilRequest {
	
	private List<Obtain> choice;
	private final int quantity;
	private final List<Obtain> possibleChoice;
	
	public CouncilRequest(List<Obtain> possibleChoice, int quantity) {
		this.possibleChoice=possibleChoice;
		this.quantity=quantity;
	}
	
	public List<Obtain> getChoice() {
		return possibleChoice;
	}
	
	public boolean addChoice(int index){
		//controlla scelta diversa da altre
		return false;
	}
	
	public void apply(Player player) {
		
	}

	
}
