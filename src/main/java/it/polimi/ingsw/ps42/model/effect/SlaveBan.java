package it.polimi.ingsw.ps42.model.effect;

import org.apache.log4j.Logger;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

public class SlaveBan extends Effect {
	//A player need more slaves to increase his familiars. This ban depends on the divisory.
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5371903224725167322L;
	private int divisory;
	
	//Logger
	private transient Logger logger = Logger.getLogger(SlaveBan.class);

	public SlaveBan(int divisory) {
		super(EffectType.SLAVE_BAN);
		this.divisory=divisory;
		
	}

	@Override
	public void enableEffect(Player player) {
		logger.info("Effect: " + this.getTypeOfEffect() + " activated");
		logger.info("Divisoru: " + this.divisory);
		this.player=player;
		
		player.setDivisory(divisory);
		
	}

	@Override
	public String print() {
		return "Player can increment something paying " + 1 / this.divisory + " slaves for 1 point";
	}
	
	@Override
	public SlaveBan clone() {
		return new SlaveBan(divisory);
	}
}
