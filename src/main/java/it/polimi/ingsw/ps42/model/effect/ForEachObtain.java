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
		int quantity;
		int quantityToObtain;
		Packet packet=new Packet();
		for (Unit unit1 : requirements) {
			quantity=player.getResource(unit1.getResource());	//ottengo da giocatore la quantità della risorsa richiesta 
			quantity=quantity/unit1.getQuantity();				//divido questa quantità per quella richiesta 
			for (Unit unit2 : gains) {
				quantityToObtain=quantity*unit2.getQuantity();		//la quantità finale da ottenere va moltiplicata per la quantità in gains
				packet.addUnit(new Unit(unit2.getResource(), quantityToObtain));	
			}
		}
		player.increaseResource(packet);
	}

	@Override
	public String print() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ForEachObtain clone() {
		Packet cloneRequirements = null;
		Packet cloneGains = null;
		if( requirements != null)
			cloneRequirements = requirements.clone();
		if( gains != null)
			cloneGains = gains.clone();
		return new ForEachObtain( cloneRequirements, cloneGains);
	}
}
