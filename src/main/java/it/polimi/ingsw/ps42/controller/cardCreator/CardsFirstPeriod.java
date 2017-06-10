package it.polimi.ingsw.ps42.controller.cardCreator;

import java.io.IOException;

public class CardsFirstPeriod extends CardsCreator{

	public CardsFirstPeriod() throws IOException {
		super("src/firstPeriod/");
	}

	@Override
	public CardsCreator nextState() throws IOException {
		if(lowIndex == 0 && highIndex == 4) {
			lowIndex = 4;
			highIndex = 8;
			return this;
		}
		return new CardsSecondPeriod();
	}

}
