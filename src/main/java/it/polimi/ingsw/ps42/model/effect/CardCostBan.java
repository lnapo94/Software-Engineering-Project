package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

public class CardCostBan extends Effect{
	//At the end of the match, the gamelogic calculate every cost of the indicated cards
	//and remove victory points from the player resources
	
	private String color;

	public CardCostBan(EffectType typeOfEffect) {
		super(typeOfEffect);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enableEffect(Player player) {
		// TODO Auto-generated method stub
		
	}

}
