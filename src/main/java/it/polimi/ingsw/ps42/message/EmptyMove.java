package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;

/**
 * Simple message used to notify the Controller that the specify player doesn't want to do a move
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class EmptyMove extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5956002235593087386L;

	public EmptyMove(String playerID) {
		super(playerID);
		
	}

	/**
	 * Method used to visit this message
	 */
	@Override
	public void accept(Visitor v) {
		
		v.visit(this);
	}

}
