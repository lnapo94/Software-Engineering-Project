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

public class TableView {
	/*Equivalent of the Model Table but simplified for the View.
	 * 
	 */
	
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

	public TableView(Player player1, Player player2) {
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
			logger.error("Unable to open the file");
			logger.info(e);
		}
	}
	
	//METHODS TO POSITIONING THE CARDS IN TOWERS
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
	}
	
	public void setPlayerFirstBan(String playerID){
		
		playersWithFirstBan.add(playerID);
	}
	
	public void setPlayerSecondBan(String playerID){
		
		playersWithSecondBan.add(playerID);
	}
	
	public void setPlayerThirdBan(String playerID){
		
		playersWithThirdBan.add(playerID);
	}
	
	public List<String> getPlayersWithFirstBan() {
		return playersWithFirstBan;
	}
	
	public List<String> getPlayersWithSecondBan() {
		return playersWithSecondBan;
	}
	
	public List<String> getPlayersWithThirdBan() {
		return playersWithThirdBan;
	}
	
	//SETTER AND GETTERS FOR THE DICE
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
		
	public int getBlackDie() {
		return blackDie;
	}
		
	public int getOrangeDie() {
		return orangeDie;
	}
		
	public int getWhiteDie() {
		return whiteDie;
	}
		
	//RESET THE TABLE
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
		
	//PLACER FOR THE FAMILIARS
	private void place(Familiar familiar, Position position) throws ElementNotFoundException{
			
		if(position.isEmpty()){
			position.setFamiliarView(familiar);
			familiar.setPosition(position);
		}
	}
		
	public void placeInMarket(Familiar familiar, int position) throws ElementNotFoundException{
		if(market.size() > position && position >= 0)
			place(familiar, market.get(position));
			
	}
	
	public void placeInProduce(Familiar familiar, int position) throws ElementNotFoundException{
		
		if(position == 0)
			place(familiar, firstProduct);
		else if(product.size() > position && position > 0)
			place(familiar, product.get(position));
	}
		
	public void placeInYield(Familiar familiar, int position) throws ElementNotFoundException{
		if(position == 0)
			place(familiar, firstYield);
		else if(yield.size() > position && position > 0)
			place(familiar, yield.get(position));
	}
		
	public void placeInCouncil(Familiar familiar) throws ElementNotFoundException{
			
		place(familiar, getFreeCouncilPosition());
			
	}
		
	public void placeInGreenTower(Familiar familiar, int position) throws ElementNotFoundException{
			
		if(greenTower.size() > position && position >= 0)
			place(familiar, greenTower.get(position));
	}

	public void placeInYellowTower(Familiar familiar, int position) throws ElementNotFoundException{
			
		if(yellowTower.size() > position && position >= 0)
			place(familiar, yellowTower.get(position));
	}

	public void placeInVioletTower(Familiar familiar, int position) throws ElementNotFoundException{

		if(violetTower.size() > position && position >= 0)
			place(familiar, violetTower.get(position));
	}
		
	public void placeInBlueTower(Familiar familiar, int position) throws ElementNotFoundException{

		if(blueTower.size() > position && position >= 0)
			place(familiar, blueTower.get(position));
	}
		
	//GETTERS FOR THE CARDS
	private Card removeCard(StaticList<TowerPosition> tower, int position) throws ElementNotFoundException{
		
		if( position >= 0 && tower.size() > position)
			if( tower.get(position).hasCard()){
				Card card = tower.get(position).getCard();
				tower.get(position).removeCard();
				return card;
			}
		
		throw new ElementNotFoundException("Card in Tower not Found");
	}
	
	public Card getGreenCard(int position) throws ElementNotFoundException{
			
		return removeCard(greenTower, position);
	}
		
	public Card getYellowCard(int position) throws ElementNotFoundException{

		return removeCard(yellowTower, position);

	}
		
	public Card getVioletCard(int position) throws ElementNotFoundException{

		return removeCard(violetTower, position);

	}
		
	public Card getBlueCard(int position) throws ElementNotFoundException{

		return removeCard(blueTower, position);

	}
	
	//Methods to show the Cards on the TableView
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
	
	private String showGreenCard(int position){
		
			return greenTower.get(position).getCard().showCard();
	}
	
	private String showVioletCard(int position){
		
		return violetTower.get(position).getCard().showCard();
	}
	
	private String showYellowCard(int position){
		
		return yellowTower.get(position).getCard().showCard();
	}
	
	private String showBlueCard(int position){
		
		return blueTower.get(position).getCard().showCard();
	}
}
