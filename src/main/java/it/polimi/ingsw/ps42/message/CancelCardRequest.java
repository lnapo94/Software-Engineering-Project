package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;

public class CancelCardRequest extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1476956422402045880L;
	
	private boolean cancelRequest;

	public CancelCardRequest(String playerID, boolean cancelRequest) {
		super(playerID);
		this.cancelRequest = cancelRequest;
	}
	
	public boolean isToCancelARequest() {
		return this.cancelRequest;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
