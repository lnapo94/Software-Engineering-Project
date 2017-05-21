package it.polimi.ingsw.ps42.model.player;

import java.util.ArrayList;

import it.polimi.ingsw.ps42.model.action.ActionPrototype;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.IncreaseAction;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.Color;
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
	private Card[] greenCards;
	private Card[] yellowCards;
	private Card[] blueCards;
	private Card[] violetCards;
	
	//HashMap used for save the current resources
	private HashMap<Resource, int> currentResources;
	
	//HashMap used for control the correct resource when a card is enabled
	private HashMap<Resource, int> previousResources;
	
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
		
	}
	
	public Familiar getFamiliar(Color color) {
		
	}
	
	public void setFamiliarValue(Color color, int value) {
		
	}
	
	public int getFamiliarValue(Color color) {
		
	}
	
	public void addCard(Card card) {
		
	}
	
	public void increaseResource(Packet packet) {
		
	}
	
	public void decreaseResource(Packet packet) {
		
	}
	
	public void addIncreaseEffect(IncreaseAction effect) {
		
	}
	
	public void enableBonus(ActionType type) {
		//Apply the bonus bar effect
		
	}
	
	public void enableBan() {
		//Apply bans present in banList
	}
	
	public void enableIncreaseEffect() {
		//Apply effects present in IncreaseEffect list
	}
	
	public boolean canPlay() {
		
	}
	
	public boolean canStayInMarket() {
		
	}
	
	public boolean canTakeBonusFromTower() {
		
	}
	
	public void setCanPlay(boolean value) {
		//Set the canPlay variable
	}
	
	public void setNoMarketBan() {
		//Enable the no market ban
	}
	
	public void disableBonusInTower() {
		//Apply the no bonus in tower ban
	}
	
	public ArrayList<Card> getCardList(Color color) {
		//Return the correct cards
	}
	
	public void setRequests(ArrayList<Request> request) {
		//Set the request arraylist
	}
	
	public ArrayList<Request> getRequests() {
		//Get all the request in player
	}
	
	public void addRequest(Request request) {
		//Add a single request in arraylist
	}
	
	public void synchResource() {
		//After the gamelogic control for the effects in the cards, this method 
		//upload the correct values of the resources HashMap
	}
	
	
	
	
	
	

	

}
