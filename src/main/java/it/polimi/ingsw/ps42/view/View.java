package it.polimi.ingsw.ps42.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.message.BanMessage;
import it.polimi.ingsw.ps42.message.BanRequest;
import it.polimi.ingsw.ps42.message.BonusBarMessage;
import it.polimi.ingsw.ps42.message.CancelCardRequest;
import it.polimi.ingsw.ps42.message.CardRequest;
import it.polimi.ingsw.ps42.message.CouncilRequest;
import it.polimi.ingsw.ps42.message.DiscardLeaderCard;
import it.polimi.ingsw.ps42.message.EmptyMove;
import it.polimi.ingsw.ps42.message.LeaderCardMessage;
import it.polimi.ingsw.ps42.message.LeaderCardUpdateMessage;
import it.polimi.ingsw.ps42.message.LoginMessage;
import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.message.PlayerMove;
import it.polimi.ingsw.ps42.message.PlayerToken;
import it.polimi.ingsw.ps42.message.PlayersListMessage;
import it.polimi.ingsw.ps42.message.ReconnectMessage;
import it.polimi.ingsw.ps42.message.WinnerMessage;
import it.polimi.ingsw.ps42.message.leaderRequest.LeaderFamiliarRequest;
import it.polimi.ingsw.ps42.message.visitorPattern.ViewVisitor;
import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.action.ActionPrototype;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;
import it.polimi.ingsw.ps42.model.exception.WrongChoiceException;
import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;
import it.polimi.ingsw.ps42.model.player.BonusBar;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;

