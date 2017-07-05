package it.polimi.ingsw.ps42.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.CouncilPosition;
import it.polimi.ingsw.ps42.model.position.MarketPosition;
import it.polimi.ingsw.ps42.model.position.Position;
import it.polimi.ingsw.ps42.model.position.TowerPosition;
import it.polimi.ingsw.ps42.model.position.YieldAndProductPosition;
import it.polimi.ingsw.ps42.parser.PositionLoader;

/**
 * Equivalent of the Model Table but simplified for the View.
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class TableView {
	
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
	//The players banned
	private List<String> playersWithFirstBan;
	private List<String> playersWithSecondBan;
	private List<String> playersWithThirdBan;
	
	//The Council position. In this position we have the CouncilObtain and
	//a player can book a better position in the next round
	private List<CouncilPosition> council;
	
	//A council position to copy
	private CouncilPosition councilCopy;
	
	//Logger
	private transient Logger logger = Logger.getLogger(TableView.class);
	
	//The four Constructors. We have a constructor for each kind of match
	//For example: with 2 players the table hasn't all the position
	
	/**
	 * Constructor of the Table for a 4 Players Game
	 * @param player1 the first player
	 * @param player2 the second player
	 * @param player3 the third player
	 * @param player4 the fourth player
	 */
	public TableView(Player player1, Player player2, Player player3, Player player4) {
		//4 players constructor
		//Create also all the market position
		
		//Add the player
		this(player1, player2, player3);
		players.add(player4);
		
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
	 * Constructor of the Table for a 3 Players Game
	 * @param player1 the first player
	 * @param player2 the second player
	 * @param player3 the third player
	 */
	public TableView(Player player1, Player player2, Player player3) {
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
			logger.error("Unable to open the file");
			logger.info(e);
		}
	}


	/**
	 * Constructor of the Table for a 2 Players Game
	 * @param player1 the first player
	 * @param player2 the second player
	 */
	public TableView(Player player1, Player player2) {
		//2 players constructor
		constructor();
		players = new ArrayList<>();
		players.add(player1);
		players.add(player2);
	}
	
	/**
	 * Private method for the construction of the yield and product position
	 * @param numberOfPosition the number of position to build
	 * @throws IOException if problems in json file reading occurs
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
	 * Private constructor for all the similar operation in the construction phase
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
		
		//Initialize the players banned lists
		playersWithFirstBan = new ArrayList<>();
		playersWithSecondBan = new ArrayList<>();
		playersWithThirdBan = new ArrayList<>();
	}
	
	public CouncilPosition getFreeCouncilPosition() {
		CouncilPosition councilPosition = councilCopy.clone();
		council.add(councilPosition);
		return councilPosition;
	}
	
	/**
	 * Private method to construct the 4 towers
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
	 * Private method to position the cards in a generic tower
	 * @param cards the list of Cards to place
	 * @param tower the positions that receive the cards 
	 */
	private void placeCards(StaticList<Card> cards, StaticList<TowerPosition> tower) {
		for (int i = 0; i < FLOORS; i++) {
			tower.get(i).setCard(cards.get(i));
		}
	}


	/**
	 * Method to position the cards in the green tower
	 * @param cards the list of Cards to place
	 */
	public void placeGreenTower(StaticList<Card> cards) {
		//Initialize cards in green tower
		placeCards(cards, greenTower);
	}


	/**
	 * Method to position the cards in the yellow tower
	 * @param cards the list of Cards to place
	 */
	public void placeYellowTower(StaticList<Card> cards) {
		//Initialize cards in Yellow tower
		placeCards(cards, yellowTower);

	}


	/**
	 * Method to position the cards in the violet tower
	 * @param cards the list of Cards to place
	 */
	public void placeVioletTower(StaticList<Card> cards) {
		//Initialize cards in Violet tower
		placeCards(cards, violetTower);
		
	}


	/**
	 * Method to position the cards in the blue tower
	 * @param cards the list of Cards to place
	 */
	public void placeBlueTower(StaticList<Card> cards) {
		//Initialize cards in Blue tower
		placeCards(cards, blueTower);
		
	}
	
	//GETTER AND SETTER FOR THE BANS
	
	/**
	 * Getter for the first ban
	 * @return the first ban of the game
	 */
	public Effect getFirstBan(){
		return firstBan;

	}

	/**
	 * Getter for the second ban of the game
	 * @return the second ban of the game
	 */
	public Effect getSecondBan(){
		return secondBan;
			
	}


	/**
	 * Getter for the third ban of the game
	 * @return the third ban of the game
	 */
	public Effect getThirdBan(){
		return thirdBan;
	}


	/**
	 * Setter for the first ban of the game
	 * @param ban the first ban of the game
	 */
	public void addFirstBan(Effect ban) {
		firstBan = ban;
	}


	/**
	 * Setter for the second ban of the game
	 * @param ban the second ban of the game
	 */
	public void addSecondBan(Effect ban) {
		secondBan = ban;
	}


	/**
	 * Setter for the third ban of the game
	 * @param ban the third ban of the game
	 */
	public void addThirdBan(Effect ban) {
		thirdBan = ban;
	}
	
	/**
	 * Setter for the first Ban to a specific Player 
	 * @param playerID the ID of the Player to ban
	 */
	public void setPlayerFirstBan(String playerID){
		
		playersWithFirstBan.add(playerID);
	}

	/**
	 * Setter for the second Ban to a specific Player 
	 * @param playerID the ID of the Player to ban
	 */
	public void setPlayerSecondBan(String playerID){
		
		playersWithSecondBan.add(playerID);
	}

	/**
	 * Setter for the third Ban to a specific Player 
	 * @param playerID the ID of the Player to ban
	 */
	public void setPlayerThirdBan(String playerID){
		
		playersWithThirdBan.add(playerID);
	}
	
	/**
	 * Getter for the list of Players banned by the first ban
	 * @return the List of Player with the first ban
	 */
	public List<String> getPlayersWithFirstBan() {
		return playersWithFirstBan;
	}

	/**
	 * Getter for the list of Players banned by the second ban
	 * @return the List of Player with the second ban
	 */
	public List<String> getPlayersWithSecondBan() {
		return playersWithSecondBan;
	}
	

	/**
	 * Getter for the list of Players banned by the third ban
	 * @return the List of Player with the third ban
	 */
	public List<String> getPlayersWithThirdBan() {
		return playersWithThirdBan;
	}
	
	//SETTER AND GETTERS FOR THE DICE
	/**
	 * Setter for the orange dice value
	 * @param value the value of the orange dice
	 */
	public void setOrangeDie(int value){
		this.orangeDie = value;
		for (Player player : players) {
			player.setFamiliarValue(FamiliarColor.ORANGE, value);
		}
	}
	
	/**
	 * Setter for the black dice value
	 * @param value the value of the black dice
	 */
	public void setBlackDie(int value){
		this.blackDie = value;
		for (Player player : players) {
			player.setFamiliarValue(FamiliarColor.BLACK, value);
		}
	}
		
	/**
	 * Setter for the white dice value
	 * @param value the value of the white dice
	 */
	public void setWhiteDie(int value){
		this.whiteDie = value;
		for (Player player : players) {
			player.setFamiliarValue(FamiliarColor.WHITE, value);
		}
	}
	
	/**
	 * Getter for the Black die value
	 * @return the current value of the Black die
	 */
	public int getBlackDie() {
		return blackDie;
	}
		

	/**
	 * Getter for the Orange die value
	 * @return the current value of the Orange die
	 */
	public int getOrangeDie() {
		return orangeDie;
	}
		

	/**
	 * Getter for the White die value
	 * @return the current value of the White die
	 */
	public int getWhiteDie() {
		return whiteDie;
	}
		
	/**
	 * Method to reset the table for the next round 
	 * @return the new Order of the Game by the Familiar placed in the Council
	 */
	public List<Player> resetTable() {
		
		//Reset all the Player's Familiars
		for (Player player : players) {
			player.getFamiliar(FamiliarColor.BLACK).resetPosition();
			player.getFamiliar(FamiliarColor.NEUTRAL).resetPosition();
			player.getFamiliar(FamiliarColor.WHITE).resetPosition();
			player.getFamiliar(FamiliarColor.ORANGE).resetPosition();
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
	 * Method to obtain the next round order
	 * @return the List of players ordered for the next round
	 */
	private List<Player> getNewOrder(){
		//Return the new order of the player
		List<Player> temp = new ArrayList<>();
		for(int i = 0; i < council.size(); i++) {
			CouncilPosition position = council.get(0);
			temp.add(position.getFamiliar().getPlayer());
			council.remove(position);
			//Remove also the familiar
			position.removeFamiliar();
		}
		return temp;
	}
		
	//PLACER FOR THE FAMILIARS
	/**
	 * Private method to place a generic familiar in a generic position
	 * @param familiar the Familiar to place
	 * @param position the position that receive the familiar
	 * @throws ElementNotFoundException if the setting of the Familiar fails
	 */
	private void place(Familiar familiar, Position position) throws ElementNotFoundException{
			
		if(position.isEmpty()){
			position.setFamiliarView(familiar);
			familiar.setPosition(position);
		}
	}
		
	/**
	 * Method to place a Familiar in a MarketPosition 
	 * @param familiar the Familiar to place
	 * @param position the specific MarketPosition 
	 * @throws ElementNotFoundException if the setting of the Familiar fails
	 */
	public void placeInMarket(Familiar familiar, int position) throws ElementNotFoundException{
		if(market.size() > position && position >= 0)
			place(familiar, market.get(position));
			
	}
	

	/**
	 * Method to place a Familiar in a Produce position 
	 * @param familiar the Familiar to place
	 * @param position the specific Produce position 
	 * @throws ElementNotFoundException if the setting of the Familiar fails
	 */
	public void placeInProduce(Familiar familiar, int position) throws ElementNotFoundException{
		
		if(position == 0)
			place(familiar, firstProduct);
		else if(product.size() > position && position > 0)
			place(familiar, product.get(position));
	}
		

	/**
	 * Method to place a Familiar in a Yield position 
	 * @param familiar the Familiar to place
	 * @param position the specific yield Position 
	 * @throws ElementNotFoundException if the setting of the Familiar fails
	 */
	public void placeInYield(Familiar familiar, int position) throws ElementNotFoundException{
		if(position == 0)
			place(familiar, firstYield);
		else if(yield.size() > position && position > 0)
			place(familiar, yield.get(position));
	}
		

	/**
	 * Method to place a Familiar in a CouncilPosition 
	 * @param familiar the Familiar to place
	 * @param position the specific CouncilPosition 
	 * @throws ElementNotFoundException if the setting of the Familiar fails
	 */
	public void placeInCouncil(Familiar familiar) throws ElementNotFoundException{
			
		place(familiar, getFreeCouncilPosition());
			
	}
		

	/**
	 * Method to place a Familiar in the GreenTower Position 
	 * @param familiar the Familiar to place
	 * @param position the specific GreenTower Position 
	 * @throws ElementNotFoundException if the setting of the Familiar fails
	 */
	public void placeInGreenTower(Familiar familiar, int position) throws ElementNotFoundException{
			
		if(greenTower.size() > position && position >= 0)
			place(familiar, greenTower.get(position));
	}


	/**
	 * Method to place a Familiar in the YellowTower Position 
	 * @param familiar the Familiar to place
	 * @param position the specific YellowTower Position 
	 * @throws ElementNotFoundException if the setting of the Familiar fails
	 */
	public void placeInYellowTower(Familiar familiar, int position) throws ElementNotFoundException{
			
		if(yellowTower.size() > position && position >= 0)
			place(familiar, yellowTower.get(position));
	}


	/**
	 * Method to place a Familiar in the VioletTower Position 
	 * @param familiar the Familiar to place
	 * @param position the specific VioletTower Position 
	 * @throws ElementNotFoundException if the setting of the Familiar fails
	 */
	public void placeInVioletTower(Familiar familiar, int position) throws ElementNotFoundException{

		if(violetTower.size() > position && position >= 0)
			place(familiar, violetTower.get(position));
	}
		

	/**
	 * Method to place a Familiar in the BlueTower Position 
	 * @param familiar the Familiar to place
	 * @param position the specific BlueTower Position 
	 * @throws ElementNotFoundException if the setting of the Familiar fails
	 */
	public void placeInBlueTower(Familiar familiar, int position) throws ElementNotFoundException{

		if(blueTower.size() > position && position >= 0)
			place(familiar, blueTower.get(position));
	}
		
	//GETTERS FOR THE CARDS
	/**
	 * Private method to remove a generic card from a specific Tower position 
	 * @param tower the Tower where the position is placed
	 * @param position the index of the position in the tower 
	 * @return the Card removed
	 * @throws ElementNotFoundException if the position does not has a Card
	 */
	private Card removeCard(StaticList<TowerPosition> tower, int position) throws ElementNotFoundException{
		
		if( position >= 0 && tower.size() > position)
			if( tower.get(position).hasCard()){
				Card card = tower.get(position).getCard();
				tower.get(position).removeCard();
				return card;
			}
		
		throw new ElementNotFoundException("Card in Tower not Found");
	}
	
	/**
	 * Method used to get a Card from a Green Tower given the position in the tower
	 * @param position the position where the card is placed
	 * @return the Card from the Position
	 * @throws ElementNotFoundException is the position does not has a Card
	 */
	public Card getGreenCard(int position) throws ElementNotFoundException{
			
		return removeCard(greenTower, position);
	}
		

	/**
	 * Method used to get a Card from a Yellow Tower given the position in the tower
	 * @param position the position where the card is placed
	 * @return the Card from the Position
	 * @throws ElementNotFoundException is the position does not has a Card
	 */
	public Card getYellowCard(int position) throws ElementNotFoundException{

		return removeCard(yellowTower, position);

	}
		

	/**
	 * Method used to get a Card from a Violet Tower given the position in the tower
	 * @param position the position where the card is placed
	 * @return the Card from the Position
	 * @throws ElementNotFoundException is the position does not has a Card
	 */
	public Card getVioletCard(int position) throws ElementNotFoundException{

		return removeCard(violetTower, position);

	}
		

	/**
	 * Method used to get a Card from a Blue Tower given the position in the tower
	 * @param position the position where the card is placed
	 * @return the Card from the Position
	 * @throws ElementNotFoundException is the position does not has a Card
	 */
	public Card getBlueCard(int position) throws ElementNotFoundException{

		return removeCard(blueTower, position);

	}
	
	//Methods to show the Cards on the TableView
	/**
	 * Method used to show a Card in tower on the table
	 * @param action the type of the tower
	 * @param position the position of the card choosen
	 * @return the String representation of the Card
	 */
	public String showCard(ActionType action, int position){
		
		if(action == ActionType.TAKE_GREEN)
			return showGreenCard(position);
		if(action == ActionType.TAKE_YELLOW)
			return showYellowCard(position);
		if(action == ActionType.TAKE_BLUE)
			return showBlueCard(position);
		if(action == ActionType.TAKE_VIOLET)
			return showVioletCard(position);
		return null;
	}
	
	/**
	 * Method used to show a Card in the Green tower position
	 * @param position the position of the Card
	 * @return the String representation of the Card
	 */
	private String showGreenCard(int position){
		
			return greenTower.get(position).getCard().showCard();
	}
	

	/**
	 * Method used to show a Card in the Violet tower position
	 * @param position the position of the Card
	 * @return the String representation of the Card
	 */
	private String showVioletCard(int position){
		
		return violetTower.get(position).getCard().showCard();
	}
	

	/**
	 * Method used to show a Card in the Yellow tower position
	 * @param position the position of the Card
	 * @return the String representation of the Card
	 */
	private String showYellowCard(int position){
		
		return yellowTower.get(position).getCard().showCard();
	}
	

	/**
	 * Method used to show a Card in the Blue tower position
	 * @param position the position of the Card
	 * @return the String representation of the Card
	 */
	private String showBlueCard(int position){
		
		return blueTower.get(position).getCard().showCard();
	}
}
