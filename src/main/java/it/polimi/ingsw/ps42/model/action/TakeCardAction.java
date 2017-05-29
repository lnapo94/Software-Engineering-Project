package it.polimi.ingsw.ps42.model.action;


import java.util.List;

import it.polimi.ingsw.ps42.model.Printable;
import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.Response;
import it.polimi.ingsw.ps42.model.exception.NotEnoughResourcesException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.position.TowerPosition;

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
		
		//Second: Active the IncreaseEffect in player
		
		//Third: Check if the position is free, if there aren't other familiar with
		//the same player, if there isn't the card, if the familiar can't stay in that position
		//and if the player has the requirements of the chosen card
		//Be careful to the neutral familiar
		
		//Fourth: if the position has a bonus, apply it to the player
		
		//Fifth: verify if there aren't any other player in the tower, else
		//decrease money in player
		
		//Sixth: Control if the card can payed. If the card has only one cost, try to pay it
		//else create a PayRequest
		return Response.FAILURE;
	}

	@Override
	public void doAction() {
		//Zero: check player request, if player has one request, satisfy it and stop
		//Take the card to the player, set player in card, remove card from the position, in case set familiar 
		//First: synchResource	
		//Second: enable immediateEffect
	}

}
