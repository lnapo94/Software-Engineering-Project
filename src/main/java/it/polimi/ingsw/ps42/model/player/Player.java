package it.polimi.ingsw.ps42.model.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.message.BanRequest;
import it.polimi.ingsw.ps42.message.BonusBarMessage;
import it.polimi.ingsw.ps42.message.CardRequest;
import it.polimi.ingsw.ps42.message.CouncilRequest;
import it.polimi.ingsw.ps42.message.LeaderCardMessage;
import it.polimi.ingsw.ps42.message.LeaderCardUpdateMessage;
import it.polimi.ingsw.ps42.message.Message;
import it.polimi.ingsw.ps42.message.PlayerToken;
import it.polimi.ingsw.ps42.message.ResourceUpdateMessage;
import it.polimi.ingsw.ps42.message.WinnerMessage;
import it.polimi.ingsw.ps42.message.leaderRequest.LeaderRequest;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.action.ActionPrototype;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.IncreaseAction;
import it.polimi.ingsw.ps42.model.effect.ObtainBan;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.exception.IsNotEmptyException;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.exception.WrongColorException;
import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

/**
 * Class that represents the model of the Player in our implementation
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class Player extends Observable {
	//This class represents the model of the Player
	
	//Max number of player's cards in each StaticList
	private static final int MAX_CARDS = 6;
	
	//The ID used in the server
	private final String ID;
	
	//The player's four familiar
	private Familiar orange;
	private Familiar black;
	private Familiar white;
	private Familiar neutral;
	
	//The player's cards. It will be six for each array at most 
	private StaticList<Card> greenCards;
	private StaticList<Card> yellowCards;
	private StaticList<Card> blueCards;
	private StaticList<Card> violetCards;
	
	//Leader card list
	private List<LeaderCard> leaderCardsList;
	private List<LeaderCard> activatedLeaderCard;
	
	//HashMap used for save the current resources
	private HashMap<Resource, Integer> currentResources;
	
	//HashMap used for control the correct resource when a card is enabled
	private HashMap<Resource, Integer> nextResources;
	
	//Only for the firsts four bans in manual and for familiar decrease
	private List<Effect> bans;
	private List<IncreaseAction> increaseEffect;
	
	
	private ActionPrototype bonusAction;
	private BonusBar bonusBar;
	
	//State variable of the player
	private boolean canStayInMarket;
	private boolean canPlay;
	private boolean enableBonusInTower;
	
	//Leader cards variables
	private boolean canPositioningEverywhere;
	private boolean noMoneyMalus;
	private boolean noMilitaryRequirements;
	private boolean fiveMoreVictoryPoints;
	
	private int divisory;
	
	//The arraylist used by the gamelogic to know more from the player
	//e. g. which cost the player wants to pay
	private List<CardRequest> requests;
	private List<CouncilRequest> councilRequests;
	
	//ArrayList used to store leader request
	private List<LeaderRequest> leaderRequests;
	
	//Logger
	private transient Logger logger = Logger.getLogger(Player.class);
	
	/**
	 * Method used to construct a player from a string, used to identify him
	 * @param ID		The string used to identify the player
	 */
	public Player(String ID) {
		//Construct the Player
		this.ID = ID;
		
		//Create the Familiar
		orange = new Familiar(this, FamiliarColor.ORANGE);
		black = new Familiar(this, FamiliarColor.BLACK);
		white = new Familiar(this, FamiliarColor.WHITE);
		neutral = new Familiar(this, FamiliarColor.NEUTRAL);
		
		//Initialize the cards arrays
		greenCards = new StaticList<>(MAX_CARDS);
		yellowCards = new StaticList<>(MAX_CARDS);
		blueCards = new StaticList<>(MAX_CARDS);
		violetCards = new StaticList<>(MAX_CARDS);
		
		//Initialize the Resources HashMaps
		currentResources = new HashMap<>();
		nextResources = new HashMap<>();
		initializeResources(currentResources);
		initializeResources(nextResources);
		
		//Initialize the Ban, the Increase Effect, the Leader Request and the Request arraylists
		increaseEffect = new ArrayList<>();
		requests = new ArrayList<>();
		councilRequests = new ArrayList<>();
		leaderRequests = new ArrayList<>();
		
		//Set the Player bonusAction to null (required by gamelogic)
		bonusAction = null;
		
		//Set Player's state variable
		canStayInMarket = true;
		canPlay = true;
		enableBonusInTower = true;
		
		canPositioningEverywhere = false;
		noMoneyMalus = false;
		noMilitaryRequirements = false;
		fiveMoreVictoryPoints = false;
		
		divisory = 1;
		
		//Initialize the leader card list
		leaderCardsList = new ArrayList<>();
		activatedLeaderCard = new ArrayList<>();
		
		//Initialize the bans arraylist
		this.bans = new ArrayList<>();
	}
	
	/**
	 * Getter for the player ID
	 * @return		A string that represents the ID of this player
	 */
	public String getPlayerID() {
		return this.ID;
	}
	
	/**
	 * Method used to set the chosen bonus bar
	 * @param bonusBar		The bonus bar to set to the player
	 */
	public void setBonusBar(BonusBar bonusBar) {
		//Set the bonusBar from gamelogic to the player
		//Then set the player variable in bonusBar
		this.bonusBar = bonusBar;
		this.bonusBar.setPlayer(this);
	}
	
	/**
	 * Method used to get the familiar given the FamiliarColor
	 * @param color		The FamiliarColor of the chosen familiar
	 * @return			A reference to the chosen familiar
	 */
	public Familiar getFamiliar(FamiliarColor color) {
		//Returns the selected familiar
		//If color isn't correct, throw an exception
		if(color == FamiliarColor.ORANGE)
			return orange;
		if(color == FamiliarColor.BLACK)
			return black;
		if(color == FamiliarColor.WHITE)
			return white;
		if(color == FamiliarColor.NEUTRAL)
			return neutral;
		if(color == null)
			return null;
		throw new WrongColorException("Error in player.getFamiliar(color): maybe the passed color is wrong");
	}
	
	/**
	 * Method used to set the value of the familiar given the color
	 * @param color		The FamiliarColor of the chosen familiar
	 * @param value		The value to set to the familiar
	 */
	public void setFamiliarValue(FamiliarColor color, int value) {
		try {
			getFamiliar(color).setValue(value);
		} catch (WrongColorException e) {
			System.out.println("Wrong color passed in player.setFamiliarValue");
			logger.info(e);
		}
	}
	
	/**
	 * Method used to get the increment of the familiar given the familiar color
	 * @param color						The FamiliarColor of the chosen familiar
	 * @return							The value of the chosen familiar
	 * @throws WrongColorException		Thrown if the color isn't a FamiliarColor
	 */
	public int getFamiliarValue(FamiliarColor color) throws WrongColorException {
		return getFamiliar(color).getValue();
	}
	
	/**
	 * Method used to add a card to the player
	 * @param card	The card to add to the player
	 */
	public void addCard(Card card) {
		StaticList<Card> temp;
		try {
			temp = getCardList(card.getColor());
			temp.add(card);
		} catch (WrongColorException e) {
			logger.error("Error in player.addCard(card): wrong color");
			logger.info(e);
		}
	}
	
	/**
	 * Method used to increment the resources of the player
	 * @param packet	The packet that contains the resources to increase
	 */
	public void increaseResource(Packet packet) {
		
		if(packet != null) {
			//Temporary variables used in the FOR EACH
			Resource tempResource;
			int tempQuantity;
			
			logger.info("Player is increasing his resources with: " + packet.print());
			
			for (Unit unit : packet) {
				
				tempResource = unit.getResource();
				tempQuantity = unit.getQuantity();
				
				//Adding resources to the player nextResources HashMap
				tempQuantity = tempQuantity + nextResources.get(tempResource);
				
				//Upload the value in the HashMap
				nextResources.put(tempResource, tempQuantity);
				
				//Check if player has a ban, in that case enable it
				for(Effect ban : bans) {
					if(ban != null && ban.getTypeOfEffect() == EffectType.OBTAIN_BAN && tempResource != null) {
						ObtainBan obtainBan = (ObtainBan) ban;
						obtainBan.setResource(tempResource);
						obtainBan.enableEffect(this);
					}
				}
			}
		}
	}
	
	/**
	 * Method used to decrease the resources of this player
	 * @param packet						The Packet with the resources to decrease
	 * @throws NotEnoughResourcesException	Thrown if the player hasn't enough resources (The calculated value goes under 0)
	 */
	public void decreaseResource(Packet packet) throws NotEnoughResourcesException {
		
		if(packet != null) {
			//Temporary variables used in the FOR EACH
			Resource tempResource;
			int tempQuantity;
			
			logger.info("Player is decreasing his resources with: " + packet.print());
			
			for (Unit unit : packet) {
					
				tempResource = unit.getResource();
				tempQuantity = unit.getQuantity();
				
				//if(currentResources.get(tempResource) - tempQuantity < 0 )
				//	throw new NotEnoughResourcesException("Not Enough resource in player currentResources");
					
				//Adding resources to the player nextResources HashMap
				tempQuantity = nextResources.get(tempResource) - tempQuantity;
				
				if(tempQuantity < 0)
					throw new NotEnoughResourcesException("tempQuantity in player.decreaseResource is"
							+ " negative, unable to continue");
				
				//Upload the value in the HashMap
				nextResources.put(tempResource, tempQuantity);
			}
		}
	}
	
	/**
	 * Method used to add an ObtainBan or a Increase Familiars ban to the player
	 * @param ban		The ban to add to the player
	 */
	public void setBan(Effect ban) {
		this.bans.add(ban);
	}
	
	/**
	 * Method used to enable the familiar ban
	 */
	public void familiarBan() {
		for(Effect ban : bans) {
			if(ban != null && ban.getTypeOfEffect() == EffectType.INCREASE_FAMILIARS)
				ban.enableEffect(this);
		}
	}
	
	/**
	 * Method used to set to 0 the specify resource
	 * 
	 * @param resource		The resource to set to 0
	 */
	public void setToZero(Resource resource) {
		nextResources.put(resource, 0);
	}
	
	/**
	 * Method used to get a value of a resource
	 * @param resource		The interested resource
	 * @return				The quantity the player has of the specify resource
	 */
	public int getResource(Resource resource) {
		return currentResources.get(resource);
	}
	
	/**
	 * Method used in view to set an entire resources HashMap to the player
	 * @param resources
	 */
	public void setCurrentResources(HashMap<Resource, Integer> resources) {
		this.currentResources = resources;
	}
	
	/**
	 * Add an IncreaseAction effect to the player's arraylist
	 * necessary in gamelogic to increase an action correctly 
	 * @param effect	The increase effect to add to the player
	 */
	public void addIncreaseEffect(IncreaseAction effect) {
		increaseEffect.add(effect);
	}
	
	/**
	 * Apply the bonus bar effect
	 * @param type		The type of the action done by the player to enable the correct bonuses
	 */
	public void enableBonus(ActionType type) {
		if(type == ActionType.PRODUCE) {
			bonusBar.productBonus();
		}
		else if(type == ActionType.YIELD) {
			bonusBar.yieldBonus();
		}
	}
	
	/**
	 * Method used to get the list of increase effect of this player
	 * @return
	 */
	public List<IncreaseAction> getIncreaseEffect() {
		return increaseEffect;
	}
	
	/**
	 * Method used to know if the player can play the first action
	 * @return
	 */
	public boolean canPlay() {
		return canPlay;
	}
	
	/**
	 * Method used to know if the player can position himself in the market
	 * @return	True if the player can stay in market, otherwise False
	 */
	public boolean canStayInMarket() {
		return canStayInMarket;
	}
	
	/**
	 * Method used to know if the player can take bonuses from tower positions
	 * @return	True if the player can take bonuses from tower, otherwise False
	 */
	public boolean canTakeBonusFromTower() {
		return enableBonusInTower;
	}
	
	/**
	 * Method used to set the can play variable
	 * @param value		The boolean value to set to the canPlay variable
	 */
	public void setCanPlay(boolean value) {
		canPlay = value;
	}
	
	/**
	 * Method used by the GameLogic to enable every round the no first action ban, if the
	 * player has that ban
	 */
	public void enableCanPlayBan() {
		if(bans != null) {
			for(Effect ban : bans) {
				if(ban.getTypeOfEffect() == EffectType.NO_FIRST_ACTION_BAN)
					ban.enableEffect(this);
			}
		}
	}
	
	/**
	 * Method used to enable the No Market Ban if the player has that ban
	 */
	public void setNoMarketBan() {
		canStayInMarket = false;
	}
	
	/**
	 * Method used to enable the No Bonus in Tower Ban if the player has that ban
	 */
	public void disableBonusInTower() {
		enableBonusInTower = false;
	}
	
	/**
	 * Method used to set the divisory of the slaves to pay to increment an action of one point
	 * @param divisory	The divisory to set
	 */
	public void setDivisory(int divisory) {
		this.divisory = divisory;
	}
	
	/**
	 * Method used to know the divisory of the current player
	 * @return	The value of the divisory
	 */
	public int getDivisory() {
		return this.divisory;
	}
 	
	/**
	 * Method to be called from the game logic when a new ban is set to the player
	 * @param message	The ban message to send
	 */
	public void notifyNewBan(Message message){
		setChanged();
		notifyObservers(message);
	}
	
	/**
	 * Method to be called from the game logic when a player 
	 * @param banPeriodNumber	The period of the ban to send to the player
	 */
	public void askIfPayTheBan( int banPeriodNumber){
		banPeriodNumber = (banPeriodNumber / 2) - 1;
		
		BanRequest message = new BanRequest( this.getPlayerID(), banPeriodNumber);
		setChanged();
		notifyObservers(message);
	}
	
	/**
	 * Method used to get the player's card list given the CardColor
	 * @param color	The color of the interested deck of cards
	 * @return		The StaticList with the player's card
	 */
	public StaticList<Card> getCardList(CardColor color) {
		//Return the correct cardlist
		if(color == CardColor.GREEN)
			return greenCards;
		if(color == CardColor.YELLOW)
			return yellowCards;
		if(color == CardColor.BLUE)
			return blueCards;
		if(color == CardColor.VIOLET)
			return violetCards;
		throw new WrongColorException("Error in player.getCardList(color), color isn't correct");
		
	}
	
	/**
	 * Get all the request in player, but this method remove all the requests in the player's list
	 * from the player
	 * @return	A List of CardRequest of the current player
	 */
	public List<CardRequest> getRequests() {
		List<CardRequest> temp = this.requests;
		this.requests = new ArrayList<>();
		return temp;
	}
	
	/**
	 * Get all the council request in player, but this method remove all the council requests
	 * in the player's list
	 * @return	A List of CouncilRequest of the current player
	 */
	public List<CouncilRequest> getCouncilRequests() {
		List<CouncilRequest> temp = this.councilRequests;
		this.councilRequests = new ArrayList<>();
		return temp;
	}
	
	/**
	 * Method used to add a council request to the player
	 * 
	 * @param councilRequest	The generated council request to add to the player
	 */
	public void addCouncilRequests(CouncilRequest councilRequest) {
		councilRequests.add(councilRequest);
	}
	
	/**
	 * Method used to add a request to the player
	 * 
	 * @param request	The generated request to add to the player
	 */
	public void addRequest(CardRequest request) {
		requests.add(request);
	}
	/** 
	 * After the GameLogic control for the effects in the cards, this method 
	 * upload the correct values of the resources HashMap
	 * Notify the View with a ResourceUpdate Message
	 */
	public void synchResource() {
		copyResources(currentResources, nextResources);
		sendResources();
	}
	
	/**
	 * Can be used when a control goes wrong
	 * Copy the currentResources Hashmap in nextResources
	 */
	public void restoreResource() {
		copyResources(nextResources, currentResources);
		sendResources();
	}

	/**
	 * This method return to the gamelogic the player's bonus action.
	 * @return
	 */
	public ActionPrototype getBonusAction() {		
		return bonusAction.clone();
	}
	
	/**
	 * Method used to set a bonus action in player. Player can have only one bonus action 
	 * @param bonusAction to add to the player
	 */
	public void setBonusAction(ActionPrototype bonusAction) {
		if(this.bonusAction != null) {
			throw new IsNotEmptyException("bonus Action isn't empty, ERROR");
		}
		this.bonusAction = bonusAction;
	}
	
	//Private Methods only for the Player class
	
	/**
	 * Method used to initialize a map with the correct resources
	 * @param map	The map to initialize
	 */
	private void initializeResources(HashMap<Resource, Integer> map) {
		//This method initialize a HashMap with all the type of Resources
		map.put(Resource.FAITHPOINT, 0);
		map.put(Resource.MILITARYPOINT, 0);
		map.put(Resource.VICTORYPOINT, 0);
		map.put(Resource.MONEY, 0);
		map.put(Resource.SLAVE, 0);
		map.put(Resource.STONE, 0);
		map.put(Resource.WOOD, 0);
	}
	
	/**
	 * Method used to copy an HashMap<Resource, Integer> into another one
	 * @param destination		The HashMap where the source will be copied
	 * @param source			The HashMap to copy
	 */
	private void copyResources(HashMap<Resource, Integer> destination, HashMap<Resource, Integer> source) {
		//Copy the source HashMap in the destination HashMap
		source.forEach((resource, quantity) -> {
			destination.put(resource, quantity);
		});
	}
	
	/**
	 * Method called by the game logic to ask a Move to the player
	 */
	public void askMove(){
		Message playerMoveMessage;
		if( this.bonusAction != null ) {
			ActionPrototype temp = bonusAction.clone();
			playerMoveMessage = new PlayerToken(this.ID, temp);
			bonusAction = null;			
		}
		else
			playerMoveMessage = new PlayerToken(this.ID);
		setChanged();
		notifyObservers(playerMoveMessage);
		
	}
	
	/**
	 * Method used to know if the player has a request or a council request
	 * @return		True if the player has a request, otherwise False
	 */
	public boolean hasRequestToAnswer() {
		return (!requests.isEmpty() || !councilRequests.isEmpty()); 
	}
	
	/**
	 * Method used to send only one request to the player
	 * @param request	The request to send to the player
	 */
	public void askRequest(CardRequest request) {
		setChanged();
		notifyObservers(request);
	}
	
	/**
	 * Method used to get and remove only one request each time
	 * @return	The first cardRequest present in player
	 */
	public CardRequest removeRequest() {
		return this.requests.remove(0);
	}
	
	/**
	 * Control if the player requests list is empty
	 * @return	True if the player has some requests, otherwise False
	 */
	public boolean isRequestsEmpty() {
		return this.requests.isEmpty();
	}
	
	/**
	 * Method used to remove all the requests
	 */
	public void removeAllRequests() {
		this.requests = new ArrayList<>();
	}

	/**
	 * Control if the player council requests list is empty
	 * @return	True if the player has some council requests, otherwise False
	 */
	public boolean isCouncilRequestsEmpty() {
		return this.councilRequests.isEmpty();
	}
	
	/**
	 * Method used to send only one council request to the player
	 * @param request	The council request to send to the player
	 */
	public void askCouncilRequest(CouncilRequest request) {
		setChanged();
		notifyObservers(request);
	}
	
	/**
	 * Method used to get and remove the one council request
	 * @return	The first council request in the player's list
	 */
	public CouncilRequest removeCouncilRequest() {
		return this.councilRequests.remove(0);
	}
	
	/**
	 * Method used to remove all the council requests in player
	 */
	public void removeAllCouncilRequests() {
		this.councilRequests = new ArrayList<>();
	}
	
	/**
	 * Method used to retrasmit a message from the GameLogic
	 * @param message		The message to retrasmit. It will be sent only if the 
	 * 						retrasmission variable is setted up.
	 */
	public void retrasmitMessage(Message message) {
		//Called in case of error in message control flow
		if(message.isRetrasmission()) {
			setChanged();
			notifyObservers(message);
		}
	}
	
	/**
	 * Called by gamelogic to ask to the player which bonusBar he wants
	 * @param bonusBars	The available List of bonus bar to send to the player
	 */
	public void askChooseBonusBar(List<BonusBar> bonusBars) {
		List<BonusBar> toThePlayer = new ArrayList<>();
		for(BonusBar bonusBar : bonusBars)
			toThePlayer.add(bonusBar.clone());
		
		//Create the message
		Message message = new BonusBarMessage(getPlayerID(), toThePlayer);
		
		setChanged();
		notifyObservers(message);
	}

	/**
	 * Method used to set one leader card to the player
	 * @param card	The leader card to set
	 */
	public void setLeaderCard(LeaderCard card) {
		card.setOwner(this);
		this.leaderCardsList.add(card);
	}
	
	/**
	 * Method used to get the list of available leader cards in player
	 * @return	The list of leader cards don't activated
	 */
	public List<LeaderCard> getLeaderCardList() {
		return this.leaderCardsList;
	}
	
	/**
	 * Method used to get the list of activated leader cards in Player
	 * @return	The list of activated leader cards
	 */
	public List<LeaderCard> getActivatedLeaderCard() {
		return this.activatedLeaderCard;
	}
	
	/**
	 * Method used to add a leader request to the player
	 * @param request	The request to add to the player
	 */
	public void addLeaderRequest(LeaderRequest request) {
		this.leaderRequests.add(request);
	}
	
	/**
	 * Method used to get all the player's leader requests 
	 * @return
	 */
	public List<LeaderRequest> getLeaderRequests() {
		return this.leaderRequests;
	}
	
	/**
	 * Method used to get and remove one leader request
	 * @return	The first leader request in the player's list
	 */
	public LeaderRequest removeLeaderRequest() {
		return leaderRequests.remove(0);
	}
	
	/**
	 * Method used to know if the leader requests list is empty
	 * @return	True if the leader requests list is empty, otherwise False
	 */
	public boolean isLeaderRequestEmpty() {
		return leaderRequests.isEmpty();
	}
	
	/**
	 * Method used to remove all the leader requests in player
	 */
	public void removeAllLeaderRequests() {
		this.leaderRequests = new ArrayList<>();
	}
	
	/**
	 * Method used to notify the client of a leader request
	 * @param request	The leader request to send to the client
	 */
	public void askLeaderRequest(LeaderRequest request) {
		setChanged();
		notifyObservers(request);
	}
	
	/**
	 * Method used to move a leader card from the available list to the activated list
	 * @param card		The card to enable
	 */
	public void setActivatedLeaderCard(LeaderCard card) {
		leaderCardsList.remove(card);
		activatedLeaderCard.add(card);
	}

	/**
	 * Enable the leader card if the player has it in his list of leader card
	 * @param chosenCard	The chosen card to enable
	 */
	public void enableLeaderCard(LeaderCard chosenCard) {
		
		for(int i = 0; i < leaderCardsList.size(); i++) {
			LeaderCard card = leaderCardsList.get(i);
			if(card.getName().equals(chosenCard.getName()) && card.canEnableCard()) {
				setActivatedLeaderCard(card);
				i = i - 1;
				
				if(card.getOnceARoundEffect() != null)
					card.enableOnceARoundEffect();
				
				if(card.getPermanentEffect() != null)
					card.enablePermanentEffect();
				
				//Create the message
				LeaderCardUpdateMessage message = new LeaderCardUpdateMessage(this.getPlayerID(), chosenCard);
				setChanged();
				notifyObservers(message);
			}
		}
	}
	
	//Leader card state variables
	
	/**
	 * Method used to enable the can positioning everywhere effect
	 */
	public void setCanPositioningEverywhere() {
		this.canPositioningEverywhere = true;
	}
	
	/**
	 * Method used to know if the can positioning everywhere effect is activated
	 * @return	True if the effect is activated, otherwise False
	 */
	public boolean canPositioningEverywhere() {
		return this.canPositioningEverywhere;
	}
	
	/**
	 * Method used to enable the no military requirements effect
	 */
	public void setMilitaryRequirements() {
		this.noMilitaryRequirements = true;
	}
	
	/**
	 * Method used to know if the no military requirements effect is activated
	 * @return	True if the effect is activated, otherwise False
	 */
	public boolean hasNoMilitaryRequirements() {
		return this.noMilitaryRequirements;
	}
	
	/**
	 * Method used to enable the no money malus effect
	 */
	public void setNoMoneyMalus() {
		this.noMoneyMalus = true;
	}
	
	/**
	 * Method used to know if the no money malus effect is activated
	 * @return	True if the effect is activated, otherwise False
	 */
	public boolean hasNoMoneyBonus() {
		return this.noMoneyMalus;
	}
	
	/**
	 * Method used to enable the five more victory points effect
	 */
	public void setFiveMoreVictoryPoint() {
		this.fiveMoreVictoryPoints = true;
	}
	
	/**
	 * Method used to know if the more victory point effect is activated
	 * @return	True if the effect is activated, otherwise False
	 */
	public boolean hasMoreVictoryPoint() {
		return this.fiveMoreVictoryPoints;
	}
	
	/**
	 * Method used to send a list of leader requests to the client
	 * 
	 * @param requests 	The list of leader requests to send to the client
	 */
	public void askLeaderRequest( List<LeaderRequest> requests){
		//Method called by the game logic to ask a response to a Leader Request
				
			for (LeaderRequest leaderRequest : requests) {
				setChanged();
				notifyObservers(leaderRequest);
			}
		}
	
	/**
	 * Method used to send to the client the available leader cards from which he can choose
	 * @param cards	The list of LeaderCard to send to the client
	 */
	public void askChooseLeaderCard(List<LeaderCard> cards) {
		List<LeaderCard> temporary = new ArrayList<>();
		
		for(LeaderCard card: cards) {
			temporary.add(card.clone());
		}
		
		LeaderCardMessage message = new LeaderCardMessage(getPlayerID(), temporary);
		setChanged();
		notifyObservers(message);
	}
	
	/**
	 * Method used to send to the player the message with the ranking of the players
	 * @param message	The WinnerMessage to send to the client
	 */
	public void notifyRanking(WinnerMessage message) {
		setChanged();
		notifyObservers(message);
	}
	
	/**
	 * Method used to send the Current Resources HashMap to the client.
	 * The HashMap is a copy of the real Current Resources HashMap in the player
	 */
	public void sendResources() {
		HashMap<Resource, Integer> resourcesCopy = new HashMap<>();
		copyResources(resourcesCopy, currentResources);
		Message updateMessage = new ResourceUpdateMessage(ID, resourcesCopy);
		setChanged();
		notifyObservers(updateMessage);
	}
	
	/**
	 * Method used to know if the player has a bonus action
	 * @return	True if the player has a bonus action, otherwise False
	 */
	public boolean hasBonusAction() {
		return this.bonusAction != null;
	}
	
}
