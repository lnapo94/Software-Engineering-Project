package it.polimi.ingsw.ps42.model.player;

import java.util.ArrayList;
import java.util.HashMap;

import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.action.ActionPrototype;
import it.polimi.ingsw.ps42.model.action.Request;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.IncreaseAction;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.Color;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.exception.IsNotEmptyException;
import it.polimi.ingsw.ps42.model.exception.WrongColorException;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

public class Player {
	//This class represents the model of the Player
	
	//The ID used in the server
	private final String ID;
	
	//The player's four familiar
	private Familiar orange;
	private Familiar black;
	private Familiar white;
	private Familiar neutral;
	
	//The player's cards. It will be six for each array at most 
	private CardList greenCards;
	private CardList yellowCards;
	private CardList blueCards;
	private CardList violetCards;
	
	//HashMap used for save the current resources
	private HashMap<Resource, Integer> currentResources;
	
	//HashMap used for control the correct resource when a card is enabled
	private HashMap<Resource, Integer> nextResources;
	
	private ArrayList<Effect> banList;
	private ArrayList<IncreaseAction> increaseEffect;
	
	
	private ActionPrototype bonusAction;
	private BonusBar bonusBar;
	
	//State variable of the player
	private boolean canStayInMarket;
	private boolean canPlay;
	private boolean enableBonusInTower;
	
	//The arraylist used by the gamelogic to know more from the player
	//e. g. which cost the player wants to pay
	private ArrayList<Request> requests;
	
	
	
	public Player(String ID, BonusBar bonusBar) {
		//Construct the Player
		this.ID = ID;
		this.bonusBar = bonusBar;
		
		//Create the Familiar
		orange = new Familiar(this, Color.ORANGE);
		black = new Familiar(this, Color.BLACK);
		white = new Familiar(this, Color.WHITE);
		neutral = new Familiar(this, Color.NEUTRAL);
		
		//Initialize the cards arrays
		greenCards = new CardList();
		yellowCards = new CardList();
		blueCards = new CardList();
		violetCards = new CardList();
		
		//Initialize the Resources HashMaps
		currentResources = new HashMap<>();
		nextResources = new HashMap<>();
		initializeResources(currentResources);
		initializeResources(nextResources);
		
		//Initialize the Ban, the Increase Effect and the Request arraylists
		banList = new ArrayList<>();
		increaseEffect = new ArrayList<>();
		requests = new ArrayList<>();
		
		//Set the Player bonusAction to null (required by gamelogic)
		bonusAction = null;
		
		//Set Player's state variable
		canStayInMarket = true;
		canPlay = true;
		enableBonusInTower = true;
	}
	
	public String getPlayerID() {
		return this.ID;
	}
	
	public Familiar getFamiliar(Color color) throws WrongColorException{
		//Returns the selected familiar
		//If color isn't correct, throw an exception
		if(color == Color.ORANGE)
			return orange;
		if(color == Color.BLACK)
			return black;
		if(color == Color.WHITE)
			return white;
		if(color == Color.NEUTRAL)
			return neutral;
		throw new WrongColorException("Wrong color in player.getFamiliar method");
		
	}
	
	public void setFamiliarValue(Color color, int value) {
		try {
			getFamiliar(color).setValue(value);
		} catch (WrongColorException e) {
			System.out.println("Wrong color passed in player.setFamiliarValue");
		}
	}
	
	public int getFamiliarValue(Color color) throws WrongColorException {
		return getFamiliar(color).getValue();
	}
	
	public void addCard(Card card) {
		CardList temp;
		try {
			temp = getCardList(card.getColor());
			temp.add(card);
		} catch (WrongColorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void increaseResource(Packet packet) {
		//TODO Iterator in Packet
	}
	
	public void decreaseResource(Packet packet) {
		//TODO Iterator in Packet
	}
	
	public void addIncreaseEffect(IncreaseAction effect) {
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
	
	public void enableIncreaseEffect() {
		//Apply effects present in IncreaseEffect list
		for(IncreaseAction e : increaseEffect) {
			e.enableEffect(this);
		}
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
	
	public CardList getCardList(Color color) throws WrongColorException {
		//Return the correct cardlist
		if(color == Color.GREEN)
			return greenCards;
		if(color == Color.YELLOW)
			return yellowCards;
		if(color == Color.BLUE)
			return blueCards;
		if(color == Color.VIOLET)
			return violetCards;
		throw new WrongColorException("Error in player.getCardList(color), color isn't correct");
		
	}
	
	public void setRequests(ArrayList<Request> requests) {
		//Set the request arraylist
		this.requests = requests;
	}
	
	public ArrayList<Request> getRequests() {
		//Get all the request in player, but this method remove all the requests arraylist
		//from the player
		
		ArrayList<Request> temp = this.requests;
		this.requests = null;
		return temp;
	}
	
	public void addRequest(Request request) {
		//Add a single request in arraylist
		requests.add(request);
	}
	
	public void synchResource() {
		//After the gamelogic control for the effects in the cards, this method 
		//upload the correct values of the resources HashMap
		currentResources = nextResources;
		initializeResources(nextResources);
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
	
	//Private Methods only for the player
	
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
	
	
	
	
	
	
	

	

}
