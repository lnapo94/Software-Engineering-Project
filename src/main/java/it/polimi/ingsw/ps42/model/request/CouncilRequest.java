package it.polimi.ingsw.ps42.model.request;

import java.util.List;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.enumeration.Resource;

public class CouncilRequest extends Request {
	
	private List<Resource> choice;
	private final int quantity;
	
	public CouncilRequest(Card card, int quantity) {
		super(card);
		this.quantity=quantity;
	}
	
	public List<Resource> getChoice() {
		return choice;
	}
	
	
	@Override
	public void apply() {
		// TODO Auto-generated method stub
		
	}

	
}
