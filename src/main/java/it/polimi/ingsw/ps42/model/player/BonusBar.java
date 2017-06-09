package it.polimi.ingsw.ps42.model.player;

import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import java.util.HashMap;

import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.Obtain;

public class BonusBar {
	//This is the player's little bonus bar. Every player can have the same bonusbar (simple)
	//or can have an advanced one (advanced mode)
	
	private Player player;
	private final Effect productBonus;
	private final Effect yieldBonus;
	
	private boolean hasBeenInitialized=false;
	
	public BonusBar(Effect productBonus, Effect yieldBonus) {
		//Advanced BonusBar for advanced game. Loaded from file
		//TO-DO: Check this code when implementing advanced mode
		this.productBonus = productBonus;
		this.yieldBonus = yieldBonus;

	}
	
	public BonusBar() {
		//Simple BonusBar for the game. Is the same for all the player 

		//Build the default yield Bonus
		HashMap<Resource, Integer> yieldBonus=new HashMap<>();
		yieldBonus.put(Resource.SLAVE,1);
		yieldBonus.put(Resource.WOOD,1);
		yieldBonus.put(Resource.STONE, 1);
		Packet yieldBonusPacket=new Packet(yieldBonus);
		Packet yieldCostPacket=new Packet();		//the cost have to be null
		this.yieldBonus=new Obtain(yieldCostPacket, yieldBonusPacket);

		//Build the default product Bonus
		HashMap<Resource,Integer> productBonus=new HashMap<>();
		productBonus.put(Resource.MONEY, 2);
		productBonus.put(Resource.MILITARYPOINT, 1);
		Packet productBonusPacket=new Packet(productBonus);
		Packet productCostPacket=new Packet(); 	//The cost have to be null
		this.productBonus=new Obtain(productCostPacket, productBonusPacket);
	}
	
	public void setPlayer(Player player) {		//Method to set the player, the set-up must be performed only once
		if(!hasBeenInitialized){
			this.player = player;
			hasBeenInitialized=true;
		}
		
	}
	
	public void yieldBonus() throws NullPointerException {
		//Apply the yield bonus when the player goes to the yield position 
		if(player!=null){
			yieldBonus.enableEffect(player);
		}
		else throw new NullPointerException("The bonus bar do not has a player! Set the player before applying the bonus");
	}
	
	public void productBonus() throws NullPointerException {
		//Apply the product bonus when the player goes to the product position
		if(player!=null){
			productBonus.enableEffect(player);
		}
		else throw new NullPointerException("The bonus bar do not has a player! Set the player before applying the bonus");
	}

	@Override
	public BonusBar clone() {

		return new BonusBar(productBonus.clone(), yieldBonus.clone());
	}
}
