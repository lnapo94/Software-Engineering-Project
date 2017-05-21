package it.polimi.ingsw.ps42.model.player;

import java.util.ArrayList;

import it.polimi.ingsw.ps42.model.effect.Effect;

public class BonusBar {
	//This is the player's little bonus bar. Every player can have the same bonusbar (simple)
	//or can have 
	
	private Player player;
	private ArrayList<Effect> productBonuses;
	private ArrayList<Effect> yieldBonuses;
	
	
	public BonusBar(Player player, ArrayList<Effect> productBonuses, ArrayList<Effect> yieldBonuses) {
		//Advanced BonusBar for advanced game. Loaded from file
	}
	
	public BonusBar(Player player) {
		//Simple BonusBar for the game. Is the same for all the player
	}
	
	public void yieldBonus() {
		//Apply the yield bonus when the player goes to the yield position
	}
	
	public void productBonus() {
		//Apply the product bonus when the player goes to the product position
	}

}
