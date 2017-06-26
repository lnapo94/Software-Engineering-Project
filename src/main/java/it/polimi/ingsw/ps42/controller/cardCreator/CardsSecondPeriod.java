package it.polimi.ingsw.ps42.controller.cardCreator;

import java.io.IOException;

import org.apache.log4j.Logger;

public class CardsSecondPeriod extends CardsCreator{
	
	//Logger
		private transient Logger logger = Logger.getLogger(CardsThirdPeriod.class);

	public CardsSecondPeriod() throws IOException {
		super("Resource//Cards//SecondPeriod//");
		logger.info("Load second period cards");
	}

	@Override
	public CardsCreator nextState() throws IOException {
		if(lowIndex == 0 && highIndex == 4) {
			logger.info("Load second period cards, second part");
			lowIndex = 4;
			highIndex = 8;
			return this;
		}
		return new CardsThirdPeriod();
	}

}
