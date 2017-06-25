package it.polimi.ingsw.ps42.model.player;

import java.util.Comparator;

import it.polimi.ingsw.ps42.model.enumeration.Resource;

public class VictoryPointComparator implements Comparator<Player>{

	@Override
	public int compare(Player o1, Player o2) {
		int difference = o1.getResource(Resource.VICTORYPOINT) - o2.getResource(Resource.VICTORYPOINT);
		
		if(difference > 0)
			return 1;
		if(difference < 0)
			return -1;
		
		return 0;					
	}

}
