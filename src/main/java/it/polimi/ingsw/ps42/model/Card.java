package it.polimi.ingsw.ps42.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.message.CardRequest;
import it.polimi.ingsw.ps42.message.FinalRequest;
import it.polimi.ingsw.ps42.message.ImmediateRequest;
import it.polimi.ingsw.ps42.message.PayRequest;
import it.polimi.ingsw.ps42.message.PermanentRequest;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.CardColor;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

/**
 * Class that represent the card object of the Lorenzo il Magnifico game
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class Card implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3184629522167274982L;
	/*Class for Card, when created is placed in a position and do not has a owner, 
	 * the owner is setted only when a player takes the card. In every ArrayList 
	 * (Packet or Effect) the elements have to be considered in OR while IN the single 
	 * Packet/Effect they are in AND
	 */
	private String name;
	private String description;
	private CardColor color;
	private int period;
	private int level;
	private transient Player owner;
	private List<Packet> costs;
	private List<Packet> requirements;
	private List<Effect> immediateEffects;
	private List<Effect> permanentEffects;
	private List<Effect> finalEffects;
	
	//ArrayList used for check if player can pay to obtain the card or enable the effect
	private List<Printable> possibleChoice;
	private List<Integer> possibleChoiceIndex;
	
	//ArrayList of cost used for the cost player can pay effectively
	private List<Packet> effectivelyCosts;
	
	//Logger
	private transient Logger logger;
	
	public Card() {
		 logger = Logger.getLogger(Card.class);
	}
	
	/**
	 * The constructor of the card
	 * 
	 * @param name				The name to give to the card
	 * @param description		A brief description
	 * @param color				The color of the card (Green, Yellow, Blue or Violet)
	 * @param period			The period of the card
	 * @param level				The level of the action in the card
	 * @param costs				All the packets that represents a cost (The costs are in "OR")
	 * @param immediateEffects	All the effects activated when the card is taken (The effects are in "OR")
	 * @param requirements		All the packets of the requirements (The requirements are in "OR")
	 * @param permanentEffect	All the effects activated with a yield (Green) or product (Yellow) action (The effects are in "OR")
	 * @param finalEffects		All the final effects activated at the end of the match (The effects are in "OR")
	 */
	public Card(String name, String description, CardColor color, int period, 
			int level, List<Packet> costs, List<Effect> immediateEffects, List<Packet> requirements,
			List<Effect> permanentEffect, List<Effect> finalEffects){
		//Construct the card
		this();
		this.name = name;
		this.description = description;
		this.color = color;
		this.period = period;
		this.level = level;
		this.costs = costs;
		this.requirements = requirements;
		this.immediateEffects = immediateEffects;
		this.permanentEffects = permanentEffect;
		this.finalEffects = finalEffects;
		
		//Construct the arraylist
		this.possibleChoice = new ArrayList<>();
		this.possibleChoiceIndex = new ArrayList<>();
		this.effectivelyCosts = new ArrayList<>();
	}
	
	/**
	 * Getter to know the card name
	 * @return	The name of the card
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Getter to know the card description
	 * @return	The description of the card
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Getter to know the card color
	 * @return	The color of the card
	 */
	public CardColor getColor() {
		return color;
	}
	
	/**
	 * Getter to know the card period
	 * @return	The period of the card
	 */
	public int getPeriod() {
		return period;
	}
	
	/**
	 * Getter to know the card action level
	 * @return	The action level of the card
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * Getter to know the card costs
	 * @return	The list of packet which represent the costs. This list is only a copy of real costs
	 */
	public List<Packet> getCosts() {
		//Return a copy of costs array
		return copyPacketList(costs);
	}

	/**
	 * Method used to set the owner of the card
	 * @param owner		The owner of the card
	 */
	public void setPlayer(Player owner) {
		this.owner = owner;
	}
	
	
	/*	PAY A CARD METHODS	*/
	
	/**
	 * Method used to pay a card if is known which cost the player wants to pay
	 * @param player						The interested player
	 * @param choice						The cost to pay
	 * @param discount						A possible discount (If there isn't a discount, set it to null)
	 * @throws NotEnoughResourcesException	Thrown if the player hasn't enough resources
	 */
	public void payCard(Player player, int choice, Packet discount) throws NotEnoughResourcesException {
		Packet chosenCost = costs.get(choice);
		
		//Discount the cost
		if(discount != null) {
			for(Unit unit : discount) {
				chosenCost.subtractUnit(unit);
			}
		}
		
		player.decreaseResource(chosenCost);
	}
	
	/**
	 * Method used to pay a card, even if the cost to pay is unknown
	 * @param player						The interested player
	 * @param discount						A possible discount (If there isn't a discount, set it to null)
	 * @throws NotEnoughResourcesException	Thrown if the player hasn't enough resoures
	 */
	public void payCard(Player player, Packet discount) throws NotEnoughResourcesException {
		
		//First of all, control if card costs are null
		if(costs != null && !costs.isEmpty()) {
			
			//Then check the requirements, maybe player can pay the card but he hasn't the requirements, such as enough military point
			checkRequirements(player);
			
			if(effectivelyCosts.size() == 0)
				throw new NotEnoughResourcesException("Player has not enough resources");
			
			//Then check if there is a discount to apply
			increaseDiscount(player, discount);
			
			//At this point, card can have only one cost or higher
			//If there is only one cost, control if player can pay it
			//In this case, the player can pay immediately, without a request
			if(effectivelyCosts.size() == 1) {
				if(checkPlayerCanPay(effectivelyCosts.get(0), player, discount)) {
					decreaseDiscount(player, discount);
					payCard(player, 0, discount);
				}
				else {
					decreaseDiscount(player, discount);
					throw new NotEnoughResourcesException("Player hasn't enough resource");
				}
			}
			
			//This branch will enable only if there is more costs in card
			if(effectivelyCosts.size() > 1) {
				
				//Then control the cost player can pay
				for(Packet cost : effectivelyCosts) {
					if(checkPlayerCanPay(cost, player, discount)) {
						possibleChoice.add(cost.clone());
						possibleChoiceIndex.add(costs.indexOf(cost));
					}
				}
				
				//The costs player can afford will be send to the client. In this way player can choose which cost
				//he want to pay
				if(possibleChoice.isEmpty() || possibleChoiceIndex.isEmpty())  {
					decreaseDiscount(player, discount);
					throw new NotEnoughResourcesException("The possibleChoice array is empty, cannot pay this");
				}
				decreaseDiscount(player, discount);
				CardRequest request = new PayRequest(player.getPlayerID(), this, copyInteger(possibleChoiceIndex), copyPrintable(possibleChoice), discount);
				player.addRequest(request);
				resetPossibleChoice();
			}
		}
	}
	
	/**
	 * Private method used to increase the resources with the discount, only for check
	 * @param player	The interested player
	 * @param discount	The discount to verify
	 */
	private void increaseDiscount(Player player, Packet discount) {
		if(discount != null) {
			player.increaseResource(discount);
			player.synchResource();
		}
	}
	
	/**
	 * Private method used to decrease the resources with the discount, only after a check
	 * @param player	The interested player
	 * @param discount	The verified discount 
	 */
	private void decreaseDiscount(Player player, Packet discount) throws NotEnoughResourcesException {
		if(discount != null) {
			player.decreaseResource(discount);
			player.synchResource();
		}
	}
	
	/*	IMMEDIATE EFFECT */
	
	/**
	 * Method used to enable the immediate effect of the card
	 * 
	 * @throws NotEnoughResourcesException	Thrown if the player hasn't enough resources
	 */
	public void enableImmediateEffect() throws NotEnoughResourcesException {
		if(immediateEffects != null && !( immediateEffects.isEmpty() ) ) {
			//If the effectList exist
			if(canEnableNowEffect(immediateEffects))
				enableEffect(0, immediateEffects, owner);
			else{
				if(controlPossibleChoice(immediateEffects))
					resetPossibleChoice();
				else {
					CardRequest request = new ImmediateRequest(owner.getPlayerID(), this, copyInteger(possibleChoiceIndex), copyPrintable(possibleChoice));
					owner.addRequest(request);
					resetPossibleChoice();
				}
			}
		}
	}
	
	/**
	 * Private method used to copy a list of Printable
	 * @param array		The list to copy
	 * @return			The copied list
	 */
	private List<Printable> copyPrintable(List<Printable> array) {
		List<Printable> temporary = new ArrayList<>();
		for(Printable element : array)
			temporary.add(element);
		return temporary;
	}
	
	/**
	 * Private method used to copy a list of Integer
	 * @param array		The list to copy
	 * @return			The copied list
	 */
	private List<Integer> copyInteger(List<Integer> array) {
		List<Integer> temporary = new ArrayList<>();
		for(Integer element : array) 
			temporary.add(element);
		return temporary;
	}
	
	/**
	 * Method used to enable the immediate effect of the card, if it is known
	 * the player choice
	 * 
	 * @param  choice						The effect which player wants to enable
	 * @throws NotEnoughResourcesException	Thrown if the player hasn't enough resources
	 */
	public void enableImmediateEffect(int choice, Player player) {
		//Try to enable the effect
		logger = Logger.getLogger(Card.class);
		try {
			enableEffect(choice, immediateEffects, player);
		} catch(ArithmeticException e) {
			//The chosen effect is no more applicable, try to choose another effect
			logger.info("The chosen effect is no more applicable, try to choose another effect");
			logger.info(e);
			try {
				enableImmediateEffect();
			} catch (NotEnoughResourcesException e1) {
				logger.info("There isn't more effect to enable in card");
				logger.info(e1);
			}
		}
	}
	/* END IMMEDIATE EFFECT */
	
	/*	PERMANENT EFFECT */
	
	/**
	 * Method used to enable the permanent effect of the card
	 * 
	 * @throws NotEnoughResourcesException	Thrown if the player hasn't enough resources
	 */
	public void enablePermanentEffect() throws NotEnoughResourcesException {
		if(permanentEffects != null && !( permanentEffects.isEmpty() ) ) {
			//If the effectList exist
			if(canEnableNowEffect(permanentEffects))
				enableEffect(0, permanentEffects, owner);
			else {
				if(controlPossibleChoice(permanentEffects))
					resetPossibleChoice();
				else {
					CardRequest request = new PermanentRequest(owner.getPlayerID(), this, copyInteger(possibleChoiceIndex), copyPrintable(possibleChoice));
					owner.addRequest(request);
					resetPossibleChoice();
				}
			}
		}
	}
	
	/**
	 * Method used to enable the permanent effect of the card, if it is known
	 * the player choice
	 * 
	 * @param  choice						The effect which player wants to enable
	 * @throws NotEnoughResourcesException	Thrown if the player hasn't enough resources
	 */
	public void enablePermanentEffect(int choice, Player player) {
		logger = Logger.getLogger(Card.class);
		try {
			enableEffect(choice, permanentEffects, player);
		} catch(ArithmeticException e) {
		//The chosen effect is no more applicable, try to choose another effect
		try {
			logger.info("The chosen effect is no more applicable, try to choose another effect");
			logger.info(e);
			enablePermanentEffect();
		} catch (NotEnoughResourcesException e1) {
			logger.info("There is not permanent effect applicable");
			logger.info(e1);
		}
	}
	}
	/*	END PERMANENT EFFECT */
	
	/* FINAL EFFECT */
	
	/**
	 * Method used to enable the final effect of the card
	 * 
	 * @throws NotEnoughResourcesException	Thrown if the player hasn't enough resources
	 */
	public void enableFinalEffect() throws NotEnoughResourcesException {
		if(finalEffects != null && !( finalEffects.isEmpty() ) ) {
			//If the effectList exist
			if(canEnableNowEffect(finalEffects))
				enableEffect(0, finalEffects, owner);
			else {
				if(controlPossibleChoice(finalEffects))
					resetPossibleChoice();
				else {
					CardRequest request = new FinalRequest(owner.getPlayerID(), this, copyInteger(possibleChoiceIndex), copyPrintable(possibleChoice));
					owner.addRequest(request);
					resetPossibleChoice();
				}
			}
		}
	}
	
	/**
	 * Method used to enable the final effect of the card, if it is known
	 * the player choice
	 * 
	 * @param  choice						The effect which player wants to enable
	 * @throws NotEnoughResourcesException	Thrown if the player hasn't enough resources
	 */
	public void enableFinalEffect(int choice, Player player) {
		logger = Logger.getLogger(Card.class);
		try {
			enableEffect(choice, finalEffects, player);
		} catch(ArithmeticException e) {
			//The chosen effect is no more applicable, try to choose another effect
			try {
				logger.info("The chosen effect is no more applicable, try to choose another effect");
				logger.info(e);
				enableFinalEffect();
			} catch (NotEnoughResourcesException e1) {
				logger.info("There is no more final effect in card");
				logger.info(e1);
			}
		}
	}
	/* END FINAL EFFECT */
	
	//PRIVATE METHODS FOR CARD CLASS
	
	/**
	 * Used to reset the possible choice when a request is created
	 */
	private void resetPossibleChoice() {
		this.possibleChoice = new ArrayList<>();
		this.possibleChoiceIndex = new ArrayList<>();
	}
	
	/**
	 * Used to control which effect can be activated after a control done before this method
	 * 
	 * @param effectList					The list of effect to control
	 * @return								True if the effect can be activated immediately, otherwise False
	 * @throws NotEnoughResourcesException	Thrown if the player can't enable any effect
	 */
	private boolean controlPossibleChoice(List<Effect> effectList) throws NotEnoughResourcesException {
		//ONLY PRIVATE request
		//Used only to verify if the arrays of choices isn't empty
		if(possibleChoice.isEmpty() || possibleChoiceIndex.isEmpty()) 
			throw new NotEnoughResourcesException("The possibleChoice array is empty, cannot pay this");
		
		if(possibleChoice.size() == 1) {
			Effect effect = effectList.get(possibleChoiceIndex.get(0));
			if(effect.getTypeOfEffect() != EffectType.OBTAIN) {
				effect.enableEffect(owner);
				return true;	
			}

		}
		return false;
	}
	
	/**
	 * Used to enable the effect
	 * 
	 * @param choice		The index of the chosen effect
	 * @param effectList	The list in which there is the effect to enable
	 * @param player		The interested player
	 */
	private void enableEffect(int choice, List<Effect> effectList, Player player) {
			effectList.get(choice).enableEffect(player);
	}
	
	/**
	 * Method used to verify if the effect list can be enabled immediately
	 * 
	 * @param effectList	The list to control
	 * @return				True if the effect can be enabled immediately, otherwise False
	 */
	private boolean canEnableNowEffect(List<Effect> effectList) {
		//Control if the effect can be enabled immediately
		
		if(effectList.size() == 1) {
			//If card has only one effect, control it and return if it is activated
			return checkActivable(effectList.get(0), 0);	
		}
		for(Effect effect : effectList) {
			//Else if card has more than one effect, control all the effect, and add it to the possibleChoice array
			//But return false because the card need to know the user's choice
			if(checkActivable(effect, effectList.indexOf(effect))) {
				possibleChoiceIndex.add(effectList.indexOf(effect));
				possibleChoice.add(effect.clone());
			}
		}
		return false;
	}
	
	/**
	 * Method used to verify if the effect can be enabled immediately
	 * 
	 * @param effectList	The single effect to control
	 * @return				True if the effect can be enabled immediately, otherwise False
	 */
	private boolean checkActivable(Effect effect, int index) {
		//Return true if the effect can be enabled
		boolean checker = true;
		
		//If the effect is obtain, cast the effect to get the obtain costs
		if(effect.getTypeOfEffect() == EffectType.OBTAIN) {
			Obtain obtainEffect = (Obtain)effect;
			Packet obtainCosts = obtainEffect.getCosts();
			
			//If there is at least one cost
			if(obtainCosts != null) {
				//Check if player can pay
				//If player can pay this cost, then checker = false because the effect cannot be immediately consumed
				//Else the effect cannot be payed nor added to the possible choice array
				checker = checkOwnerCanPay(obtainCosts, null);
				if(checker == true) {
					possibleChoice.add(effect.clone());
					possibleChoiceIndex.add(index);
					checker = false;
				}
			}
		}
		return checker;
	}
	
	/**
	 * Method used to know if the owner can pay a cost
	 * 
	 * @param costs		The packet of cost to verify
	 * @param discount	A possible discount (If there isn't discount, set it to null)
	 * @return			True if he can pay, otherwise False
	 */
	private boolean checkOwnerCanPay(Packet costs, Packet discount) {
		//Check if the owner can pay an effect
		return checkPlayerCanPay(costs, owner, discount);
	}
	
	/**
	 * Method used to know if a generic player can pay a cost
	 * 
	 * @param costs		The packet of cost to verify
	 * @param player 	The generic player to control
	 * @param discount	A possible discount (If there isn't discount, set it to null)
	 * @return			True if he can pay, otherwise False
	 */
	private boolean checkPlayerCanPay(Packet costs, Player player, Packet discount) {
		//Check if a generic player can pay a packet of costs
		for (Unit unit : costs) {
			if(unit.getQuantity() > player.getResource(unit.getResource())) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method used to check if the player satisfy at least one requirement
	 * 
	 * @param player	The interested player
	 */
	private void checkRequirements(Player player) {
		//Used to fill the effectivelyCosts array to pay the card
		//Costs with requirement before than normal costs
		int i = 0;
		if(requirements != null) {
			for(Packet requirement : requirements) {
				boolean checker = true;
				for(Unit unit : requirement) {
					if(unit.getQuantity() > player.getResource(unit.getResource()))
						checker = false;
				}
				if(checker == true)
					effectivelyCosts.add(costs.get(requirements.indexOf(requirement)));
				i++;
			}
		}
		
		//Add the other costs to the effectivelyCosts
		for(int j = i; j < costs.size(); j++){
			effectivelyCosts.add(costs.get(j));
		}
	}
	
	/**
	 * Used to copy a list of packet
	 * 
	 * @param start		The packet list to copy
	 * @return			the copied list
	 */
	private List<Packet> copyPacketList(List<Packet> start) {
		List<Packet> temp = new ArrayList<>();
		if(start != null) {
			for(Packet packet : start) {
				temp.add(packet.clone());
			}
		}
		return temp;
	}
	
	/**
	 * Method used to show the card
	 * 
	 * @return	A well-formatted string
	 */
	public String showCard(){
		
		StringBuilder cardBuilder = new StringBuilder();
		cardBuilder.append("NAME: "+this.name+"\n");
		cardBuilder.append("DESCRIPTION: "+this.description+"\n");
		cardBuilder.append("LEVEL: "+this.level+"\n");
		
		cardBuilder.append("COSTS: \n");
		if(costs.size() != 0){
			for (Packet cost : this.costs) {
				cardBuilder.append(cost.print()+"\n");
			}
		}
		if(requirements.size() != 0){
			cardBuilder.append("REQUIREMENTS: \n");
			for (Packet requirement: this.requirements){
				cardBuilder.append(requirement.print()+"\n");
			}
		}
		if(immediateEffects.size() != 0){
			cardBuilder.append("IMMEDIATE EFFECTS: \n");
			for (Effect immediate : this.immediateEffects){
				cardBuilder.append(immediate.print()+"\n");
			}
		}
		if(permanentEffects.size() != 0){
			cardBuilder.append("PERMANENT EFFECTS: \n");
			for (Effect permanent : this.permanentEffects){
				cardBuilder.append(permanent.print()+"\n");
			}
		}
		if(finalEffects.size() != 0){
			cardBuilder.append("FINAL EFFECTS: \n");
			for (Effect finalEff : this.finalEffects){
				cardBuilder.append(finalEff.print()+"\n");
			}
		}		
		
		return cardBuilder.toString();
	}
	
	/**
	 * Method used to clone the card
	 */
	@Override
	public Card clone() {
		//Create temporary arrays of card
		ArrayList<Packet> tempRequirements = new ArrayList<>();
		ArrayList<Packet> tempCosts = new ArrayList<>();
		ArrayList<Effect> tempImmediateEffects = new ArrayList<>();
		ArrayList<Effect> tempPermanentEffects = new ArrayList<>();
		ArrayList<Effect> tempFinalEffects = new ArrayList<>();
		
		//Copy the requirements
		if(requirements != null)
			for(Packet requirement : requirements)
				tempRequirements.add(requirement.clone());
		
		//Copy the costs
		if(costs != null)
			for(Packet cost : costs)
				tempCosts.add(cost.clone());
		
		//Copy the immediate effects
		if(immediateEffects != null)
			for(Effect immediateEffect : immediateEffects)
				tempImmediateEffects.add(immediateEffect.clone());
		
		//Copy the permanent effects
		if(permanentEffects != null)
			for(Effect permanentEffect : permanentEffects)
				tempPermanentEffects.add(permanentEffect.clone());
		
		//Copy the final effects
		if(finalEffects != null)
			for(Effect finalEffect : finalEffects)
				tempFinalEffects.add(finalEffect.clone());
		
		return new Card(getName(), getDescription(), getColor(), getPeriod(), getLevel(), tempCosts, tempImmediateEffects, tempRequirements, tempPermanentEffects, tempFinalEffects);
	}
}
