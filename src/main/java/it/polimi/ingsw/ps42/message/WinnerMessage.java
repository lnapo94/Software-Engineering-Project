package it.polimi.ingsw.ps42.message;

import java.util.List;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;

public class WinnerMessage extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7365083657811667357L;

	private List<String> result;
	
	public WinnerMessage(String playerID, List<String> result) {
		
		super(playerID);
		this.result = result;
		
	}
	
	public List<String> getResult() {
		return result;
	}
	
	@Override
	public void accept(Visitor v) {
		
		v.visit(this);
	}
	

}
