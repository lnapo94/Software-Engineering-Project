package it.polimi.ingsw.ps42.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.message.BanMessage;
import it.polimi.ingsw.ps42.message.BonusBarMessage;
import it.polimi.ingsw.ps42.message.CardsMessage;
import it.polimi.ingsw.ps42.message.DiceMessage;
import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.player.BonusBar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.CouncilPosition;
import it.polimi.ingsw.ps42.model.position.MarketPosition;
import it.polimi.ingsw.ps42.model.position.TowerPosition;
import it.polimi.ingsw.ps42.model.position.YieldAndProductPosition;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;
import it.polimi.ingsw.ps42.parser.PositionLoader;

/**
 * This is the Table Class
 * Necessary to group all the useful Model Classes
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class Table extends Observable{
	//
	
	//Constants for the default match
	private final static int FLOORS = 4;
	
	private final static int FIRST_MONEY = 5;
	private final static int SECOND_MONEY = 6;
	private final static int THIRD_MONEY = 7;
	private final static int FOURTH_MONEY = 8;
	
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
	
	private int indexOfFirstBan;
	private int indexOfSecondBan;
	private int indexOfThirdBan;
	
	//The Council position. In this position we have the CouncilObtain and
	//a player can book a better position in the next round
	private List<CouncilPosition> council;
	
	//A council position to copy
	private CouncilPosition councilCopy;

	//The Game BonusBar List loaded by file 
	private List<BonusBar> gameBonusBar;
	
	//Initilial resources for all the players
	private Packet initialResources;
	
	//Logger
	private transient Logger logger = Logger.getLogger(Table.class);
	
	//The four Constructors. We have a constructor for each kind of match
	//For example: with 2 players the table hasn't all the position
	
	/**
	 * The first constructor for the table, to construct the full table
	 * @param player1
	 * @param player2
	 * @param player3
	 * @param player4
	 */
	public Table(Player player1, Player player2, Player player3, Player player4) {
		//4 players constructor
		//Create also all the market position
		
		//Add the player
		this(player1, player2, player3);
		players.add(player4);
		
		player4.increaseResource(addInitialResources(FOURTH_MONEY));
		player4.synchResource();
		
		//Add all the required position for a 4-player game (7 max, so 2 more than a 3-player game)
		try {
			yieldAndProductConstructor(2);
		} catch (IOException e) {
			logger.error("Unable to open the file");
			logger.info(e);
		}
		
		//Add two more position for 4 players, then close the file
		market.add(marketLoader.getNextMarketPosition());
		market.add(marketLoader.getNextMarketPosition());
	}

	/**
	 * The constructor used for a 3 player match, construct a table with market
	 * with only 2 positions
	 * 
	 * @param player1
	 * @param player2
	 * @param player3
	 */
	public Table(Player player1, Player player2, Player player3) {
		//3 players constructor
		this(player1, player2);
		players.add(player3);
		
		player3.increaseResource(addInitialResources(THIRD_MONEY));
		player3.synchResource();
		
		//Initialize the other yield and product positions
		yield = new ArrayList<>();
		product = new ArrayList<>();
		
		//Add all the required position for a 3-player game (5 max)
		try {
			yieldAndProductConstructor(5);
		} catch (IOException e) {
			logger.error("Unable to open the file");
			logger.info(e);
		}
	}

	/**
	 * The most simple construct for a 2-players match
	 * Construct a table with only 2 market positions and with
	 * 1 yield position and 1 produce positon
	 * 
	 * @param player1
	 * @param player2
	 */
	public Table(Player player1, Player player2) {
		//2 players constructor
		constructor();
		players = new ArrayList<>();
		players.add(player1);
		players.add(player2);
		
		player1.increaseResource(addInitialResources(FIRST_MONEY));
		player2.increaseResource(addInitialResources(SECOND_MONEY));
		
		player1.synchResource();
		player2.synchResource();
	}
	
	/**
	 * Add the correct resources to the players
	 * 
	 * @param quantityOfMoney		How much money give to the player
	 * @return						The Packet of money
	 */
	private Packet addInitialResources(int quantityOfMoney) {
		Packet packet = initialResources.clone();
		packet.addUnit(new Unit(Resource.MONEY, quantityOfMoney));
		return packet;
	}
	
	/**
	 * Private method for the construction of the yield and product position
	 * 
	 * @param numberOfPosition		The number of position to construct
	 * @throws IOException			Thrown if there is a problemi in reading the file
	 */
	private void yieldAndProductConstructor(int numberOfPosition) throws IOException {
		PositionLoader loader = new PositionLoader("Resource//Position//YieldAndProductPosition//otherPosition.json");
		YieldAndProductPosition position = loader.getNextYieldAndProductPosition();
		for(int i=0 ; i<numberOfPosition; i++){
			yield.add(position.clone());
			product.add(position.clone());
		}
		loader.close();
	}
	
	/**
	 * PRIVATE CONSTRUCTOR FOR ALL THE SIMILIAR OPERATION IN THE CONSTRUCTION
	 */
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
			logger.error("Unable to open the file");
			logger.info(e);
		}
		
		//Setting the council
		council = new ArrayList<>();
		
		//Setting the market and load the first 2 position
		market = new StaticList<>(4);
		try {
			marketLoader = new PositionLoader("Resource//Position//MarketPosition//marketPosition.json");
		} catch (IOException e) {
			logger.error("Unable to open the file");
			logger.info(e);
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
			logger.error("Unable to open the file");
			logger.info(e);
		}
		
		//Adding initial resources to the packet
		initialResources = new Packet();
		initialResources.addUnit(new Unit(Resource.WOOD, 2));
		initialResources.addUnit(new Unit(Resource.STONE, 2));
		initialResources.addUnit(new Unit(Resource.SLAVE, 3));
	}
	
	/**
	 * PRIVATE METHOD TO CONSTRUCT THE 4 TOWER
	 */
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
			logger.error("Unable to open the file");
			logger.info(e);
		}
	}
	
	
	/**
	 * PRIVATE METHOD TO POSITIONING THE CARDS IN TOWER
	 * 
	 * @param cards		The StaticList of 4 cards to position on the table
	 * @param tower		The tower where the cards must be positioned
	 */
	private void placeCards(StaticList<Card> cards, StaticList<TowerPosition> tower) {
		for (int i = 0; i < FLOORS; i++) {
			tower.get(i).setCard(cards.get(i));
		}
	}

	/**
	 * Place the cards on the green tower
	 * 
	 * @param cards		The StaticList of 4 cards to position on the table
	 */
	public void placeGreenTower(StaticList<Card> cards) {
		//Initialize cards in green tower
		placeCards(cards, greenTower);
		notifyNewCardsInTower(cards, CardColor.GREEN);
	}

	/**
	 * Place the cards on the yellow tower
	 * 
	 * @param cards		The StaticList of 4 cards to position on the table
	 */
	public void placeYellowTower(StaticList<Card> cards) {
		//Initialize cards in Yellow tower
		placeCards(cards, yellowTower);
		notifyNewCardsInTower(cards, CardColor.YELLOW);

	}

	/**
	 * Place the cards on the violet tower
	 * 
	 * @param cards		The StaticList of 4 cards to position on the table
	 */
	public void placeVioletTower(StaticList<Card> cards) {
		//Initialize cards in Violet tower
		placeCards(cards, violetTower);
		notifyNewCardsInTower(cards, CardColor.VIOLET);

	}

	/**
	 * Place the cards on the blue tower
	 * 
	 * @param cards		The StaticList of 4 cards to position on the table
	 */
	public void placeBlueTower(StaticList<Card> cards) {
		//Initialize cards in Blue tower
		placeCards(cards, blueTower);
		notifyNewCardsInTower(cards, CardColor.BLUE);

	}
	
	/**
	 * Method called every time a new deck of cards is set on a tower to notify the View
	 * 
	 * @param deck		The StaticList of 4 cards positioned on the table 
	 * @param color		The color of the cards
	 */
	private void notifyNewCardsInTower( StaticList<Card> deck, CardColor color){
		
		StaticList<Card> deckCopy = new StaticList<>(4);
		for (Card card : deck) {
			deckCopy.add(card.clone());
		}
		
		Message cardsMessage = new CardsMessage(deck, color);
		setChanged();
		notifyObservers(cardsMessage);
	}
	
	/**
	 * SETTER FOR THE BONUS BAR
	 * 
	 * @param gameBonusBars			The available bonus bars
	 */
	public void setBonusBarList(List<BonusBar> gameBonusBars){
		this.gameBonusBar = gameBonusBars;
	}
	
	/**
	 * Remove a bonus bar from the current match
	 * 
	 * @param index		The bonus bar to remove
	 */
	public BonusBar removeBonusBar( int index){
		return gameBonusBar.remove(index);
	}
	
	/**
	 * Method that notify the View asking to choose between the available bonusBars
	 * 
	 * @param indexForPlayer		THe index of the selected player
	 */
	public void askPlayerBonusBar( int indexForPlayer){
		
		ArrayList<BonusBar> copyGameBonusBar = new ArrayList<>();
		for (BonusBar bonusBar : gameBonusBar) {
			copyGameBonusBar.add(bonusBar.clone());
		}
		Message bonusBarMessage = new BonusBarMessage( players.get(indexForPlayer).getPlayerID(), copyGameBonusBar);
		setChanged();
		notifyObservers(bonusBarMessage);
	}
	
	/**
	 * Getter for the first ban
	 * 
	 * @return		The first setted ban
	 */
	public Effect getFirstBan(){
		return firstBan;

	}

	/**
	 * Getter for the second ban
	 * 
	 * @return		The second setted ban
	 */
	public Effect getSecondBan(){
		return secondBan;
		
	}

	/**
	 * Getter for the third ban
	 * 
	 * @return		The third setted ban
	 */
	public Effect getThirdBan(){
		return thirdBan;
	}

	/**
	 * Method used to set the first ban in the table
	 * 
	 * @param ban				The first ban to set
	 * @param indexOfFirstBan	The index in the file of the ban
	 */
	public void addFirstBan(Effect ban, int indexOfFirstBan) {
		firstBan = ban;
		this.indexOfFirstBan = indexOfFirstBan;
	}

	/**
	 * Method used to set the second ban in the table
	 * 
	 * @param ban				The second ban to set
	 * @param indexOfFirstBan	The index in the file of the ban
	 */
	public void addSecondBan(Effect ban, int indexOfSecondBan) {
		secondBan = ban;
		this.indexOfSecondBan = indexOfSecondBan;
	}

	/**
	 * Method used to set the third ban in the table and to notify the 3 bans in table
	 * 
	 * @param ban				The third ban to set
	 * @param indexOfFirstBan	The index in the file of the ban
	 */
	public void addThirdBan(Effect ban, int indexOfThirdBan) {
		thirdBan = ban;
		this.indexOfThirdBan = indexOfThirdBan;
		
		//Notify each player's View of the three game's bans with a BanMessage

		Message banMessage = new BanMessage(this.firstBan.clone(), this.indexOfFirstBan, this.secondBan.clone(), this.indexOfSecondBan, this.thirdBan.clone(), this.indexOfThirdBan);
		setChanged();
		notifyObservers(banMessage);
	}

	/**
	 * When the round is finished, get the new order in the table
	 * 
	 * @return		The list of player sorted with the correct order yet
	 */
	private List<Player> getNewOrder(){
		//Return the new order of the player
		List<Player> temp = new ArrayList<>();
		for(int i = 0; i < council.size(); i++) {
			CouncilPosition position = council.get(0);
			if(!temp.contains(position.getFamiliar().getPlayer()))
				temp.add(position.getFamiliar().getPlayer());
			council.remove(position);
			//Remove also the familiar
			position.removeFamiliar();
		}
		return temp;
	}

	/**
	 * Method used to throw the 3 dice, set their values and notify all the players
	 * 
	 * @param random		A random object to extract 3 integer
	 */
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
	
	/**
	 * Set the value of the orange die and also set it to the players' familiars
	 * 
	 * @param value		The value to set to the die
	 */
	public void setOrangeDie(int value){
		this.orangeDie = value;
		for (Player player : players) {
			player.setFamiliarValue(FamiliarColor.ORANGE, value);
		}
	}
	
	/**
	 * Set the value of the black die and also set it to the players' familiars
	 * 
	 * @param value		The value to set to the die
	 */
	public void setBlackDie(int value){
		this.blackDie = value;
		for (Player player : players) {
			player.setFamiliarValue(FamiliarColor.BLACK, value);
		}
	}
	
	/**
	 * Set the value of the white die and also set it to the players' familiars
	 * 
	 * @param value		The value to set to the die
	 */
	public void setWhiteDie(int value){
		this.whiteDie = value;
		for (Player player : players) {
			player.setFamiliarValue(FamiliarColor.WHITE, value);
		}
	}
	
	/**
	 * Get the green tower positions
	 * 
	 * @return	StaticList of 4 tower positions
	 */
	public StaticList<TowerPosition> getGreenTower() {
		return greenTower;
	}
	
	/**
	 * Get the yellow tower positions
	 * 
	 * @return	StaticList of 4 tower positions
	 */
	public StaticList<TowerPosition> getYellowTower() {
		return yellowTower;
	}
	
	/**
	 * Get the blue tower positions
	 * 
	 * @return	StaticList of 4 tower positions
	 */
	public StaticList<TowerPosition> getBlueTower() {
		return blueTower;
	}
	
	/**
	 * Get the violet tower positions
	 * 
	 * @return	StaticList of 4 tower positions
	 */
	public StaticList<TowerPosition> getVioletTower() {
		return violetTower;
	}
	
	/**
	 * Get the available market positions
	 * 
	 * @return	StaticList of available market positions (MAX 4)
	 */
	public StaticList<MarketPosition> getMarket() {
		return market;
	}
	
	/**
	 * Get the first yield position
	 * 
	 * @return	The first yield position
	 */
	public YieldAndProductPosition getFirstYield() {
		return firstYield;
	}
	
	/**
	 * Get the first product position
	 * 
	 * @return	The first product position
	 */
	public YieldAndProductPosition getFirstProduct() {
		return firstProduct;
	}
	
	/**
	 * Get the others yield position
	 * 
	 * @return	The List of others yield position
	 */
	public List<YieldAndProductPosition> getOtherYield() {
		return yield;
	}

	/**
	 * Get the others product position
	 * 
	 * @return	The List of others product position
	 */
	public List<YieldAndProductPosition> getOtherProduct() {
		return product;
	}

	/**
	 * Method used to reset the table when a round is finished
	 */
	public List<Player> resetTable() {
		//Reset all the familiars
		for(Player player : players) {
			player.getFamiliar(FamiliarColor.ORANGE).resetPosition();
			player.getFamiliar(FamiliarColor.WHITE).resetPosition();
			player.getFamiliar(FamiliarColor.BLACK).resetPosition();
			player.getFamiliar(FamiliarColor.NEUTRAL).resetPosition();
		}
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
		if(yield != null) {
			for(YieldAndProductPosition position : yield) {
				position.removeFamiliar();
				position.removeBonusFamiliars();
			}
		}
		
		if(product != null) {
			for(YieldAndProductPosition position : product) {
				position.removeFamiliar();
				position.removeBonusFamiliars();
			}
		}
		
		//Reset the market positions
		for(MarketPosition position : market) {
			position.removeFamiliar();
			position.removeBonusFamiliars();
		}
		
		return getNewOrder();
	}
	
	/**
	 * Method used to have always a free council position
	 * 
	 * @return	Create a new council position, add it to the council list in table and finally return it
	 */
	public CouncilPosition getFreeCouncilPosition() {
		CouncilPosition councilPosition = councilCopy.clone();
		council.add(councilPosition);
		return councilPosition;
	}

}
