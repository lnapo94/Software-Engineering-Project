package it.polimi.ingsw.ps42.model.effect;

import it.polimi.ingsw.ps42.model.enumeration.Color;
import it.polimi.ingsw.ps42.model.enumeration.EffectType;
import it.polimi.ingsw.ps42.model.exception.WrongColorException;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;

public class IncreaseSingleFamiliar extends Effect{
	//This effect increase only one familiar's value
	
	private int value;
	private Color color;

	public IncreaseSingleFamiliar(int value, Color color) {
		//controllo sul colore passato?
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
			System.out.println("Incremento del familiare fallito perchè l'effetto è stato male inizializzato");
		}
	}

}
