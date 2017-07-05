package it.polimi.ingsw.ps42.model.player;

import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

import java.io.Serializable;
import java.util.HashMap;

import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.Obtain;

/**
 * Class that represents the bonus bars the player can have. This bonus bars give to the player
 * a bonus when he does a product or an yield action
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class BonusBar implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7853505964214729073L;
	//This is the player's little bonus bar. Every player can have the same bonusbar (simple)
	//or can have an advanced one (advanced mode)
	
	private Player player;
	private final Effect productBonus;
	private final Effect yieldBonus;
	private final String name;
	
	private boolean hasBeenInitialized=false;
	
	/**
	 * Constructor of the bonus bar
	 * @param productBonus		The effect to enable when the player does a product action
	 * @param yieldBonus		The effect to enable when the player does a yield action
	 * @param name				The name of the bonus bar to load it from a file
	 */
	public BonusBar(Effect productBonus, Effect yieldBonus, String name) {
		this.productBonus = productBonus;
		this.yieldBonus = yieldBonus;
		this.name = name;
	}
	
	/**
	 * Simple BonusBar for the game. Is the same for all the player 
	 */
	public BonusBar() {
				this.name = "default";
		//Build the default yield Bonus
		HashMap<Resource, Integer> yieldBonus=new HashMap<>();
		yieldBonus.put(Resource.SLAVE,1);
		yieldBonus.put(Resource.WOOD,1);
		yieldBonus.put(Resource.STONE, 1);
		Packet yieldBonusPacket=new Packet(yieldBonus);
		Packet yieldCostPacket=new Packet();		//the cost have to be null
		this.yieldBonus=new Obtain(yieldCostPacket, yieldBonusPacket, null);

		//Build the default product Bonus
		HashMap<Resource,Integer> productBonus=new HashMap<>();
		productBonus.put(Resource.MONEY, 2);
		productBonus.put(Resource.MILITARYPOINT, 1);
		Packet productBonusPacket=new Packet(productBonus);
		Packet productCostPacket=new Packet(); 	//The cost have to be null
		this.productBonus=new Obtain(productCostPacket, productBonusPacket, null);
	}
	
	/**
	 * Method used to set the player to the bonus bar
	 * @param player	The player to set to the bonus bar
	 */
	public void setPlayer(Player player) {		//Method to set the player, the set-up must be performed only once
		if(!hasBeenInitialized){
			this.player = player;
			hasBeenInitialized=true;
		}
		
	}
	
	/**
	 * Method used to enable the yield bonuses
	 * @throws NullPointerException		Thrown if the bonus bar doesn't have a setted player
	 */
	public void yieldBonus() throws NullPointerException {
		//Apply the yield bonus when the player goes to the yield position 
		if(player!=null){
			yieldBonus.enableEffect(player);
		}
		else throw new NullPointerException("The bonus bar do not has a player! Set the player before applying the bonus");
	}
	
	/**
	 * Method used to enable the product bonuses
	 * @throws NullPointerException		Thrown if the bonus bar doesn't have a setted player
	 */
	public void productBonus() throws NullPointerException {
		//Apply the product bonus when the player goes to the product position
		if(player!=null){
			productBonus.enableEffect(player);
		}
		else throw new NullPointerException("The bonus bar do not has a player! Set the player before applying the bonus");
	}

	/**
	 * Getter for the name of the bonus bar
	 * @return	The name of this bonus bar
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Method used to copy this bonus bar
	 */
	@Override
	public BonusBar clone() {
		return new BonusBar(productBonus.clone(), yieldBonus.clone(), name);
	}
	
	/**
	 * Method used to print this bonus bar
	 */
	@Override
	public String toString() {
		return "Produce Bonus: " + this.productBonus.print() + "\n" +
				"Yield Bonus" + this.yieldBonus.print();
	}
}
