package it.polimi.ingsw.ps42;

import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.player.BonusBar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public class TestClaudio {

	
	
	public static void main(String[] args) {
		
		//BonusBar bonusBar=new BonusBar();
		
		//Player p1=new Player("provaGiocatore", bonusBar);
		Packet packet=new Packet();
		System.out.println(packet);
		packet.addUnit(new Unit(Resource.STONE, 5));
		System.out.println(packet);
		/*p1.increaseResource(packet);
		System.out.println(p1.getResource(Resource.STONE));
		p1.enableBonus(ActionType.PRODUCE);
		System.out.println(p1.getResource(Resource.STONE));*/
		
	}
}
