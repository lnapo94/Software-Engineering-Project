package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.exception.WrongColorException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;

public class IncreaseSingleFamiliar extends Effect{
	//This effect increase only one familiar's value
	
	private int value;
	private FamiliarColor color;

	public IncreaseSingleFamiliar(int value, FamiliarColor color) {
		
		super(EffectType.INCREASE_SINGLE_FAMILIAR);
		this.value=value;
		this.color=color;
	}

	@Override
	public void enableEffect(Player player) {
		//Increments the value of a single familiar for the current round
		this.player=player;
		try{
			Familiar playerFamiliar=player.getFamiliar(color);
			playerFamiliar.setIncrement(value);
		}
		catch (WrongColorException e) {
			//e.printStackTrace("Familiars increase failed beacause of a wrong initialization of the effect");
			//TO-DO:aggiungere metodo in wrong color exception che permetta append di un messaggio
		}
	}

	@Override
	public String print() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IncreaseSingleFamiliar clone() {
		FamiliarColor cloneColor = this.color;
		return new IncreaseSingleFamiliar(this.value, cloneColor);
	}
	
}
