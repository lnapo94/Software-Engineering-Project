package it.polimi.ingsw.ps42.model.player;

import java.util.Comparator;

import it.polimi.ingsw.ps42.model.enumeration.Resource;

/**
 * Class used to compare the players comparing the victory points
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class VictoryPointComparator implements Comparator<Player>{

	/**
	 * Method used to compare two players
	 */
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
