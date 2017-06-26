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

/*TO-DO:
 * 1: controllare la posizione bonus per player nei controlli, e aggiungere var di stato
 * 2: agg var di stato per evitare moneyMalus più controlli in action
 * 3: agg var di stato per il set dei familiari a 5 più controlli in player
 * 4: gestire gli increase familiar effect come array list in player
 * 5: agg var di stato per il raddoppio sui bonus ricevuti da eff imm delle carte
 * 6: agg var di stato per requisito pti militari
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
	
	public String getPlayerID() {
		return this.ID;
	}
	
	public void setBonusBar(BonusBar bonusBar) {
		//Set the bonusBar from gamelogic to the player
		//Then set the player variable in bonusBar
		this.bonusBar = bonusBar;
		this.bonusBar.setPlayer(this);
	}
	
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
	
	public void setFamiliarValue(FamiliarColor color, int value) {
		try {
			getFamiliar(color).setValue(value);
		} catch (WrongColorException e) {
			System.out.println("Wrong color passed in player.setFamiliarValue");
			logger.info(e);
		}
	}
	
	public int getFamiliarValue(FamiliarColor color) throws WrongColorException {
		return getFamiliar(color).getValue();
	}
	
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
					if(ban != null && ban.getTypeOfEffect() == EffectType.OBTAIN_BAN) {
						ObtainBan obtainBan = (ObtainBan) ban;
						obtainBan.setResource(tempResource);
						obtainBan.enableEffect(this);
					}
				}
			}
		}
	}
	
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
	
	public void setBan(Effect ban) {
		this.bans.add(ban);
	}
	
	public void familiarBan() {
		for(Effect ban : bans) {
			if(ban != null && ban.getTypeOfEffect() == EffectType.INCREASE_FAMILIARS)
				ban.enableEffect(this);
		}
	}
	
	public void setToZero(Resource resource) {
		//Set a player resource to zero. This method is used in ObtainBan in case
		//the decrease resource goes under zero
		nextResources.put(resource, 0);
	}
	
	public int getResource(Resource resource) {
		//This method return the quantity of the indicated resource
		return currentResources.get(resource);
	}
	
	public void setCurrentResources(HashMap<Resource, Integer> resources) {
		this.currentResources = resources;
	}
	
	public void addIncreaseEffect(IncreaseAction effect) {
		//Add an IncreaseAction effect to the player's arraylist
		//necessary in gamelogic to increase an action correctly 
		
		increaseEffect.add(effect);
	}
	
	public void enableBonus(ActionType type) {
		//Apply the bonus bar effect
		if(type == ActionType.PRODUCE) {
			bonusBar.productBonus();
		}
		else if(type == ActionType.YIELD) {
			bonusBar.yieldBonus();
		}
	}
	
	public List<IncreaseAction> getIncreaseEffect() {
		return increaseEffect;
	}
	
	public boolean canPlay() {
		return canPlay;
	}
	
	public boolean canStayInMarket() {
		return canStayInMarket;
	}
	
	public boolean canTakeBonusFromTower() {
		return enableBonusInTower;
	}
	
	public void setCanPlay(boolean value) {
		//Set the canPlay variable
		canPlay = value;
	}
	
	public void setNoMarketBan() {
		//Enable the no market ban
		canStayInMarket = false;
	}
	
	public void disableBonusInTower() {
		//Apply the no bonus in tower ban
		enableBonusInTower = false;
	}
	
	public void setDivisory(int divisory) {
		this.divisory = divisory;
	}
	
	public int getDivisory() {
		return this.divisory;
	}
 	
	public void notifyNewBan(Message message){
		//Method to be called from the game logic when a new ban is set to the player
		setChanged();
		notifyObservers(message);
	}
	
	public void askIfPayTheBan( int banPeriodNumber){
		//Method to be called from the game logic when a player 
		
		BanRequest message = new BanRequest( this.getPlayerID(), banPeriodNumber);
		setChanged();
		notifyObservers(message);
	}
	
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
	
	public List<CardRequest> getRequests() {
		//Get all the request in player, but this method remove all the requests arraylist
		//from the player
		
		List<CardRequest> temp = this.requests;
		this.requests = new ArrayList<>();
		return temp;
	}
	
	public List<CouncilRequest> getCouncilRequests() {
		List<CouncilRequest> temp = this.councilRequests;
		this.councilRequests = new ArrayList<>();
		return temp;
	}
	
	public void addCouncilRequests(CouncilRequest councilRequest) {
		councilRequests.add(councilRequest);
	}
	
	public void addRequest(CardRequest request) {
		//Add a single request in arraylist
		requests.add(request);
	}
	
	public void synchResource() {
		//After the gamelogic control for the effects in the cards, this method 
		//upload the correct values of the resources HashMap
		//Notify the View with a ResourceUpdate Message
		copyResources(currentResources, nextResources);
		sendResources();
	}
	
	public void restoreResource() {
		//Can be used when a control goes wrong
		//Copy the currentResources Hashmap in nextResources
		//This method maybe is useless
		copyResources(nextResources, currentResources);
	}

	
	public ActionPrototype getBonusAction() {
		//This method return to the gamelogic the player's bonus action.
		//This method also remove the ActionPrototype from the bonusAction variable
		
		ActionPrototype temp = bonusAction;
		this.bonusAction = null;
		return temp;
	}
	
	public void setBonusAction(ActionPrototype bonusAction) {
		if(this.bonusAction != null) {
			throw new IsNotEmptyException("bonus Action isn't empty, ERROR");
		}
		this.bonusAction = bonusAction;
	}
	
	//Private Methods only for the Player class
	
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
	
	private void copyResources(HashMap<Resource, Integer> destination, HashMap<Resource, Integer> source) {
		//Copy the source HashMap in the destination HashMap
		source.forEach((resource, quantity) -> {
			destination.put(resource, quantity);
		});
	}
	
	public void askMove(){
	//Method called by the game logic to ask a Move to the player	
		Message playerMoveMessage;
		if( this.bonusAction != null )
			playerMoveMessage = new PlayerToken(this.ID, this.bonusAction.clone());
		else
			playerMoveMessage = new PlayerToken(this.ID);
		setChanged();
		notifyObservers(playerMoveMessage);
		
	}
	
	public boolean hasRequestToAnswer() {
		return (!requests.isEmpty() || !councilRequests.isEmpty()); 
	}
	
	public void askRequest( List<CardRequest> requests){
	//Method called by the game logic to ask a response to a generic Request
		
		for (CardRequest cardRequest : requests) {
			setChanged();
			notifyObservers(cardRequest);
		}
	}
	
	public void askRequest(CardRequest request) {
		setChanged();
		notifyObservers(request);
	}
	
	public CardRequest removeRequest() {
		return this.requests.remove(0);
	}
	
	public boolean isRequestsEmpty() {
		return this.requests.isEmpty();
	}
	
	public void removeAllRequests() {
		this.requests = new ArrayList<>();
	}
	
	public void askCouncilRequest( List<CouncilRequest> requests){
	//Method called by the game logic to ask a response to a Council Request
			
		for (CouncilRequest councilRequest : requests) {
			setChanged();
			notifyObservers(councilRequest);
		}
	}
	
	public boolean isCouncilRequestsEmpty() {
		return this.councilRequests.isEmpty();
	}
	
	public void askCouncilRequest(CouncilRequest request) {
		setChanged();
		notifyObservers(request);
	}
	
	public CouncilRequest removeCouncilRequest() {
		return this.councilRequests.remove(0);
	}
	
	public void removeAllCouncilRequests() {
		this.councilRequests = new ArrayList<>();
	}
	
	public void retrasmitMessage(Message message) {
		//Called in case of error in message control flow
		if(message.isRetrasmission()) {
			setChanged();
			notifyObservers(message);
		}
	}
	
	public void askChooseBonusBar(List<BonusBar> bonusBars) {
		//Called by gamelogic to ask to the player which bonusBar he wants
		List<BonusBar> toThePlayer = new ArrayList<>();
		for(BonusBar bonusBar : bonusBars)
			toThePlayer.add(bonusBar.clone());
		
		//Create the message
		Message message = new BonusBarMessage(getPlayerID(), toThePlayer);
		
		setChanged();
		notifyObservers(message);
	}
	//TODO carte leader
	public void setLeaderCard(LeaderCard card) {
		this.leaderCardsList.add(card);
	}
	
	public List<LeaderCard> getLeaderCardList() {
		return this.leaderCardsList;
	}
	
	public List<LeaderCard> getActivatedLeaderCard() {
		return this.activatedLeaderCard;
	}
	
	public void addLeaderRequest(LeaderRequest request) {
		this.leaderRequests.add(request);
	}
	
	public List<LeaderRequest> getLeaderRequests() {
		return this.leaderRequests;
	}
	
	public LeaderRequest removeLeaderRequest() {
		return leaderRequests.remove(0);
	}
	
	public boolean isLeaderRequestEmpty() {
		return leaderRequests.isEmpty();
	}
	
	public void removeAllLeaderRequests() {
		this.leaderRequests = new ArrayList<>();
	}
	
	public void askLeaderRequest(LeaderRequest request) {
		setChanged();
		notifyObservers(request);
	}
	
	public void enableLeaderCard(LeaderCard chosenCard) {
		//Enable the leader card if the player has it in his list of leader card
		
		while(!leaderCardsList.isEmpty()) {
			LeaderCard card = leaderCardsList.get(0);
			if(card.getName() == chosenCard.getName() && card.canEnableCard()) {
				leaderCardsList.remove(leaderCardsList.indexOf(card));
				activatedLeaderCard.add(card);
				
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
	public void setCanPositioningEverywhere() {
		this.canPositioningEverywhere = true;
	}
	
	public boolean canPositioningEverywhere() {
		return this.canPositioningEverywhere;
	}
	
	public void setMilitaryRequirements() {
		this.noMilitaryRequirements = true;
	}
	
	public boolean hasNoMilitaryRequirements() {
		return this.noMilitaryRequirements;
	}
	
	public void setNoMoneyMalus() {
		this.noMoneyMalus = true;
	}
	
	public boolean hasNoMoneyBonus() {
		return this.noMoneyMalus;
	}
	
	public void setFiveMoreVictoryPoint() {
		this.fiveMoreVictoryPoints = true;
	}
	
	public boolean hasMoreVictoryPoint() {
		return this.fiveMoreVictoryPoints;
	}
	
	public void askLeaderRequest( List<LeaderRequest> requests){
		//Method called by the game logic to ask a response to a Leader Request
				
			for (LeaderRequest leaderRequest : requests) {
				setChanged();
				notifyObservers(leaderRequest);
			}
		}
	
	public void askChooseLeaderCard(List<LeaderCard> cards) {
		List<LeaderCard> temporary = new ArrayList<>();
		
		for(LeaderCard card: cards) {
			temporary.add(card.clone());
		}
		
		LeaderCardMessage message = new LeaderCardMessage(getPlayerID(), temporary);
		setChanged();
		notifyObservers(message);
	}
	
	public void notifyRanking(WinnerMessage message) {
		setChanged();
		notifyObservers(message);
	}
	
	public void sendResources() {
		HashMap<Resource, Integer> resourcesCopy = new HashMap<>();
		copyResources(resourcesCopy, currentResources);
		Message updateMessage = new ResourceUpdateMessage(ID, resourcesCopy);
		setChanged();
		notifyObservers(updateMessage);
	}
	
}
