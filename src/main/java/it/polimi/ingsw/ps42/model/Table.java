package it.polimi.ingsw.ps42.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import it.polimi.ingsw.ps42.message.BanMessage;
import it.polimi.ingsw.ps42.message.BonusBarMessage;
import it.polimi.ingsw.ps42.message.CardsMessage;
import it.polimi.ingsw.ps42.message.DiceMessage;
import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.player.BonusBar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.CouncilPosition;
import it.polimi.ingsw.ps42.model.position.MarketPosition;
import it.polimi.ingsw.ps42.model.position.TowerPosition;
import it.polimi.ingsw.ps42.model.position.YieldAndProductPosition;
import it.polimi.ingsw.ps42.parser.PositionLoader;

public class Table extends Observable{
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
	//Loader for the market
	private PositionLoader marketLoader;
	
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
	
	//A council position to copy
	private CouncilPosition councilCopy;

	//The Game BonusBar List loaded by file 
	private List<BonusBar> gameBonusBar;
	
	//The four Constructors. We have a constructor for each kind of match
	//For example: with 2 players the table hasn't all the position

	public Table(Player player1, Player player2, Player player3, Player player4) {
		//4 players constructor
		//Create also all the market position
		
		//Add the player
		this(player1, player2, player3);
		players.add(player4);
		
		//Add all the required position for a 4-player game (7 max, so 2 more than a 3-player game)
		try {
			yieldAndProductConstructor(2);
		} catch (IOException e) {
			System.out.println("Unable to open the file");
		}
		
		//Add two more position for 4 players, then close the file
		market.add(marketLoader.getNextMarketPosition());
		market.add(marketLoader.getNextMarketPosition());
	}

	public Table(Player player1, Player player2, Player player3) {
		//3 players constructor
		this(player1, player2);
		players.add(player3);
		
		//Initialize the other yield and product positions
		yield = new ArrayList<>();
		product = new ArrayList<>();
		
		//Add all the required position for a 3-player game (5 max)
		try {
			yieldAndProductConstructor(5);
		} catch (IOException e) {
			System.out.println("Unable to open the file");
		}
	}

	public Table(Player player1, Player player2) {
		//2 players constructor
		constructor();
		players = new ArrayList<>();
		players.add(player1);
		players.add(player2);
	}
	
	//Private method for the construction of the yield and product position
	private void yieldAndProductConstructor(int numberOfPosition) throws IOException {
		PositionLoader loader = new PositionLoader("Resource//Position//YieldAndProductPosition//otherPosition.json");
		YieldAndProductPosition position = loader.getNextYieldAndProductPosition();
		for(int i=0 ; i<numberOfPosition; i++){
			yield.add(position.clone());
			product.add(position.clone());
		}
		loader.close();
	}
	
	//PRIVATE CONSTRUCTOR FOR ALL THE SIMILIAR OPERATION IN THE CONSTRUCTION
	private void constructor() {
		
		//Setting the StaticList of the Towers
		greenTower = new StaticList<>(FLOORS);
		yellowTower = new StaticList<>(FLOORS);
		blueTower = new StaticList<>(FLOORS);
		violetTower = new StaticList<>(FLOORS);
		
		towersConstructor();
		
		//Setting the First positions (Yield and Product)
		PositionLoader loader;
		try {
			loader = new PositionLoader("Resource//Position//YieldAndProductPosition//firstPosition.json");
			firstYield = loader.getNextYieldAndProductPosition();
			firstProduct = loader.getNextYieldAndProductPosition();
			loader.close();
		} catch (IOException e) {
			System.out.println("Unable to open the file");
		}
		
		//Setting the council
		council = new ArrayList<>();
		
		//Setting the market and load the first 2 position
		market = new StaticList<>(4);
		try {
			marketLoader = new PositionLoader("Resource//Position//MarketPosition//marketPosition.json");
		} catch (IOException e) {
			System.out.println("Unable to open the file");
		}
		market.add(marketLoader.getNextMarketPosition());
		market.add(marketLoader.getNextMarketPosition());
		
		//Initialize the councilLoader
		PositionLoader councilLoader;
		try {
			councilLoader = new PositionLoader("Resource//Position//CouncilPosition//councilPosition.json");
			councilCopy = councilLoader.getNextCouncilPosition();
			councilLoader.close();
		} catch (IOException e) {
			System.out.println("Unable to open the file");
		}
	}
	
	//PRIVATE METHOD TO CONSTRUCT THE 4 TOWER
	private void towersConstructor() {
		try {
			//Load greenTower
			PositionLoader loader = new PositionLoader("Resource//Position//TowerPosition//greenTowerPosition.json");
			TowerPosition position;
			position = loader.getNextTowerPosition();
			while(position != null) {
				greenTower.add(position);
				position = loader.getNextTowerPosition();
			}
			
			//Load yellowTower
			loader.setFileName("Resource//Position//TowerPosition//yellowTowerPosition.json");
			position = loader.getNextTowerPosition();
			while(position != null) {
				yellowTower.add(position);
				position = loader.getNextTowerPosition();
			}
			
			//Load blueTower
			loader.setFileName("Resource//Position//TowerPosition//blueTowerPosition.json");
			position = loader.getNextTowerPosition();
			while(position != null) {
				blueTower.add(position);
				position = loader.getNextTowerPosition();
			}
			
			//Load violetTower
			loader.setFileName("Resource//Position//TowerPosition//violetTowerPosition.json");
			position = loader.getNextTowerPosition();
			while(position != null) {
				violetTower.add(position);
				position = loader.getNextTowerPosition();
			}
			loader.close();
		} catch (IOException e) {
			System.out.println("Unable to open the file");
		}
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
		notifyNewCardsInTower(cards, CardColor.GREEN);
	}

