package it.polimi.ingsw.ps42.model.action;


import java.util.List;

import it.polimi.ingsw.ps42.model.Printable;
import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.enumeration.Response;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.Position;
import it.polimi.ingsw.ps42.model.position.TowerPosition;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public class TakeCardAction extends Action{
	
	private StaticList<TowerPosition> tablePosition;
	private int positionInTableList;
	private List<Printable> possibleChoice;
	private List<Integer> possibleChoiceIndex;

	public TakeCardAction(ActionType type, Familiar familiar, StaticList<TowerPosition> tablePosition, int positionInTableList) throws NotEnoughResourcesException{
		//Constructor for normal action
		super(type, familiar);
		this.tablePosition = tablePosition;
		this.positionInTableList = positionInTableList;
	}
	public TakeCardAction(ActionType type, Player player, StaticList<TowerPosition> tablePosition, int positionInTableList, int actionValue){
		//Constructor for bonus action
		super(type, player, actionValue);
		this.tablePosition = tablePosition;
		this.positionInTableList = positionInTableList;
	}
	
	@Override
	public Response checkAction() {
		//Initial checks for the takeCard action, valid for both normal and bonus action
		
		//First: Check if the player can play
		if(!player.canPlay())
			return Response.CANNOT_PLAY;
		
		//Second: Active the IncreaseEffect in player, control the ban of the tower bonus position
		checkIncreaseEffect();
		
		//Third: Check if the position is free, if there aren't other familiar with
		//the same player, if there isn't the card, if the familiar can't stay in that position
		//and if the player has the requirements of the chosen card
		//Be careful to the neutral familiar
		
		//Take the chosen position
		TowerPosition position = tablePosition.get(positionInTableList);
		
		//If the position is empty
		if(position.isEmpty()) {
			//if the position has a card
			if(position.hasCard()) {
				//And player can position his familiar
				if(position.getLevel() <= actionValue) {
					//Check if there isn't any familiar with my color
					for(TowerPosition towerPosition : tablePosition) {
						//If the position contains a familiar
						if(!towerPosition.isEmpty()) {
							//First of all, if familiar has action same player
							if(towerPosition.getFamiliar().getPlayer() == player) {
								//Then, control if familiar isn't null, if the color isn't neutral and if the other familiar
								//the player has positioned in tower yet aren't the neutral familiar
								if(familiar != null && towerPosition.getFamiliar().getColor() != FamiliarColor.NEUTRAL && familiar.getColor() != FamiliarColor.NEUTRAL)
									return Response.FAILURE;
							}
						}
					}
				}
				List<Packet> requirements = position.getCard().getRequirements();
				for(Packet requirement : requirements) {
					boolean control = true;
					for(Unit unit : requirement) {
						if(unit.getQuantity() > player.getResource(unit.getResource()))
							control = false;
					}
					if(control == true);
						return Response.SUCCESS;
				}
				return Response.FAILURE;
			}
		}
		
		//Fourth: if the position has a bonus, apply it to the player
		if(position.getBonus() != null) {
			position.getBonus().enableEffect(player);
		}
		
		//Fifth: verify if there aren't any other player in the tower, else
		//decrease money in player
		for(Position towerPosition : tablePosition)
			if(!towerPosition.isEmpty() && towerPosition.getFamiliar().getPlayer() != player) {
				Packet moneyMalus = new Packet();
				moneyMalus.addUnit(new Unit(Resource.MONEY, 3));
				try {
					player.decreaseResource(moneyMalus);
				} catch (NotEnoughResourcesException e) {
					return Response.LOW_LEVEL;
				}
			}
		
		//Sixth: Control if the card can payed. If the card has only one cost, try to pay it
		//else create a PayRequest
		List<Packet> costs = position.getCard().getCosts();
		if(costs != null) {
			for(Packet packet : costs) {
				if(position.getCard().checkCosts(player) != null) {
					possibleChoice.add(position.getCard().checkCosts(player)); 
					possibleChoiceIndex.add(costs.indexOf(position.getCard().checkCosts(player)));
					}
			}
		}
		return Response.SUCCESS;
	}

	@Override
	public void doAction() {
		//Zero: check player request, if player has one request, satisfy it and stop
		//Take the card to the player, set player in card, remove card from the position, in case set familiar 
		//First: synchResource	
		//Second: enable immediateEffect
		//Pensare agli effetti permanenti carte blu
	}
}
