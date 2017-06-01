package it.polimi.ingsw.ps42.model.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.action.ActionPrototype;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.IncreaseAction;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.exception.IsNotEmptyException;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.exception.WrongColorException;
import it.polimi.ingsw.ps42.model.request.CouncilRequest;
import it.polimi.ingsw.ps42.model.request.RequestInterface;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public class Player {
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
	
	//HashMap used for save the current resources
	private HashMap<Resource, Integer> currentResources;
	
	//HashMap used for control the correct resource when a card is enabled
	private HashMap<Resource, Integer> nextResources;
	
	private List<Effect> banList;
	private List<IncreaseAction> increaseEffect;
	
	
	private ActionPrototype bonusAction;
	private BonusBar bonusBar;
	
	//State variable of the player
	private boolean canStayInMarket;
	private boolean canPlay;
	private boolean enableBonusInTower;
	private int divisory;
	
	//The arraylist used by the gamelogic to know more from the player
	//e. g. which cost the player wants to pay
	private List<RequestInterface> requests;
	private List<CouncilRequest> councilRequests;
	
	
	
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
		
		//Initialize the Ban, the Increase Effect and the Request arraylists
		banList = new ArrayList<>();
		increaseEffect = new ArrayList<>();
		requests = new ArrayList<>();
		councilRequests = new ArrayList<>();
		
		//Set the Player bonusAction to null (required by gamelogic)
		bonusAction = null;
		
		//Set Player's state variable
		canStayInMarket = true;
		canPlay = true;
		enableBonusInTower = true;
		divisory = 1;
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
			System.out.println("Error in player.addCard(card): wrong color");
		}
	}
	
	public void increaseResource(Packet packet) {
		
		if(packet != null) {
			//Temporary variables used in the FOR EACH
			Resource tempResource;
			int tempQuantity;
			
			for (Unit unit : packet) {
				
				tempResource = unit.getResource();
				tempQuantity = unit.getQuantity();
				
				//Adding resources to the player nextResources HashMap
				tempQuantity = tempQuantity + nextResources.get(tempResource);
				
				//Upload the value in the HashMap
				nextResources.put(tempResource, tempQuantity);
			}
		}
	}
	
	public void decreaseResource(Packet packet) throws NotEnoughResourcesException {
		
		if(packet != null) {
			//Temporary variables used in the FOR EACH
			Resource tempResource;
			int tempQuantity;
				
			for (Unit unit : packet) {
					
				tempResource = unit.getResource();
				tempQuantity = unit.getQuantity();
				
				if(currentResources.get(tempResource) - tempQuantity < 0 )
					throw new NotEnoughResourcesException("Not Enough resource in player currentResources");
					
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
	
	public int getResource(Resource resource) {
		//This method return the quantity of the indicated resource
		return currentResources.get(resource);
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
	
	public void enableBan() {
		//Apply bans present in banList
		for(Effect ban : banList) {
			ban.enableEffect(this);
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
	
	public List<RequestInterface> getRequests() {
		//Get all the request in player, but this method remove all the requests arraylist
		//from the player
		
		List<RequestInterface> temp = this.requests;
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
	
	public void addRequest(RequestInterface request) {
		//Add a single request in arraylist
		requests.add(request);
	}
	
	public void synchResource() {
		//After the gamelogic control for the effects in the cards, this method 
		//upload the correct values of the resources HashMap
		copyResources(currentResources, nextResources);
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
}
