package it.polimi.ingsw.ps42.model.effect;


import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public class ForEachObtain extends Effect{
	//Obtain some resources for each requirements the player has
	
	private Packet requirements;
	private Packet gains;

	public ForEachObtain(Packet requirements, Packet gains) {
		super(EffectType.FOR_EACH_OBTAIN);
		this.requirements=requirements;
		this.gains=gains;
		
	}

	@Override
	public void enableEffect(Player player) {
		
		this.player=player;
		for (Unit unit : requirements) {
			//TO-DO:aggiungere metodo get risorse in player+ discutere gestione OR/AND dell'effetto
		}
	}

}
