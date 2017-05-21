package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;

public class SlaveBan extends Effect {
	//A player need more slaves to increase his familiars. This ban depends on the divisory.
	
	private int divisory;

	public SlaveBan(int divisory) {
		super(EffectType.SLAVE_BAN);
		this.divisory=divisory;
		
	}

	@Override
	public void enableEffect(Player player) {
		this.player=player;
		
		//TO-DO: aggiungere a player un metodo per settare il divisore+var divisore. player.setDivisory(divisory);
		
	}
	

}