/**
 * Class that represent the Abstract View of the MVC, it handles all the input/output methods for the game
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public abstract class View extends Observable implements Observer {

	
	protected TableView table; 
	protected Player player;
	protected List<Player> otherPlayers; 
	protected Visitor viewVisitor;
	
	//Logger
	private Logger logger = Logger.getLogger(View.class);
	
	/**
	 * Default Constructor for the View, it initialize the visitor to allow the communication in the MVC
	 */
	public View() {
		
		this.viewVisitor = new ViewVisitor(this);
		this.otherPlayers = new ArrayList<>();
		
	}
	
	/**
	 * Method used to add a player given its name
	 * @param playerID the player name
	 */
	public void addPlayer(String playerID){
		
		this.player = new Player(playerID);
	}
	
	/**
	 * Method to call to notify the Controller of the name chosen by the player of this view
	 * @param playerID the name chosen
	 */
	public void setNewPlayerID(String playerID){
		LoginMessage message = new LoginMessage(playerID);
		addPlayer(playerID);
		setChanged();
		notifyObservers(message);
	}
	
	/**
	 * Getter for the name of the main Player of the view
	 * @return
	 */
	public String getViewPlayerID() {
		return player.getPlayerID();
	}
	
	/**
	 * Method to call when a new game starts that builds the proper Table given the list of Players of the Game
	 * @param playersID the list of players of the game
	 */
	public void createTable(List<String> playersID){
		
		//Method to be called from the ViewVisitor when receive the players of the game
		if(playersID.contains(player.getPlayerID())){
			
			playersID.remove(player.getPlayerID());
			//Create the others players
			for (String playerID : playersID) {
				otherPlayers.add(new Player(playerID));
			}
			//Add the players to the Table
			if(playersID.size() == 1)
				this.table = new TableView( player, otherPlayers.get(0));
			else if(playersID.size() == 2)
				this.table = new TableView( player, otherPlayers.get(0), otherPlayers.get(1));
			else if(playersID.size() == 3)
				this.table = new TableView( player,  otherPlayers.get(0), otherPlayers.get(1), otherPlayers.get(2));
			
		}
	}
	
	/**
	 * Methods to verify if the Player has to answer to a message
	 * @param playerID the name of the player in a message
	 * @return true if the name passe equals the view main player name
	 */
	protected boolean hasToAnswer(String playerID){
		
		return player.getPlayerID().equals(playerID);
	}
	
	/**
	 * Method to know the position of the others players in the game order
	 * @param playerID the player to search
	 * @return the index in theround order of the player
	 * @throws ElementNotFoundException if the name passed does not equals any of the names in the view
	 */
	protected int indexOfOtherPlayer(String playerID) throws ElementNotFoundException{
		for(int i=0; i<otherPlayers.size(); i++){
			if(otherPlayers.get(i).getPlayerID().equals(playerID))
				return i;
		}
		throw new ElementNotFoundException("Player in View not Found");
	}
	
	/**
	 * Method to ask a new BonusBar to the player
	 * @param message the bonusBar message with all the bonusBar to choose from
	 */
	public void askBonusBar(BonusBarMessage message) {
		
		if( hasToAnswer(message.getPlayerID())){
			//Ask to choose a BonusBar
			chooseBonusBar(message.getAvailableBonusBar());
		}

	}
	
	/**
	 * Method to call when setting the bonusBar choice
	 * @param bonusBars the list of possible BonusBars 
	 * @param choice the index of the bonusBar chosen in the passed List
	 */
	public void setBonusBarChoice(List<BonusBar> bonusBars, int choice){
			
			//Send a new Message to the Game Logic to notify the choice
			List<BonusBar> availableBonusBar = new ArrayList<>();
			for (BonusBar bonusBar : bonusBars) {
				availableBonusBar.add(bonusBar.clone());
			}
			BonusBarMessage message = new BonusBarMessage(player.getPlayerID(), availableBonusBar);
			try {
				message.setChoice(choice);				
				setChanged();
				notifyObservers(message);
				
				//Add the BonusBar to the Player
				player.setBonusBar(bonusBars.get(choice));
				
			} catch (WrongChoiceException e) {
				logger.debug("Wrong BonusBar choice has been made");
				askBonusBar(new BonusBarMessage(player.getPlayerID(), bonusBars));
			}
		
	}
	
	/**
	 * Method to ask the Player to choose a LeaderCard
	 * @param message the message with all the leaderCards to choose from
	 */
	public void askLeaderCard(LeaderCardMessage message ) {
		
		if( hasToAnswer(message.getPlayerID())){
			//Ask to choose a Leader Card
			List<LeaderCard> possibleChoice = message.getAvailableLeaderCards();
			chooseLeaderCard(possibleChoice);
			
		}
	}
		
	/**
	 * Method to call to set the choice of the Leader Card
	 * @param leaderCards the list of the leaderCards
	 * @param choice the index of the leaderCard chosen in the list passed
	 */
	public void setLeaderCardChoice(List<LeaderCard> leaderCards, int choice){

		//Set the LeaderCard to the Player
		if(choice < leaderCards.size())
			player.setLeaderCard(leaderCards.get(choice));
		
		//Send a new Message to the Game Logic to notify the choice
		List<LeaderCard> leaderCardList = new ArrayList<>();
		for (LeaderCard leaderCard : leaderCards) {
			leaderCardList.add(leaderCard.clone());
		}
		LeaderCardMessage leaderCardMessage = new LeaderCardMessage(player.getPlayerID(), leaderCardList);
		try {
			leaderCardMessage.setChoice(choice);
			setChanged();
			notifyObservers(leaderCardMessage);
		} catch (WrongChoiceException e) {
			logger.debug("Wrong LeaderCard choice has been made");
			this.askLeaderCard(leaderCardMessage);
		}
		
	}
	
	/**
	 * Method to ask the player how to convert its council privilege
	 * @param message the council message with all the possible conversions
	 */
	public void askCouncilRequest( CouncilRequest message) {
		
		if( hasToAnswer(message.getPlayerID())){
			//Ask to choose a Conversion for the Council Privilege
			chooseCouncilConversion(message);
		}
		
	}
	
	/**
	 * Method to call to notify the Controller of the conversion chosen
	 * @param message the message received with all the possible conversions and the choice already setted
	 */
	public void setCouncilRequestResponse(CouncilRequest message){
		//Notify the choice to the Game Logic
		setChanged();
		notifyObservers(message);
	}
	
	/**
	 * Method to ask the Player to choose a new move
	 * @param message the message for advice on the next move like the action prototype
	 */
	public void askPlayerMove( PlayerToken message){
		
		if(hasToAnswer(message.getPlayerID())){
			//Ask to choose a Move to the Player
			askIfWantToPlay(message);
		}
	}
	
	/**
	 * Method to notify the Controller of the Move chosen
	 * @param move the move chosen 
	 */
	public void setNewMove(PlayerMove move){
		
		//Notify the Move to Game Logic
		setChanged();
		notifyObservers(move);
	}
	
	/**
	 * Method to call to notify the Controller of an empty move
	 */
	public void setEmptyMove(){

		//Notify the Game Logic with an EmptyMessage
		setChanged();
		notifyObservers(new EmptyMove(player.getPlayerID()));
	}
	
	/**
	 * Method to call to ask the player if he wants to pay the passed ban
	 * @param message the message with the ban to pay
	 */
	public void askPayBan(BanRequest message){
	
		if(hasToAnswer(message.getPlayerID())){
			//Ask to the player if he wants to pay the faithPoint instead of getting the ban
			chooseIfPayBan( message.getBanNumber());

		}
	}
	
	/**
	 * Method to notify the Controller of the choice made about the ban
	 * @param wantToPay true if the player wants to pay the ban
	 * @param banNumber the number of the ban to pay
	 */
	public void setPayBanResponse(boolean wantToPay, int banNumber){

		BanRequest message = new BanRequest(player.getPlayerID(),banNumber);
		message.setWantPayForBan(wantToPay);
		//Notify the choice to the Game Logic
		setChanged();
		notifyObservers(message);
	}
	
	/**
	 * Method to ask the player to resolve a Card request
	 * @param message the message with the request to resolve
	 */
	public void askCardRequest( CardRequest message){
		
		if(hasToAnswer(message.getPlayerID())){
			//Ask to the player to answer
			answerCardRequest(message);	
		}
	}
	
	/**
	 * Method to notify the Controller of the player choice on the card request
	 * @param message the message with the card request
	 * @param response the index of the choice made by the player
	 */ 
	public void setCardRequestResponse(CardRequest message, int response){
		
		message.setChoice(response);
		//Notify the choice to the Game Logic
		setChanged();
		notifyObservers(message);
	}
	
	/**
	 * Method to call to ask the player to resolve a leader card request
	 * @param message the message with the leader card
	 */
	public void handleLeaderFamiliarRequest(LeaderFamiliarRequest message){
		
		if(hasToAnswer(message.getPlayerID())){
			//Ask to the Player to choose a FamiliarColor
			chooseFamiliarColor(message);
		}
	}
	
	/**
	 * Method to notify the Controller of the Leader card request choice
	 * @param color the color chosen by the player
	 * @param message the message of the previous request
	 */
	public void setLeaderFamiliarRequestResponse(FamiliarColor color, LeaderFamiliarRequest message){
		//Set the color chosen to the LeaderFamiliarRequest
		
		message.setFamiliarColor(color);
		//Notify the Game Logic of the choice
		setChanged();
		notifyObservers(message);
	}
	
	//Methods For Input/Output Implemented by Concrete Classes
	
	/**
	 * Method called to ask the user a new name for the game
	 */
	public abstract void askNewPlayerID();
	
	/**
	 * Method called to ask the player to choose a bonus bar from the passed list
	 * @param bonusBarList the list of bonus bar to choose from
	 */
	protected abstract void chooseBonusBar(List<BonusBar> bonusBarList);

	/**
	 * Method called to ask the player to choose a leader card from the passed list
	 * @param leaderCardList the list of leader card to choose from
	 */
	protected abstract void chooseLeaderCard(List<LeaderCard> leaderCardList);
	

	/**
	 * Method called to ask the player to choose a council conversion from the passed list
	 * @param message message with the list of possible conversions to choose from
	 */
	protected abstract void chooseCouncilConversion(CouncilRequest message);
	

	/**
	 * Method called to ask the player to choose a new move
	 * @param prototype the action prototype of the new move
	 * @param isRetrasmission true if the previous move was wrong
	 */
	protected abstract void choosePlayerMove(ActionPrototype prototype, boolean isRestrasmission);
	

	/**
	 * Method called to ask the player to choose if pay for the ban
	 * @param banPeriod the period of the ban to pay 
	 */
	protected abstract void chooseIfPayBan(int banPeriod);
	

	/**
	 * Method called to ask the player to resolve a card  request from the passed message
	 * @param message the request to answer
	 */
	protected abstract void answerCardRequest( CardRequest message);
	

	/**
	 * Method called to ask the player to choose a color for a leader card request 
	 * @param message the message of leader card request 
	 */
	protected abstract void chooseFamiliarColor(LeaderFamiliarRequest message);
	
	/**
	 * Method called to notify the player of the activation of a leader card
	 */
	protected abstract void notifyLeaderCardActivation();

	/**
	 * Method called to notify the player of the discard of a leader card
	 */
	protected abstract void notifyLeaderCardDiscard();

	/**
	 * Method called to ask the player if he wants to play
	 */
	protected abstract void askIfWantToPlay(PlayerToken moveToken);
	
	//SETTER FOR TABLE BANS
	/**
	 * Private method for setting the first ban 
	 * @param firstBan the first ban of the game 
	 */
	private void setFirstBan(Effect firstBan){
		if(firstBan != null)
			this.table.addFirstBan(firstBan);
	}
	
	/**
	 * Private method for setting the second ban 
	 * @param secondBan the second ban of the game 
	 */
	private void setSecondBan(Effect secondBan){
		if(secondBan != null)
			this.table.addSecondBan(secondBan);
	}
	
	/**
	 * Private method for setting the third ban 
	 * @param thirdBan the third ban of the game 
	 */
	private void setThirdBan(Effect thirdBan){
		if(thirdBan != null)
			this.table.addThirdBan(thirdBan);
	}
	
	/**
	 * Method for setting the ban of the game 
	 * @param message the message with all the bans of the game
	 */
	public void setGameBans(BanMessage message){
		if(message != null){
			setFirstBan(message.getFirstEffect());
			setSecondBan(message.getSecondEffect());
			setThirdBan(message.getThirdEffect());
		}
	}
	

	/**
	 * Method for setting a ban of the game to a player
	 * @param playerID the player that receive the ban
	 * @param banPeriod the period of the ban to assign
	 */
	public void setBanToPlayer(String playerID, int banPeriod) throws ElementNotFoundException{
			
		if(searchPlayer(playerID) != null){
			if( banPeriod == 0)
				this.table.setPlayerFirstBan(playerID);
			else if( banPeriod == 1)
				this.table.setPlayerSecondBan(playerID);
			else if( banPeriod == 2)
				this.table.setPlayerThirdBan(playerID);
		}
	}
	
	//SETTER FOR DICE VALUES
	/**
	 * Setter for the Orange die value
	 * @param value the value of the die
	 */
	public void setOrangeDie(int value){
		this.table.setOrangeDie(value);
	}
	
	/**
	 * Setter for the Black die value
	 * @param value the value of the die
	 */
	public void setBlackDie(int value){
		this.table.setBlackDie(value);
	}
	
	/**
	 * Setter for the White die value
	 * @param value the value of the die
	 */
	public void setWhiteDie(int value){
		this.table.setWhiteDie(value);
	}
	
	/**
	 * Method to search a Player given its name
	 * @param playerID the name of the player to find
	 * @return the Player reference searched if exist
	 * @throws ElementNotFoundException if the player searched does not exist
	 */
	protected Player searchPlayer(String playerID) throws ElementNotFoundException{
		
		if(playerID == null)
			throw new NullPointerException();
		
		if(playerID.equals(player.getPlayerID()))
			return player;
		else{
			for (Player tempPlayer : otherPlayers) {
				if( playerID.equals(tempPlayer.getPlayerID()))
						return tempPlayer;
			}
		}
		throw new ElementNotFoundException("Player not Found");
		
	}
	
	/**
	 * Method to update the resources of a Player
	 * @param resources the HashMap of resources and their quantity
	 * @param playerID the player to update the resources
	 */
	public void setResources( HashMap<Resource, Integer> resources, String playerID){
		
		try {
			
			Player player = searchPlayer(playerID);
			player.setCurrentResources(resources);
		} catch (ElementNotFoundException e) {
			logger.error("Error in ViewVisitor setResources");
			logger.info(e);
		}
	}
	
	//SETTER FOR THE CARDS
	/**
	 * Method to set a list of new green cards
	 * @param cards the list of cards to place
	 */
	public void setGreenCards(StaticList<Card> cards){
		
		if( cards != null)
			this.table.placeGreenTower(cards);
	}
	
	/**
	 * Method to set a list of new yellow cards
	 * @param cards the list of cards to place
	 */
	public void setYellowCards(StaticList<Card> cards){
		
		if( cards != null)
			this.table.placeYellowTower(cards);	
	}
	
	/**
	 * Method to set a list of new blue cards
	 * @param cards the list of cards to place
	 */
	public void setBlueCards(StaticList<Card> cards){
	
		if( cards != null)
			this.table.placeBlueTower(cards);
	}

	/**
	 * Method to set a list of new violet cards
	 * @param cards the list of cards to place
	 */
	public void setVioletCards(StaticList<Card> cards){

		if( cards != null)
			this.table.placeVioletTower(cards);
	}
	
	//SETTERS FOR THE FAMILIAR
	/**
	 * Method to set a familiar in a green tower position 
	 * @param playerID the player that did the move
	 * @param color the familiar moved
	 * @param position the position in the green tower
	 * @throws ElementNotFoundException if the player does not exist
	 */
	public void setFamiliarInGreenTower(String playerID, FamiliarColor color, int position) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		Familiar familiar = player.getFamiliar(color);
		this.table.placeInGreenTower(familiar, position);
	}
	
	/**
	 * Method to set a familiar in a blue tower position 
	 * @param playerID the player that did the move
	 * @param color the familiar moved
	 * @param position the position in the blue tower
	 * @throws ElementNotFoundException if the player does not exist
	 */
	public void setFamiliarInBlueTower(String playerID, FamiliarColor color, int position) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		Familiar familiar = player.getFamiliar(color);
		this.table.placeInBlueTower(familiar, position);
	}	
	
	/**
	 * Method to set a familiar in a yellow tower position 
	 * @param playerID the player that did the move
	 * @param color the familiar moved
	 * @param position the position in the yellow tower
	 * @throws ElementNotFoundException if the player does not exist
	 */
	public void setFamiliarInYellowTower(String playerID, FamiliarColor color, int position) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		Familiar familiar = player.getFamiliar(color);
		this.table.placeInYellowTower(familiar, position);
	}
	
	/**
	 * Method to set a familiar in a violet tower position 
	 * @param playerID the player that did the move
	 * @param color the familiar moved
	 * @param position the position in the violet tower
	 * @throws ElementNotFoundException if the player does not exist
	 */
	public void setFamiliarInVioletTower(String playerID, FamiliarColor color, int position) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		Familiar familiar = player.getFamiliar(color);
		this.table.placeInVioletTower(familiar, position);
	}
	
	/**
	 * Method to set a familiar in a yield position 
	 * @param playerID the player that did the move
	 * @param color the familiar moved
	 * @param position the position in the yield zone
	 * @throws ElementNotFoundException if the player does not exist
	 */
	public void setFamiliarInYield(String playerID, FamiliarColor color, int position) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		Familiar familiar = player.getFamiliar(color);
		this.table.placeInYield(familiar, position);
	}
	
	/**
	 * Method to set a familiar in a produce position 
	 * @param playerID the player that did the move
	 * @param color the familiar moved
	 * @param position the position in the produce zone
	 * @throws ElementNotFoundException if the player does not exist
	 */
	public void setFamiliarInProduce(String playerID, FamiliarColor color, int position) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		Familiar familiar = player.getFamiliar(color);
		this.table.placeInProduce(familiar, position);
	}
	
	/**
	 * Method to set a familiar in a council position 
	 * @param playerID the player that did the move
	 * @param color the familiar moved
	 * @throws ElementNotFoundException if the player does not exist
	 */
	public void setFamiliarInCouncil(String playerID, FamiliarColor color) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		Familiar familiar = player.getFamiliar(color);
		this.table.placeInCouncil(familiar);
	}
	
	/**
	 * Method to set a familiar in a market position 
	 * @param playerID the player that did the move
	 * @param color the familiar moved
	 * @param position the position in the market
	 * @throws ElementNotFoundException if the player does not exist
	 */
	public void setFamiliarInMarket(String playerID, FamiliarColor color, int position) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		Familiar familiar = player.getFamiliar(color);
		this.table.placeInMarket(familiar, position);
	}
	
	//SETTER FOR THE CARDS TO A PLAYER
	/**
	 * Method to set a green card to a player
	 * @param playerID the player that receive the card
	 * @param position the position where the card is
	 * @throws ElementNotFoundException if the player does not exist
	 */
	public void setGreenCard(String playerID, int position) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		Card card = this.table.getGreenCard(position);
		player.addCard(card);
		
	}
	
	/**
	 * Method to set a blue card to a player
	 * @param playerID the player that receive the card
	 * @param position the position where the card is
	 * @throws ElementNotFoundException if the player does not exist
	 */
	public void setBlueCard(String playerID, int position) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		Card card = this.table.getBlueCard(position);
		player.addCard(card);
		
	}	
	
	/**
	 * Method to set a violet card to a player
	 * @param playerID the player that receive the card
	 * @param position the position where the card is
	 * @throws ElementNotFoundException if the player does not exist
	 */
	public void setVioletCard(String playerID, int position) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		Card card = this.table.getVioletCard(position);
		player.addCard(card);
		
	}
	
	/**
	 * Method to set a yellow card to a player
	 * @param playerID the player that receive the card
	 * @param position the position where the card is
	 * @throws ElementNotFoundException if the player does not exist
	 */
	public void setYellowCard(String playerID, int position) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		Card card = this.table.getYellowCard(position);
		player.addCard(card);
		
	}
	
	/**
	 * Method to set a leader card enabled to a player
	 * @param playerID the player that activated the card
	 * @param card the card activated
	 * @throws ElementNotFoundException if the player does not exist
	 */
	public void setEnabledLeaderCard( String playerID, LeaderCard card) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		player.enableLeaderCard(card);
	}
	
	/**
	 * Method to call when the player wants to activate a leader card
	 * @param card the card to activate
	 */
	public void sendLeaderCardUpdate(LeaderCard card) {
		LeaderCardUpdateMessage message = new LeaderCardUpdateMessage(player.getPlayerID(), card);
		setChanged();
		notifyObservers(message);
	}

	/**
	 * Method to call when a player discard a leader card
	 * @param playerID the player that discarded the Leader Card
	 * @param card the card to discard
	 */
	public void discardLeaderCard(String playerID, LeaderCard cardToDiscard) {
		if(hasToAnswer(playerID)) {
			List<LeaderCard> playerList = player.getLeaderCardList();
			for(int i = 0; i < playerList.size(); i++) {
				LeaderCard card = playerList.get(i);
				
				if(card.getName().equals(cardToDiscard.getName())) {
					playerList.remove(card);
					i = i - 1;
				}
			}
		}
	}
	
	/**
	 * Method to call when the player wants to discard a leader card
	 * @param card the card to discard
	 */
	public void sendDiscardRequest(LeaderCard cardToDiscard) {
		DiscardLeaderCard message = new DiscardLeaderCard(getViewPlayerID(), cardToDiscard);
		setChanged();
		notifyObservers(message);
	}
	
	//Methods to handle the game initialization and end
	/**
	 * Method to handle the login message, it start the game initialization phase 
	 * @param message the login message
	 */
	private void handleLoginMessage(LoginMessage message){
		if(message.existAnotherPlayer()){
			this.askNewPlayerID();
		}
		else 
			addPlayer(message.getUserName());
	}
	
	/**
	 * Method to notify the player of the final result of the Game
	 * @param message the message with the final chart of the Game
	 */
	public void handleResult(WinnerMessage message){
		if(hasToAnswer(message.getPlayerID()))
			showResult(message.getResult());
	}
	
	/**
	 * Method to reset the tableView 
	 */
	public void resetTable() {
		table.resetTable();
	}
	
	/**
	 * Method to notify the Controller of a delete of a card request, for example 
	 * if the player does not want to activate an effect
	 * @param message the request to delete
	 */
	public void sendCancelRequest(CancelCardRequest message) {
		setChanged();
		notifyObservers(message);
	}
	
	/**
	 * Method to handle the disconnection of the Player, allows the Player to restore his previous state
	 * @param message the message with all the information about the Player State
	 */
	public void handleReconnect(ReconnectMessage message ){
		
		if(hasToAnswer(message.getPlayerID())){
			reconnect(message);
			
		}
	}
	
	protected abstract void reconnect(ReconnectMessage message);
	
	/**
	 * Method that should show the final result of the Game given the final chart
	 * @param finalChart the final chart of the game
	 */
	protected abstract void showResult(List<String> finalChart);
	
	@Override
	/**
	 * The update message that redirects almost all the game messages to the ViewVisitor that then 
	 * handles them with the proper operations
	 */
	public void update(Observable sender, Object newMessage) {
		
		if (newMessage instanceof Message) {
			Message message = (Message) newMessage;
			message.accept(viewVisitor);
		}
		else if(newMessage instanceof LoginMessage){
			LoginMessage message = (LoginMessage) newMessage;
			handleLoginMessage( message );
		}
		else if(newMessage instanceof PlayersListMessage){
			PlayersListMessage message = (PlayersListMessage) newMessage;
			if(this.table == null)
				createTable(message.getPlayerList());
		}
	}

}
