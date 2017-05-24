package it.polimi.ingsw.ps42.model;

import java.util.List;
import java.util.Random;

import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.CouncilPosition;
import it.polimi.ingsw.ps42.model.position.MarketPosition;
import it.polimi.ingsw.ps42.model.position.TowerPosition;
import it.polimi.ingsw.ps42.model.position.YieldAndProductPosition;

public class Table {
	// this is the class of the table

	private List<Player> player;
	private int orangeDie;
	private int blackDie;
	private int whiteDie;
	private TowerPosition[] greenTower;
	private TowerPosition[] yellowTower;
	private TowerPosition[] blueTower;
	private TowerPosition[] violetTower;
	private MarketPosition[] market;
	private List<YieldAndProductPosition> yield;
	private List<YieldAndProductPosition> product;
	private YieldAndProductPosition firstYield;
	private YieldAndProductPosition firstProduct;
	private Effect firstBan;
	private Effect secondBan;
	private Effect thirdBan;
	private List<CouncilPosition> council;

	// constructrs

	public Table(Player Player1, Player Player2, Player Player3, Player Player4) {
		// 4 players

	}

	public Table(Player Player1, Player Player2, Player Player3) {
		// 3 players

	}

	public Table(Player Player1, Player Player2) {
		// 2 players

	}

	public void placeGreenTower(List<Card> cards) {
		// init cards in green tower

	}

	public void placeYellowTower(List<Card> cards) {
		// init cards in Yellow tower

	}

	public void placeVioletTower(List<Card> cards) {
		// init cards in Violet tower

	}

	public void placeBlueTower(List<Card> cards) {
		// init cards in Blue tower

	}

	// methods get position
	public Effect getFirsBan(){
		return firstBan;

	}

	public Effect getSecondBan(){
		return firstBan;
		
	}

	public Effect getThirdBan(){
		return firstBan;

	}

	public void addFirstBan(Effect ban) {

	}

	public void addSecondBan(Effect ban) {

	}

	public void addThirdBan(Effect ban) {

	}

	public List<CouncilPosition> getNewOrder(){
		return council;

	}

	public void throwDice(Random random) {

	}

	public List<CouncilPosition> resetTable() 
	{
		return council;

	}

}
