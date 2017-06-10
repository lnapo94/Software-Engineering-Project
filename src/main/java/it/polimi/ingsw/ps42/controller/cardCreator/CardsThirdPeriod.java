package it.polimi.ingsw.ps42.controller.cardCreator;

import java.io.IOException;

public class CardsThirdPeriod extends CardsCreator{

	public CardsThirdPeriod() throws IOException {
		super("src/thirdPeriod");
	}

	@Override
	public CardsCreator nextState() {
		if(lowIndex == 0 && highIndex == 4) {
			lowIndex = 4;
			highIndex = 8;
			return this;
		}
		return null;
	}

}
