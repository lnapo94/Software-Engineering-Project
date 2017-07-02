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
import it.polimi.ingsw.ps42.message.CardRequest;
import it.polimi.ingsw.ps42.message.CouncilRequest;
import it.polimi.ingsw.ps42.message.EmptyMove;
import it.polimi.ingsw.ps42.message.LeaderCardMessage;
import it.polimi.ingsw.ps42.message.LoginMessage;
import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.message.PlayerMove;
import it.polimi.ingsw.ps42.message.PlayerToken;
import it.polimi.ingsw.ps42.message.PlayersListMessage;
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

public abstract class View extends Observable implements Observer {

	
	protected TableView table; 
	protected Player player;
	protected List<Player> otherPlayers; 
	protected Visitor viewVisitor;
	
	//Logger
	private Logger logger = Logger.getLogger(View.class);
	
	public View() {
		
		this.viewVisitor = new ViewVisitor(this);
		this.otherPlayers = new ArrayList<>();
		
	}
	
	public void addPlayer(String playerID){
		
		this.player = new Player(playerID);
	}
	
	public void setNewPlayerID(String playerID){
		LoginMessage message = new LoginMessage(playerID);
		addPlayer(playerID);
		setChanged();
		notifyObservers(message);
	}
	
	public String getViewPlayerID() {
		return player.getPlayerID();
	}
	
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
	
	//Methods to Ask Player Input
	protected boolean hasToAnswer(String playerID){
		
		return player.getPlayerID().equals(playerID);
	}
	
	public void askBonusBar(BonusBarMessage message) {
		
		if( hasToAnswer(message.getPlayerID())){
			//Ask to choose a BonusBar
			chooseBonusBar(message.getAvailableBonusBar());
		}

	}
	
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
	
	public void askLeaderCard(LeaderCardMessage message ) {
		
		if( hasToAnswer(message.getPlayerID())){
			//Ask to choose a Leader Card
			List<LeaderCard> possibleChoice = message.getAvailableLeaderCards();
			chooseLeaderCard(possibleChoice);
			
		}
	}
		
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
	
	public void askCouncilRequest( CouncilRequest message) {
		
		if( hasToAnswer(message.getPlayerID())){
			//Ask to choose a Conversion for the Council Privilege
			chooseCouncilConversion(message);
		}
		
	}
	
	public void setCouncilRequestResponse(CouncilRequest message){
		//Notify the choice to the Game Logic
		setChanged();
		notifyObservers(message);
	}
	
	public void askPlayerMove( PlayerToken message){
		
		if(hasToAnswer(message.getPlayerID())){
			//Ask to choose a Move to the Player
			askIfWantToPlay(message);
		}
	}
	
	public void setNewMove(PlayerMove move){
		
		//Notify the Move to Game Logic
		setChanged();
		notifyObservers(move);
	}
	
	public void setEmptyMove(){

		//Notify the Game Logic with an EmptyMessage
		setChanged();
		notifyObservers(new EmptyMove(player.getPlayerID()));
	}
	
	public void askPayBan(BanRequest message){
	
		if(hasToAnswer(message.getPlayerID())){
			//Ask to the player if he wants to pay the faithPoint instead of getting the ban
			chooseIfPayBan( message.getBanNumber());

		}
	}
	
	public void setPayBanResponse(boolean wantToPay, int banNumber){

		BanRequest message = new BanRequest(player.getPlayerID(),banNumber);
		if(wantToPay)
			message.wantPayForBan();
		//Notify the choice to the Game Logic
		setChanged();
		notifyObservers(message);
	}
	
	public void askCardRequest( CardRequest message){
		
		if(hasToAnswer(message.getPlayerID())){
			//Ask to the player to answer
			answerCardRequest(message);	
		}
	}
	
	public void setCardRequestResponse(CardRequest message, int response){
		
		message.setChoice(response);
		//Notify the choice to the Game Logic
		setChanged();
		notifyObservers(message);
	}
	
	public void handleLeaderFamiliarRequest(LeaderFamiliarRequest message){
		
		if(hasToAnswer(message.getPlayerID())){
			//Ask to the Player to choose a FamiliarColor
			chooseFamiliarColor(message);
		}
	}
	
	public void setLeaderFamiliarRequestResponse(FamiliarColor color, LeaderFamiliarRequest message){
		//Set the color chosen to the LeaderFamiliarRequest
		
		message.setFamiliarColor(color);
		//Notify the Game Logic of the choice
		setChanged();
		notifyObservers(message);
	}
	
	//Methods For Input/Output Implemented by Concrete Classes
	public abstract void askNewPlayerID();
	
	protected abstract void chooseBonusBar(List<BonusBar> bonusBarList);

	protected abstract void chooseLeaderCard(List<LeaderCard> leaderCardList);
	
	protected abstract void chooseCouncilConversion(CouncilRequest message);
	
	protected abstract void choosePlayerMove(ActionPrototype prototype, boolean isRestrasmission);
	