	public void placeYellowTower(StaticList<Card> cards) {
		//Initialize cards in Yellow tower
		placeCards(cards, yellowTower);
		notifyNewCardsInTower(cards, CardColor.YELLOW);

	}

	public void placeVioletTower(StaticList<Card> cards) {
		//Initialize cards in Violet tower
		placeCards(cards, violetTower);
		notifyNewCardsInTower(cards, CardColor.VIOLET);

	}

	public void placeBlueTower(StaticList<Card> cards) {
		//Initialize cards in Blue tower
		placeCards(cards, blueTower);
		notifyNewCardsInTower(cards, CardColor.BLUE);

	}
	
	private void notifyNewCardsInTower( StaticList<Card> deck, CardColor color){
		//Method called every time a new deck of cards is set on a tower to notify the View
		StaticList<Card> deckCopy = new StaticList<>(4);
		for (Card card : deck) {
			deckCopy.add(card.clone());
		}
		
		Message cardsMessage = new CardsMessage(deck, color);
		setChanged();
		notifyObservers(cardsMessage);
	}
	
	//SETTER FOR THE BONUS BAR
	public void setBonusBarList(List<BonusBar> gameBonusBars){
		this.gameBonusBar = gameBonusBars;
	}
	
	public BonusBar removeBonusBar( int index){
		return gameBonusBar.remove(index);
	}
	
	public void askPlayerBonusBar( int indexForPlayer){
		//Method that notify the View asking to choose between the available bonusBars
		ArrayList<BonusBar> copyGameBonusBar = new ArrayList<>();
		for (BonusBar bonusBar : gameBonusBar) {
			copyGameBonusBar.add(bonusBar.clone());
		}
		Message bonusBarMessage = new BonusBarMessage( players.get(indexForPlayer).getPlayerID(), copyGameBonusBar);
		setChanged();
		notifyObservers(bonusBarMessage);
	}
	
	//GETTER AND SETTER FOR THE BANS
	public Effect getFirstBan(){
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
		
		//Notify each player's View of the three game's bans with a BanMessage

		Message banMessage = new BanMessage( this.firstBan.clone(), this.secondBan.clone(), this.thirdBan.clone());
		setChanged();
		notifyObservers(banMessage);
		
	}

	private List<Player> getNewOrder(){
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
		setOrangeDie(1 + random.nextInt(6));
		setBlackDie(1 + random.nextInt(6));
		setWhiteDie(1 + random.nextInt(6));
		
		//Notify the View of the new values of the dice
		Message diceMessage = new DiceMessage(orangeDie, blackDie, whiteDie);
		setChanged();
		notifyObservers(diceMessage);
		
	}
	
	//SETTER FOR THE DICE
	public void setOrangeDie(int value){
		this.orangeDie = value;
		for (Player player : players) {
			player.setFamiliarValue(FamiliarColor.ORANGE, value);
		}
	}
	
	public void setBlackDie(int value){
		this.blackDie = value;
		for (Player player : players) {
			player.setFamiliarValue(FamiliarColor.BLACK, value);
		}
	}
	
	public void setWhiteDie(int value){
		this.whiteDie = value;
		for (Player player : players) {
			player.setFamiliarValue(FamiliarColor.WHITE, value);
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

	public List<Player> resetTable() {
		//Reset all the tower position
		for(int i = 0; i < FLOORS; i++) {
			
			greenTower.get(i).removeCard();
			greenTower.get(i).removeFamiliar();
			
			yellowTower.get(i).removeCard();
			yellowTower.get(i).removeFamiliar();
			
			blueTower.get(i).removeCard();
			blueTower.get(i).removeFamiliar();
			
			violetTower.get(i).removeCard();
			violetTower.get(i).removeFamiliar();
		}
		
		//Reset Yield and Product first Position
		firstYield.removeFamiliar();
		firstYield.removeBonusFamiliars();
		
		firstProduct.removeFamiliar();
		firstProduct.removeBonusFamiliars();
		
		//Reset the others Yield and Product Positions
		for(YieldAndProductPosition position : yield) {
			position.removeFamiliar();
			position.removeBonusFamiliars();
		}
		
		for(YieldAndProductPosition position : product) {
			position.removeFamiliar();
			position.removeBonusFamiliars();
		}
		
		//Reset the market positions
		for(MarketPosition position : market) {
			position.removeFamiliar();
			position.removeBonusFamiliars();
		}
		
		return getNewOrder();
	}
	
	public CouncilPosition getFreeCouncilPosition() {
		CouncilPosition councilPosition = councilCopy.clone();
		council.add(councilPosition);
		return councilPosition;
	}

}
