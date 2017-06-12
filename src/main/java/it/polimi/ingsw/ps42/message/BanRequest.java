package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;

public class BanRequest extends Message {
	
	private int banNumber;
	private boolean wantPayForBan;

	public BanRequest(String playerID, int banNumber) {
		super(playerID);
		this.banNumber = banNumber;
	}
	
	public int getBanNumber() {
		return banNumber;
	}
	
	public boolean wantPayForBan() {
		return wantPayForBan;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