	protected abstract void chooseIfPayBan(int banPeriod);
	
	protected abstract void answerCardRequest( CardRequest message);
	
	protected abstract void chooseFamiliarColor(LeaderFamiliarRequest message);
	
	protected abstract void notifyLeaderCardActivation();
	
	protected abstract void notifyLeaderCardDiscard();
	
	protected abstract void askIfWantToPlay(PlayerToken moveToken);
	
	//SETTER FOR TABLE BANS
	private void setFirstBan(Effect firstBan){
		if(firstBan != null)
			this.table.addFirstBan(firstBan);
	}
	
	private void setSecondBan(Effect secondBan){
		if(secondBan != null)
			this.table.addSecondBan(secondBan);
	}
	
	private void setThirdBan(Effect thirdBan){
		if(thirdBan != null)
			this.table.addThirdBan(thirdBan);
	}
	
	public void setGameBans(BanMessage message){
		if(message != null){
			setFirstBan(message.getFirstEffect());
			setSecondBan(message.getSecondEffect());
			setThirdBan(message.getThirdEffect());
		}
	}
	
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
	public void setOrangeDie(int value){
		this.table.setOrangeDie(value);
	}
	
	public void setBlackDie(int value){
		this.table.setBlackDie(value);
	}
	
	public void setWhiteDie(int value){
		this.table.setWhiteDie(value);
	}
	
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
	public void setGreenCards(StaticList<Card> cards){
		
		if( cards != null)
			this.table.placeGreenTower(cards);
	}
	
	public void setYellowCards(StaticList<Card> cards){
		
		if( cards != null)
			this.table.placeYellowTower(cards);	
	}
	
	public void setBlueCards(StaticList<Card> cards){
	
		if( cards != null)
			this.table.placeBlueTower(cards);
	}

	public void setVioletCards(StaticList<Card> cards){

		if( cards != null)
			this.table.placeVioletTower(cards);
	}
	
	//SETTERS FOR THE FAMILIAR
	public void setFamiliarInGreenTower(String playerID, FamiliarColor color, int position) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		Familiar familiar = player.getFamiliar(color);
		this.table.placeInGreenTower(familiar, position);
	}
	
	public void setFamiliarInBlueTower(String playerID, FamiliarColor color, int position) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		Familiar familiar = player.getFamiliar(color);
		this.table.placeInBlueTower(familiar, position);
	}	
	
	public void setFamiliarInYellowTower(String playerID, FamiliarColor color, int position) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		Familiar familiar = player.getFamiliar(color);
		this.table.placeInYellowTower(familiar, position);
	}
	
	public void setFamiliarInVioletTower(String playerID, FamiliarColor color, int position) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		Familiar familiar = player.getFamiliar(color);
		this.table.placeInVioletTower(familiar, position);
	}
	
	public void setFamiliarInYield(String playerID, FamiliarColor color, int position) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		Familiar familiar = player.getFamiliar(color);
		this.table.placeInYield(familiar, position);
	}
	
	public void setFamiliarInProduce(String playerID, FamiliarColor color, int position) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		Familiar familiar = player.getFamiliar(color);
		this.table.placeInProduce(familiar, position);
	}
	
	public void setFamiliarInCouncil(String playerID, FamiliarColor color) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		Familiar familiar = player.getFamiliar(color);
		this.table.placeInCouncil(familiar);
	}
	
	public void setFamiliarInMarket(String playerID, FamiliarColor color, int position) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		Familiar familiar = player.getFamiliar(color);
		this.table.placeInMarket(familiar, position);
	}
	
	//SETTER FOR THE CARDS TO A PLAYER
	public void setGreenCard(String playerID, int position) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		Card card = this.table.getGreenCard(position);
		player.addCard(card);
		
	}
	
	public void setBlueCard(String playerID, int position) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		Card card = this.table.getBlueCard(position);
		player.addCard(card);
		
	}	
	
	public void setVioletCard(String playerID, int position) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		Card card = this.table.getVioletCard(position);
		player.addCard(card);
		
	}
	
	public void setYellowCard(String playerID, int position) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		Card card = this.table.getYellowCard(position);
		player.addCard(card);
		
	}
	
	public void setEnabledLeaderCard( String playerID, LeaderCard card) throws ElementNotFoundException{
		
		Player player = searchPlayer(playerID);
		player.enableLeaderCard(card);
	}
	
	//Methods to handle the game initialization and end
	private void handleLoginMessage(LoginMessage message){
		if(message.existAnotherPlayer()){
			this.askNewPlayerID();
		}
		else 
			addPlayer(message.getUserName());
	}
	
	public void handleResult(WinnerMessage message){
		
		showResult(message.getResult());
	}
	
	public void resetTable() {
		table.resetTable();
	}
	
	protected abstract void showResult(List<String> finalChart);
	
	@Override
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
			createTable(message.getPlayerList());
		}
	}

}
