package it.polimi.ingsw.ps42.model.effect;

import java.util.ArrayList;

import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

public class Obtain extends Effect{
	//Obtain the indicated resources by paying of following costs
	
	private ArrayList<Packet> costs;
	private ArrayList<Packet> gains;
	
	//variable used to know when the effect is enabled (every time or just one time)
	private boolean enableOnActivation;

	public Obtain(EffectType typeOfEffect) {
		super(typeOfEffect);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enableEffect(Player player) {
		// TODO Auto-generated method stub
		
	}

}
