package it.polimi.ingsw.ps42.model.effect;

import java.util.ArrayList;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

public class ForEachObtain extends Effect{
	//Obtain some resources for each requirements the player has
	
	private ArrayList<Packet> requirements;
	private ArrayList<Packet> gains;

	public ForEachObtain(EffectType typeOfEffect) {
		super(typeOfEffect);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enableEffect(Player player) {
		// TODO Auto-generated method stub
		
	}

}
