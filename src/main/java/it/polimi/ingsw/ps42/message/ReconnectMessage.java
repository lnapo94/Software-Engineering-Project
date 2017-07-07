package it.polimi.ingsw.ps42.message;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.effect.Effect;

public class ReconnectMessage extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7030620816522846921L;
	
	private Effect firstBan;
	private int firstBanIndex;
	private Effect secondBan;
	private int secondBanIndex;
	private Effect thirdBan;
	private int thirdBanIndex;
	
	private StaticList<Card> green;
	private StaticList<Card> yellow;
	private StaticList<Card> blue;
	private StaticList<Card> violet;

	public ReconnectMessage(String playerID) {
		super(playerID);
	}

	public Effect getFirstBan() {
		return firstBan;
	}



	public void setFirstBan(Effect firstBan, int index) {
		this.firstBan = firstBan;
		this.firstBanIndex = index;
	}



	public int getFirstBanIndex() {
		return firstBanIndex;
	}



	public Effect getSecondBan() {
		return secondBan;
	}



	public void setSecondBan(Effect secondBan, int index) {
		this.secondBan = secondBan;
		this.secondBanIndex = index;
	}



	public int getSecondBanIndex() {
		return secondBanIndex;
	}

	public Effect getThirdBan() {
		return thirdBan;
	}



	public void setThirdBan(Effect thirdBan, int index) {
		this.thirdBan = thirdBan;
		this.thirdBanIndex = index;
	}



	public int getThirdBanIndex() {
		return thirdBanIndex;
	}


	public StaticList<Card> getGreen() {
		return green;
	}



	public void setGreen(StaticList<Card> green) {
		this.green = green;
	}



	public StaticList<Card> getYellow() {
		return yellow;
	}



	public void setYellow(StaticList<Card> yellow) {
		this.yellow = yellow;
	}



	public StaticList<Card> getBlue() {
		return blue;
	}



	public void setBlue(StaticList<Card> blue) {
		this.blue = blue;
	}



	public StaticList<Card> getViolet() {
		return violet;
	}



	public void setViolet(StaticList<Card> violet) {
		this.violet = violet;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}	

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
