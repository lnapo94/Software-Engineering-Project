package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.exception.WrongColorException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;

public class IncreaseFamiliarsPoint extends Effect{
	//In this effect all the familiars' points are increased
	
	private int value;

	public IncreaseFamiliarsPoint(int value) {
		super(EffectType.INCREASE_FAMILIARS);
		this.value=value;
	}

	@Override
	public void enableEffect(Player player) {
		//Increase the value of every familiar for the current round
		this.player=player;
		try{
			Familiar playerFamiliar=player.getFamiliar(FamiliarColor.ORANGE);
			playerFamiliar.setIncrement(value);
			playerFamiliar=player.getFamiliar(FamiliarColor.BLACK);
			playerFamiliar.setIncrement(value);
			playerFamiliar=player.getFamiliar(FamiliarColor.WHITE);
			playerFamiliar.setIncrement(value);
			playerFamiliar=player.getFamiliar(FamiliarColor.NEUTRAL);
			playerFamiliar.setIncrement(value);
		}
		catch (WrongColorException e) {
			System.out.println("Familiars increase failed beacause of a wrong initialization of the effect");
			//TO-DO:aggiungere metodo in wrong color exception che permetta append di un messaggio
		}
	}

	@Override
	public String print() {
		// TODO Auto-generated method stub
		return null;
	}

}
