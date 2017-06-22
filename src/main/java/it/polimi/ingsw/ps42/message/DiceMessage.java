package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;

public class DiceMessage extends Message{
	//Message used to update the dice values
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5755333406273163374L;
	private int orange;
	private int black;
	private int white;
	
	public DiceMessage( int orange, int black, int white) {
		super(null);
		this.orange = orange;
		this.black = black;
		this.white = white;
	}
	
	public int getOrangeDie() {
		return this.orange;
	}
	
	public int getBlackDie() {
		return this.black;
	}
	
	public int getWhiteDie() {
		return this.white;
	}

	@Override
	public void accept(Visitor v) {
		//Method used to start the visit
		v.visit(this);
	}

}
