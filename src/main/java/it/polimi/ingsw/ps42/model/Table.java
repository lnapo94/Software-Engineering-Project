package it.polimi.ingsw.ps42.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.CouncilPosition;
import it.polimi.ingsw.ps42.model.position.MarketPosition;
import it.polimi.ingsw.ps42.model.position.TowerPosition;
import it.polimi.ingsw.ps42.model.position.YieldAndProductPosition;

public class Table {
	//This is the Table Class
	//Necessary to group all the useful Model Classes
	
	//Constants for the default match
	private final static int FLOORS = 4;
	
	//Variable to set the player who is playing this match
	private List<Player> players;
	
	//Variable used to store the point of the dice
	private int orangeDie;
	private int blackDie;
	private int whiteDie;
	
	//The four tower represented by a StaticList, because of a tower can have only 4 position
	private StaticList<TowerPosition> greenTower;
	private StaticList<TowerPosition> yellowTower;
	private StaticList<TowerPosition> blueTower;
	private StaticList<TowerPosition> violetTower;
	
	//The Market can have only 4 position, each with an obtain effect different
	private StaticList<MarketPosition> market;
	
	//The two first positions, the former for an Yield action, the latter for a Product action
	//In this two positions there aren't any malus
	private YieldAndProductPosition firstYield;
	private YieldAndProductPosition firstProduct;
	
	//The other position for Yield and Product, now they have a malus
	private List<YieldAndProductPosition> yield;
	private List<YieldAndProductPosition> product;

	//The 3 ban chosen at the beginning of the match
	private Effect firstBan;
	private Effect secondBan;
	private Effect thirdBan;
	
	//The Council position. In this position we have the CouncilObtain and
	//a player can book a better position in the next round
	private List<CouncilPosition> council;
	
	//List of Obtain which represents the Council Bonuses
	private List<Obtain> councilBonuses;

	//The four Constructors. We have a constructor for each kind of match
	//For example: with 2 players the table hasn't all the position

	public Table(Player player1, Player player2, Player player3, Player player4) {
		//4 players constructor
		//Create also all the market position
		
		//Add the player
		this(player1, player2, player3);
		players.add(player4);
		
		//Add all the required position for a 4-player game (7 max, so 2 more than a 3-player game)
		for(int i=0 ; i<2; i++){
			yield.add(new YieldAndProductPosition(ActionType.YIELD , 1, null, 3));
			product.add(new YieldAndProductPosition(ActionType.PRODUCE, 1, null, 3));
		}
	}

	public Table(Player player1, Player player2, Player player3) {
		//3 players constructor
		this(player1, player2);
		players.add(player3);
		
		//Initialize the other yield and product positions
		yield = new ArrayList<>();
		product = new ArrayList<>();
		
		//Add all the required position for a 3-player game (5 max)
		for(int i=0 ; i<5; i++){
			yield.add(new YieldAndProductPosition(ActionType.YIELD , 1, null, 3));
			product.add(new YieldAndProductPosition(ActionType.PRODUCE, 1, null, 3));
		}
	}

	public Table(Player player1, Player player2) {
		//2 players constructor
		constructor();
		players = new ArrayList<>();
		players.add(player1);
		players.add(player2);
	}
	
	//PRIVATE CONSTRUCTOR FOR ALL THE SIMILIAR OPERATION IN THE CONSTRUCTION
	private void constructor() {
		
		//Setting the StaticList of the Towers
		greenTower = new StaticList<>(FLOORS);
		yellowTower = new StaticList<>(FLOORS);
		blueTower = new StaticList<>(FLOORS);
		violetTower = new StaticList<>(FLOORS);
		
		//Setting the First positions (Yield and Product)
		firstYield = new YieldAndProductPosition(ActionType.YIELD, 1, null, 0);
		firstProduct = new YieldAndProductPosition(ActionType.PRODUCE, 1, null, 0);
		
		//Setting the council
		council = new ArrayList<>();
		
		/*	TODO
		 * 	Load from file the three Ban and the councilBonuses
		 * 	Also load all the tower position
		 * 	Load the first 2 market position (money and slave)
		 */
	}
	
	
	//PRIVATE METHOD TO POSITIONING THE CARDS IN TOWER
	private void placeCards(StaticList<Card> cards, StaticList<TowerPosition> tower) {
		for (int i = 0; i < FLOORS; i++) {
			tower.get(i).setCard(cards.get(i));
		}
	}

