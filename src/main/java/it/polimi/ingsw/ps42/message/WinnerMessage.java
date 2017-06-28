package it.polimi.ingsw.ps42.message;

import java.util.List;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;

/**
 * Message sent when a match finished
 * @author Luca Napoletano, Claudio Montanari
 *
 */
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
	
	/**
	 * Method used to visit this message
	 */
	@Override
	public void accept(Visitor v) {
		
		v.visit(this);
	}
	

}