	public void placeGreenTower(StaticList<Card> cards) {
		//Initialize cards in green tower
		placeCards(cards, greenTower);
	}

	public void placeYellowTower(StaticList<Card> cards) {
		//Initialize cards in Yellow tower
		placeCards(cards, yellowTower);

	}

	public void placeVioletTower(StaticList<Card> cards) {
		//Initialize cards in Violet tower
		placeCards(cards, violetTower);

	}

	public void placeBlueTower(StaticList<Card> cards) {
		//Initialize cards in Blue tower
		placeCards(cards, blueTower);
	}

	//GETTER FOR THE BANS
	public Effect getFirsBan(){
		return firstBan;

	}

	public Effect getSecondBan(){
		return secondBan;
		
	}

	public Effect getThirdBan(){
		return thirdBan;

	}

	public void addFirstBan(Effect ban) {
		firstBan = ban;
	}

	public void addSecondBan(Effect ban) {
		secondBan = ban;
	}

	public void addThirdBan(Effect ban) {
		thirdBan = ban;
	}

	public List<Player> getNewOrder(){
		//Return the new order of the player
		List<Player> temp = new ArrayList<>();
		for(CouncilPosition position : council) {
			temp.add(position.getFamiliar().getPlayer());
			council.remove(position);
			//Remove also the familiar
			position.removeFamiliar();
		}
		return temp;
	}

	public void throwDice(Random random) {
		//Set the three dice with a value between 1 and 6 
		orangeDie = 1 + random.nextInt(6);
		blackDie =  1 + random.nextInt(6);
		whiteDie =  1 + random.nextInt(6);
		
		//Set also the familiars color
		for(Player player : this.players) {
			player.getFamiliar(FamiliarColor.ORANGE).setValue(orangeDie);
			player.getFamiliar(FamiliarColor.BLACK).setValue(blackDie);
			player.getFamiliar(FamiliarColor.WHITE).setValue(whiteDie);
		}
		
	}
	//GETTER FOR THE TOWERS
	public StaticList<TowerPosition> getGreenTower() {
		return greenTower;
	}
	
	public StaticList<TowerPosition> getYellowTower() {
		return yellowTower;
	}
	
	public StaticList<TowerPosition> getBlueTower() {
		return blueTower;
	}
	
	public StaticList<TowerPosition> getVioletTower() {
		return violetTower;
	}
	
	//GETTER FOR THE MARKET
	public StaticList<MarketPosition> getMarket() {
		return market;
	}
	
	//GETTER FOR THE YIELD AND PRODUCT
	public YieldAndProductPosition getFirstYield() {
		return firstYield;
	}
	
	public YieldAndProductPosition getFirstProduct() {
		return firstProduct;
	}
	
	public List<YieldAndProductPosition> getOtherYield() {
		return yield;
	}

	public List<YieldAndProductPosition> getOtherProduct() {
		return product;
	}

	public List<CouncilPosition> resetTable() {
		/*	TODO
		 * 	RESET TAVOLO
		 */
		return council;
	}
	
	//RETURN THE COUNCIL BONUS
	public List<Obtain> getCouncilBonuses() {
		return councilBonuses;
	}
	
	public CouncilPosition getFreeCouncilPosition() {
		CouncilPosition councilPosition = new CouncilPosition(ActionType.COUNCIL, 1, null, 0, null);
		council.add(councilPosition);
		return councilPosition;
		/*TODO
		 * Creare da file i possibili privilegi (ultimo valore null in Costruttore)
		 */
	}

}
